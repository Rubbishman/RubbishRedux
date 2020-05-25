package com.rubbishman.rubbishRedux.external.setup_extra.actionTrack;

import java.util.HashMap;

public interface ActionTrackListener {
    void listen(HashMap<Long, Object> stageResults);
}
