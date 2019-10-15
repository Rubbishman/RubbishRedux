package com.rubbishman.rubbishRedux.statefullTimer.state;

import java.util.ArrayList;

public class TimerState {
    public ArrayList<RepeatingTimer> timers = new ArrayList<>();

    public TimerState clone() {
        TimerState cloned = new TimerState();
        cloned.timers.addAll(this.timers); // aliasing -> This costs a lot...

        return cloned;
    }
}
