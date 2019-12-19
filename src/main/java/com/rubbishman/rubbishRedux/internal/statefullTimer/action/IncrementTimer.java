package com.rubbishman.rubbishRedux.internal.statefullTimer.action;

import com.rubbishman.rubbishRedux.external.operational.store.Identifier;

public class IncrementTimer {
    public final long nowTime;
    public final Identifier subject;

    public IncrementTimer(long nowTime, Identifier subject) {
        this.nowTime = nowTime;
        this.subject = subject;
    }
}
