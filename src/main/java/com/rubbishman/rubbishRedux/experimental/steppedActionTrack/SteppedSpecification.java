package com.rubbishman.rubbishRedux.experimental.steppedActionTrack;

public abstract class SteppedSpecification {
    public static SteppedSpecification STEP_ONCE = new SteppedSpecification() {
        private int counter = 0;

        boolean shouldStop() {
            return counter > 0;
        }

        public void resume() {
            counter = 0;
            super.resume();
        }

        void observeAction() {
            counter++;
        }
    };

    protected ISteppedActionTrack steppedActionTrack;
    public void setResumePoint(ISteppedActionTrack steppedActionTrack) {
        this.steppedActionTrack = steppedActionTrack;
    }

    public void resume() {
        steppedActionTrack.resume();
    }

    abstract void observeAction();
    abstract boolean shouldStop();
}
