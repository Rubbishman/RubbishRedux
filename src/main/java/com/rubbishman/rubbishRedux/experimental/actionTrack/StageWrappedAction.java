package com.rubbishman.rubbishRedux.experimental.actionTrack;

import com.google.common.collect.ImmutableList;

import java.util.HashMap;

public class StageWrappedAction<T> implements Comparable {
    public final T originalAction;
    public final Object currentAction;
    public final HashMap<Long, Object> stageResults;
    public final ImmutableList<StageWrap> stages;
    public final int currentStage;

    public StageWrappedAction(T action, ImmutableList<StageWrap> stages) {
        this.originalAction = action;
        currentAction = action;
        this.stages = stages;
        this.currentStage = 0;
        stageResults = new HashMap<>();
    }

    public StageWrappedAction(T action, ImmutableList<StageWrap> stages, int currentStage) {
        this.originalAction = action;
        currentAction = action;
        this.stages = stages;
        this.currentStage = currentStage;
        stageResults = new HashMap<>();
    }

    public StageWrappedAction(T action, Object currentAction, HashMap<Long, Object> stageResults, ImmutableList<StageWrap> stages, int currentStage) {
        this.originalAction = action;
        this.currentAction = currentAction;
        this.stageResults = stageResults;
        this.stages = stages;
        this.currentStage = currentStage;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof StageWrappedAction) {
            StageWrappedAction other = (StageWrappedAction) o;

            return stages.get(currentStage).compareTo(other.stages.get(other.currentStage));
        }

        return 0;
    }
}
