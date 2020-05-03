package com.rubbishman.rubbishRedux.experimental.actionTrack.stage;

import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.Stage.Stage;

public class StageCreator {
    private long priority = 0;

    public Stage createStage(String name) {
        return new Stage(name, priority++);
    }
}
