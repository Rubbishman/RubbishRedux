package com.rubbishman.rubbishRedux.experimental.steppedActionTrack;

public interface ISteppedSpecification {
    boolean shouldStop();
    void setResumePoint(ISteppedActionTrack steppedActionTrack);
    void resume();
}
