package com.rubbishman.rubbishRedux.experimental.actionTrack;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishRedux.experimental.actionTrack.stage.*;
import com.rubbishman.rubbishRedux.experimental.nestedHistoryScratcher.ScratchHistoryItem;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.Stage.Stage;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import redux.api.Store;

import java.util.*;

public class ActionTrack {
    private Store<ObjectStore> store;

    public final ActionTrack parent;
    public final StageStack stageStack;

    public PriorityQueue<StageWrappedAction> actionQueue;
    public Stack<ScratchHistoryItem> actionHistory;

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

    public ActionTrack(Store<ObjectStore> store, StageStack stageStack, PriorityQueue<StageWrappedAction> actionQueue, Stack<ScratchHistoryItem> actionHistory) {
        this.parent = null;
        this.store = store;
        this.stageStack = stageStack;
        this.actionHistory = actionHistory;
        this.actionQueue = actionQueue;
    }

    public ActionTrack(Store<ObjectStore> store, ActionTrack parent, StageStack stageStack, PriorityQueue<StageWrappedAction> actionQueue, Stack<ScratchHistoryItem> actionHistory) {
        this.parent = parent;
        this.store = store;
        this.stageStack = stageStack;
        this.actionHistory = actionHistory;
        this.actionQueue = actionQueue;
    }

    public ActionTrack(Store<ObjectStore> store,StageStack stageStack) {
        this.parent = null;
        this.store = store;
        this.stageStack = stageStack;
        actionHistory = new Stack<>();
        actionQueue = new PriorityQueue<>();
    }

    public ActionTrack(Store<ObjectStore> store) {
        this.parent = null;
        this.store = store;
        this.stageStack = new StageStack(new HashMap());
        actionHistory = new Stack<>();
        actionQueue = new PriorityQueue<>();
    }

    //TODO, external version does not expose StageWrappedAction function..?
    public void addAction(Object action) {
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

            if(result.dispatchAction != null) {
                store.dispatch(result.dispatchAction);
            }

            if(result.processedAction != null
                    && wrappedAction.currentStage < wrappedAction.stages.size() - 1) {

                wrappedAction.stageResults.put(currentStage.stage.priority, result.stageObject);

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

    public ActionTrack isolate() {
        PriorityQueue<StageWrappedAction> isolateActionQueue = this.actionQueue;

        this.actionQueue = new PriorityQueue();

        return new ActionTrack(
                store,
                stageStack,
                isolateActionQueue,
                actionHistory
        );
    }
}
