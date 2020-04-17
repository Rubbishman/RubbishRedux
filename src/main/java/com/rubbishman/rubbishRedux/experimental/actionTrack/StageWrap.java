package com.rubbishman.rubbishRedux.experimental.actionTrack;

import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.Stage.Stage;

public class StageWrap<T> implements Comparable {
    public final Stage stage;
    public final StageProcessor<T> stageProcessor;

    public StageWrap(Stage stage, StageProcessor<T> stageProcessor) {
        this.stage = stage;
        this.stageProcessor = stageProcessor;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof StageWrap) {
            StageWrap other = (StageWrap)o;

            return this.stage.compareTo(other.stage);
        }

        return 0;
    }
}
