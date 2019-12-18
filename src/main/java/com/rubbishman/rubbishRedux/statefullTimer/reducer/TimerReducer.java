package com.rubbishman.rubbishRedux.statefullTimer.reducer;

import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.IdObject;
import com.rubbishman.rubbishRedux.internal.dynamicObjectStore.store.ObjectStore;
import com.rubbishman.rubbishRedux.statefullTimer.action.IncrementTimer;
import com.rubbishman.rubbishRedux.statefullTimer.helper.TimerHelper;
import com.rubbishman.rubbishRedux.statefullTimer.state.RepeatingTimer;
import redux.api.Reducer;

public class TimerReducer implements Reducer<ObjectStore> {
    public ObjectStore reduce(ObjectStore state, IncrementTimer action) {
        RepeatingTimer timer = (RepeatingTimer)state.objectMap.get(action.subject).object;

        RepeatingTimer newTimer = timer.changeRepeats(TimerHelper.calculateNewRepeats(action.nowTime, timer));

        ObjectStore cloned = new ObjectStore(
                state.objectMap.assoc(action.subject, new IdObject(action.subject, newTimer)),
                state.idGenerator
        );

        int diff = newTimer.currentRepeats - timer.currentRepeats;

        while(diff > 0) {
            cloned = this.reduce(cloned, timer.action); //TODO, this is a bug...
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
