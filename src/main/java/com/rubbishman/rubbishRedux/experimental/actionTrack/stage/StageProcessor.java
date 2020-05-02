package com.rubbishman.rubbishRedux.experimental.actionTrack.stage;

import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;

public interface StageProcessor {
    /**
     * Process the action for this stage, returning the one of, or both of processedAction or dispatchAction
     * @param action
     * @return
     */
    StageWrapResult processStage(ObjectStore state, StageWrappedAction action);
}
