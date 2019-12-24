package com.rubbishman.rubbishRedux.external.setup;

import com.rubbishman.rubbishRedux.external.RubbishContainer;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.setup_extra.multiStageActions.MultiStageActionsProcessing;
import redux.api.Reducer;

public abstract class IRubbishReducer implements Reducer<ObjectStore> {
    protected RubbishContainer rubbishContainer;
    protected MultiStageActionsProcessing multistageAction;

    public void setMultiStageActions(MultiStageActionsProcessing multistageAction) {
        this.multistageAction = multistageAction;
    }

    public void setRubbishContainer(RubbishContainer rubbishContainer) {
        this.rubbishContainer = rubbishContainer;
    }

    public abstract ObjectStore reduce(ObjectStore state, Object action);
}
