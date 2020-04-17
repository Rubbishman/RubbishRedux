package com.rubbishman.rubbishRedux.experimental.actionTrack;

public interface StageProcessor<T> {
    StageWrappedAction<T> processStage(StageWrappedAction<T> action);
}
