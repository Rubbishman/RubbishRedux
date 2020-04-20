package com.rubbishman.rubbishRedux.experimental.actionTrack;

public interface StageProcessor<T> {
    /**
     * Process the action for this stage, returning the one of, or both of processedAction or dispatchAction
     * @param action
     * @return
     */
    StageWrapResult processStage(StageWrappedAction action);
}
