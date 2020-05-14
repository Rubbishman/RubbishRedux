package com.rubbishman.rubbishRedux.internal.statefullTimer.reducer;

import com.rubbishman.rubbishRedux.external.operational.store.IdObject;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.setup.IRubbishReducer;
import com.rubbishman.rubbishRedux.internal.statefullTimer.action.IncrementTimer;
import com.rubbishman.rubbishRedux.internal.statefullTimer.helper.TimerHelper;
import com.rubbishman.rubbishRedux.internal.statefullTimer.state.RepeatingTimer;
import redux.api.Reducer;

public class TimerReducer extends IRubbishReducer {
    public ObjectStore reduce(ObjectStore state, IncrementTimer action) {
        RepeatingTimer timer = state.getObject(action.subject);

        RepeatingTimer newTimer = timer.changeRepeats(TimerHelper.calculateNewRepeats(action.nowTime, timer));

        ObjectStore cloned = state.setObject(action.subject, newTimer);

        int diff = newTimer.currentRepeats - timer.currentRepeats;

        while(diff > 0) {
            this.currentActionTrack.performActionImmediately(timer.action); //These will happen before next action
            diff--;
        }

        return cloned;
    }

    public ObjectStore reduce(ObjectStore state, Object action) {
        if (action instanceof IncrementTimer) {
            return reduce(state, (IncrementTimer)action);
        }
        return state;
    }
}
