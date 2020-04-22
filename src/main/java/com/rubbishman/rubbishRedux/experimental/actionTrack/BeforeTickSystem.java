package com.rubbishman.rubbishRedux.experimental.actionTrack;

import com.rubbishman.rubbishRedux.internal.statefullTimer.logic.TimerLogic;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface BeforeTickSystem {
    LinkedList<TimerLogic> beforeDispatchStarted(ConcurrentLinkedQueue<Object> actionQueue, Long nowTime);
}
