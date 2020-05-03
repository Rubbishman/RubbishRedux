package com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.stage;

public class StageWrapResult {
    public final Object processedAction;
    public final Object stageObject;
    public final Object dispatchAction;

    public StageWrapResult(Object action) {
        this.processedAction = action;
        this.stageObject = action;
        this.dispatchAction = action;
    }

    public StageWrapResult(Object processedAction, Object stageObject, Object dispatchAction) {
        this.processedAction = processedAction;
        this.stageObject = stageObject;
        this.dispatchAction = dispatchAction;
    }
}
