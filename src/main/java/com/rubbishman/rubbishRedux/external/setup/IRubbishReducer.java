package com.rubbishman.rubbishRedux.external.setup;

import com.rubbishman.rubbishRedux.external.RubbishContainer;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.ActionTrack;
import redux.api.Reducer;

public abstract class IRubbishReducer implements Reducer<ObjectStore> {
    protected RubbishContainer rubbishContainer;
    protected ActionTrack currentActionTrack;

    public void setRubbishContainer(RubbishContainer rubbishContainer) {
        this.rubbishContainer = rubbishContainer;
    }

    public long getNowTime() {
        return rubbishContainer.getNowTime();
    }

    public long getElapsedTime() {
        return rubbishContainer.getElapsedTime();
    }

    public void setCurrentActionTrack(ActionTrack currentActionTrack) {
        this.currentActionTrack = currentActionTrack;
    }

    public abstract ObjectStore reduce(ObjectStore state, Object action);
}
