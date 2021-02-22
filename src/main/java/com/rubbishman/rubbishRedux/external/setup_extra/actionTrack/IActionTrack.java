package com.rubbishman.rubbishRedux.external.setup_extra.actionTrack;

import com.rubbishman.rubbishRedux.experimental.steppedActionTrack.SteppedActionTrack;
import com.rubbishman.rubbishRedux.experimental.steppedActionTrack.SteppedSpecification;

public interface IActionTrack {
    void addAction(Object action);
    IActionTrack isolate();
    void processActions();
    void performActionImmediately(Object action);
    void setSteppedSpecification(SteppedSpecification spec);
}
