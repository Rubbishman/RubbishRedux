package com.rubbishman.rubbishRedux.experimental.actionTrack;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishRedux.experimental.nestedHistoryScratcher.ScratchHistoryItem;
import com.rubbishman.rubbishRedux.external.RubbishContainer;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.Stage.Stage;

import java.util.*;

public class ActionTrack {
    private RubbishContainer rubbish;

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
                public StageWrapResult processStage(StageWrappedAction action) {
                    return new StageWrapResult(
                            action.originalAction
                    );
                }
            }
    );

    public static final ImmutableList<StageWrap> FINAL_STAGE_LIST = ImmutableList.of(FINAL_STAGE);

    public ActionTrack(RubbishContainer rubbish, StageStack stageStack, PriorityQueue<StageWrappedAction> actionQueue, Stack<ScratchHistoryItem> actionHistory) {
        this.parent = null;
        this.rubbish = rubbish;
        this.stageStack = stageStack;
        this.actionHistory = actionHistory;
        this.actionQueue = actionQueue;
    }

    public ActionTrack(RubbishContainer rubbish,ActionTrack parent, StageStack stageStack, PriorityQueue<StageWrappedAction> actionQueue, Stack<ScratchHistoryItem> actionHistory) {
        this.parent = parent;
        this.rubbish = rubbish;
        this.stageStack = stageStack;
        this.actionHistory = actionHistory;
        this.actionQueue = actionQueue;
    }

    public ActionTrack(RubbishContainer rubbish,StageStack stageStack) {
        this.parent = null;
        this.rubbish = rubbish;
        this.stageStack = stageStack;
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
            StageWrapResult result = currentStage.stageProcessor.processStage(wrappedAction);

            if(result.dispatchAction != null) {
                rubbish.performAction(result.dispatchAction);
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
}
