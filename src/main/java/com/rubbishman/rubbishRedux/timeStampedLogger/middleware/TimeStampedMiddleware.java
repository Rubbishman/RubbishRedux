package com.rubbishman.rubbishRedux.timeStampedLogger.middleware;

import com.rubbishman.rubbishRedux.timeStampedLogger.action.TimeStampedAction;
import redux.api.Dispatcher;
import redux.api.Store;
import redux.api.enhancer.Middleware;

import java.util.ArrayList;

public class TimeStampedMiddleware implements Middleware {
    private ArrayList<TimeStampedAction> logging = new ArrayList<>();

    public void dispatchLogInstantly(Store store) {
        ArrayList<TimeStampedAction> cloneOfLog = new ArrayList<>();
        cloneOfLog.addAll(logging);

        for(TimeStampedAction timeStampedAction : cloneOfLog) {
            store.dispatch(timeStampedAction.action);
        }
    }

    public void dispatchLogRealtime(Store store) {
        ArrayList<TimeStampedAction> cloneOfLog = new ArrayList<>();
        cloneOfLog.addAll(logging);

        for(TimeStampedAction timeStampedAction : cloneOfLog) {
            store.dispatch(timeStampedAction.action);
        }
    }

    public int getLogSize() {
        return logging.size();
    }

    public void cleanLog() {
        logging = new ArrayList<>();
    }

    public Object dispatch(Store store, Dispatcher next, Object action) {
        Object dispatchAction;
        TimeStampedAction timeStampedAction;
        if(action instanceof TimeStampedAction) {
            timeStampedAction = (TimeStampedAction)action;
            dispatchAction = timeStampedAction.action;
        } else {
            timeStampedAction = new TimeStampedAction(System.nanoTime(), action);
            dispatchAction = action;
        }

        logging.add(timeStampedAction);

        return next.dispatch(dispatchAction);
    }
}
