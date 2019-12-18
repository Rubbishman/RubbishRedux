package com.rubbishman.rubbishRedux.statefullTimer.action;

import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.Identifier;

public class IncrementTimer {
    public final long nowTime;
    public final Identifier subject;

    public IncrementTimer(long nowTime, Identifier subject) {
        this.nowTime = nowTime;
        this.subject = subject;
    }
}
