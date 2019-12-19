package com.rubbishman.rubbishRedux.internal.multistageActionTest.stage;

import com.rubbishman.rubbishRedux.internal.multistageActions.stage.Stage;

public class StageConstants {
    private static final String NAME_COUNTER_CREATE_RESOLUTION = "counter_create_resolution";
    private static final long PRIORITY_COUNTER_CREATE_RESOLUTION = 1;

    public static final Stage COUNTER_CREATE_RESOLUTION = new Stage(NAME_COUNTER_CREATE_RESOLUTION, PRIORITY_COUNTER_CREATE_RESOLUTION);

    private static final String NAME_INCREMENT_RESOLUTION = "increment_resolution";
    private static final long PRIORITY_INCREMENT_RESOLUTION = 2;

    public static final Stage INCREMENT_RESOLUTION = new Stage(NAME_INCREMENT_RESOLUTION, PRIORITY_INCREMENT_RESOLUTION);
}
