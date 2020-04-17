package com.rubbishman.rubbishRedux.experimental.actionTrack;

import com.google.common.collect.ImmutableList;

import java.util.HashMap;

public class StageStack {
    public final HashMap<Class, ImmutableList<StageWrap>> actionStageMap;

    public StageStack(HashMap<Class, ImmutableList<StageWrap>> actionStageMap) {
        this.actionStageMap = actionStageMap;
    }
}
