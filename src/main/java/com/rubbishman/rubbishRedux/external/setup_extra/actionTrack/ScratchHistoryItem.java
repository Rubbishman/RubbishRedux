package com.rubbishman.rubbishRedux.external.setup_extra.actionTrack;

import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;

public class ScratchHistoryItem {
    public final ObjectStore state;
    public final Object action;

    public ScratchHistoryItem(ObjectStore state, Object action) {
        this.state = state;
        this.action = action;
    }
}
