package com.rubbishman.rubbishRedux.experimental.actionTrack.stage;

public interface StageProcessor {
    /**
     * Process the action for this stage, returning the one of, or both of processedAction or dispatchAction
     * @param action
     * @return
     */
    StageWrapResult processStage(StageWrappedAction action);
}
