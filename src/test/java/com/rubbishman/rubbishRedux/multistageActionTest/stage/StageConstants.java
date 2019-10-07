package com.rubbishman.rubbishRedux.multistageActionTest.stage;

import com.rubbishman.rubbishRedux.multistageActions.stage.Stage;

public class StageConstants {
    public static final String NAME_INCREMENT_RESOLUTION = "increment_resolution";
    public static final long PRIORITY_INCREMENT_RESOLUTION = 1;

    public static final Stage INCREMENT_RESOLUTION = new Stage(NAME_INCREMENT_RESOLUTION, PRIORITY_INCREMENT_RESOLUTION);
}
