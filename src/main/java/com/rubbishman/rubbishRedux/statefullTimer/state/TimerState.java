package com.rubbishman.rubbishRedux.statefullTimer.state;

import java.util.ArrayList;

public class TimerState {
    public ArrayList<RepeatingTimer> timers = new ArrayList<>();

    public TimerState clone() {
        TimerState state = new TimerState();
        state.timers = this.timers; // aliasing -> This costs a lot...

        return state;
    }

    public String toString() {
        return timers.toString();
    }
}