package com.rubbishman.rubbishRedux.external.setup_extra.actionTrack;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishRedux.external.setup_extra.TickSystem;
import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.stage.*;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.Stage.Stage;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.internal.RubbishReducer;
import com.rubbishman.rubbishRedux.internal.timekeeper.TimeKeeper;
import redux.api.Store;

import java.util.*;

public class ActionTrack {
    private Store<ObjectStore> store;
//    private ObjectStore currentState;
    private RubbishReducer reducer;
    public final ActionTrack parent;
    public final StageStack stageStack;

    private HashMap<Stage, ArrayList<ActionTrackListener>> listeners;

    private PriorityQueue<StageWrappedAction> actionQueue;
    private Stack<ScratchHistoryItem> actionHistory;
    private ArrayList<TickSystem> registeredTickSystems;
    private TimeKeeper timeKeeper;
    private Boolean processingAction = false;

    private PriorityQueue<StageWrappedAction> childActionQueue;

    public static final StageWrap FINAL_STAGE = new StageWrap(
            new Stage(
                    "final_stage",
                    Long.MAX_VALUE
            ),
            new StageProcessor() {
                @Override
                public StageWrapResult processStage(ObjectStore state, StageWrappedAction action) {
                    return new StageWrapResult(
                            null,
                            action.originalAction,
                            action.originalAction
                    );
                }
            }
    );

    public static final ImmutableList<StageWrap> FINAL_STAGE_LIST = ImmutableList.of(FINAL_STAGE);

    public ActionTrack(
        TimeKeeper timeKeeper,
        ArrayList<TickSystem> registeredTickSystems,
        Store<ObjectStore> store,
        RubbishReducer reducer,
        StageStack stageStack,
        PriorityQueue<StageWrappedAction> actionQueue,
        Stack<ScratchHistoryItem> actionHistory,
        HashMap<Stage, ArrayList<ActionTrackListener>> listeners
    ) {
        this.parent = null;
        this.store = store;
        this.stageStack = stageStack;
        this.actionHistory = actionHistory;
        this.actionQueue = actionQueue;
        childActionQueue = new PriorityQueue<>();
        this.registeredTickSystems = registeredTickSystems;
        this.timeKeeper = timeKeeper;
        this.reducer = reducer;
        this.listeners = listeners;
    }

    public ActionTrack(
            TimeKeeper timeKeeper,
            ArrayList<TickSystem> registeredTickSystems,
            Store<ObjectStore> store,
            RubbishReducer reducer,
            ActionTrack parent,
            StageStack stageStack,
            PriorityQueue<StageWrappedAction> actionQueue,
            Stack<ScratchHistoryItem> actionHistory,
            HashMap<Stage, ArrayList<ActionTrackListener>> listeners
    ) {
        this.parent = parent;
        this.store = store;
        this.stageStack = stageStack;
        this.actionHistory = actionHistory;
        this.actionQueue = actionQueue;
        childActionQueue = new PriorityQueue<>();
        this.registeredTickSystems = registeredTickSystems;
        this.timeKeeper = timeKeeper;
        this.reducer = reducer;
        this.listeners = listeners;
    }

    public ActionTrack(
            TimeKeeper timeKeeper,
            ArrayList<TickSystem> registeredTickSystems,
            Store<ObjectStore> store,
            RubbishReducer reducer,
            StageStack stageStack,
            HashMap<Stage, ArrayList<ActionTrackListener>> listeners
    ) {
        this.parent = null;
        this.store = store;
        this.stageStack = stageStack;
        actionHistory = new Stack<>();
        actionQueue = new PriorityQueue<>();
        childActionQueue = new PriorityQueue<>();
        this.registeredTickSystems = registeredTickSystems;
        this.timeKeeper = timeKeeper;
        this.reducer = reducer;
        this.listeners = listeners;
    }

    public void addAction(Object action) {
        addAction(this.actionQueue, action);
    }

    private void addAction(PriorityQueue<StageWrappedAction> actionQueue, Object action) {
        if(action instanceof StageWrappedAction) {
            actionQueue.add((StageWrappedAction)action);
        }

        if(stageStack.actionStageMap.containsKey(action.getClass())) {
            actionQueue.add(
                    new StageWrappedAction(
                            action,
                            stageStack.actionStageMap.get(action.getClass())
                    )
            );
        } else {
            actionQueue.add(
                    new StageWrappedAction(
                            action,
                            FINAL_STAGE_LIST
                    )
            );
        }
    }

    public boolean hasNext() {
        return actionQueue.peek() != null;
    }

    public void processNextAction() {
        StageWrappedAction wrappedAction = actionQueue.poll();

        if(wrappedAction != null) {
            StageWrap currentStage = wrappedAction.stages.get(wrappedAction.currentStage);
            StageWrapResult result = currentStage.stageProcessor.processStage(store.getState(), wrappedAction);

            wrappedAction.stageResults.put(currentStage.stage.priority, result.stageObject);

            if(result.dispatchAction != null) {
                store.dispatch(result.dispatchAction);
            }

            if(listeners.containsKey(currentStage.stage)) {
                for(ActionTrackListener listener: listeners.get(currentStage.stage)) {
                    listener.listen(wrappedAction.stageResults);
                }
            }

            if(result.processedAction != null
                    && wrappedAction.currentStage < wrappedAction.stages.size() - 1) {

                actionQueue.add(new StageWrappedAction(
                    wrappedAction.originalAction,
                        result.processedAction,
                        wrappedAction.stageResults,
                        wrappedAction.stages,
                        wrappedAction.currentStage + 1
                ));
            }
        }
    }

    public void processActions() {
        for(TickSystem tickSystem : registeredTickSystems) {
            tickSystem.beforeDispatchStarted(this, timeKeeper.getNowTime());
        }

        internalProcessActions();

        for(TickSystem tickSystem : registeredTickSystems) {
            tickSystem.afterDispatchFinished();
        }
    }

    private void internalProcessActions() {
        boolean wasProcessingActionTrue;
        synchronized (processingAction) {
            wasProcessingActionTrue = processingAction;
        }

        reducer.setCurrentActionTrack(this);

        while(this.hasNext()) {
            if(!wasProcessingActionTrue) {
                synchronized (processingAction) {
                    processingAction = true;
                }
                this.processNextAction();
                synchronized (processingAction) {
                    processingAction = false;
                }

                isolateChildActions();

            } else {
                this.processNextAction();
            }
        }
    }

    private void isolateChildActions() {
        synchronized(childActionQueue) {
            if(!childActionQueue.isEmpty()) {
                ActionTrack child = new ActionTrack(
                        timeKeeper,
                        registeredTickSystems,
                        store,
                        reducer,
                        this,
                        stageStack,
                        childActionQueue,
                        actionHistory,
                        listeners
                );

                childActionQueue = new PriorityQueue<>();
                child.internalProcessActions(); // Does not do tick systems, since those should only happen at top level.
                reducer.setCurrentActionTrack(this); //Return to our current level...
            }
        }
    }

    public void performActionThisTick(Object action) {
        synchronized (processingAction) {
            if(processingAction) {
                this.addAction(action);
            } else {
                throw new RuntimeException();
            }
        }
    }

    public void performActionImmediately(Object action) {
        synchronized (processingAction) {
            if(processingAction) {
                addAction(this.childActionQueue, action);
            } else {
                throw new RuntimeException();
            }
        }
    }

    public void performActionsImmediately(Object... actions) {
        synchronized (processingAction) {
            if(processingAction) {
                for(Object action : actions) {
                    addAction(this.childActionQueue, action);
                }
            } else {
                throw new RuntimeException();
            }
        }
    }

    public ActionTrack isolate() {
        PriorityQueue<StageWrappedAction> isolateActionQueue = this.actionQueue;

        this.actionQueue = new PriorityQueue();

        return new ActionTrack(
                timeKeeper,
                registeredTickSystems,
                store,
                reducer,
                stageStack,
                isolateActionQueue,
                actionHistory,
                listeners
        );
    }
}
