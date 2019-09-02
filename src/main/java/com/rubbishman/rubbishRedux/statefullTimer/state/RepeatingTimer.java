package com.rubbishman.rubbishRedux.statefullTimer.state;

public class RepeatingTimer {
    public int id;
    public Timer timer;
    public int repeats;
    public int currentRepeats;

    public Object action;

    public RepeatingTimer clone() {
        RepeatingTimer cloned = new RepeatingTimer();
        cloned.id = this.id;
        cloned.timer = this.timer.clone();
        cloned.currentRepeats = this.currentRepeats;
        cloned.repeats = this.repeats;

        cloned.action = this.action;

        return cloned;
    }

    public String toString() {
        return "RepeatingTimer {" +
                " id: " + id +
                " startTime: " + timer.startTime +
                " period: " + timer.period +
                " currentRepeats: " + currentRepeats +
                " repeats: " + repeats +
                " action: " + action.toString();
    }
}
