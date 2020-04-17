package com.rubbishman.rubbishRedux.experimental.actionTrack;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishRedux.experimental.nestedHistoryScratcher.ScratchHistoryItem;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.Stage.Stage;

import java.util.*;

public class ActionTrack {
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
                public StageWrappedAction processStage(StageWrappedAction action) {
                    return action;
                }
            }
    );

    public ActionTrack(StageStack stageStack, PriorityQueue<StageWrappedAction> actionQueue, Stack<ScratchHistoryItem> actionHistory) {
        this.parent = null;

        this.stageStack = stageStack;
        this.actionHistory = actionHistory;
        this.actionQueue = actionQueue;
    }

    public ActionTrack(ActionTrack parent, StageStack stageStack, PriorityQueue<StageWrappedAction> actionQueue, Stack<ScratchHistoryItem> actionHistory) {
        this.parent = parent;

        this.stageStack = stageStack;
        this.actionHistory = actionHistory;
        this.actionQueue = actionQueue;
    }

    public ActionTrack(StageStack stageStack) {
        this.parent = null;

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
                            ImmutableList.of(FINAL_STAGE)
                    )
            );
        }
    }
}
