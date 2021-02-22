package com.rubbishman.rubbishRedux.external.setup;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishRedux.external.setup_extra.TickSystem;
import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.ActionTrackListener;
import com.rubbishman.rubbishRedux.external.setup_extra.actionTrack.stage.StageWrap;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.Stage.Stage;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.internal.timekeeper.TimeKeeper;
import redux.api.enhancer.Middleware;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RubbishContainerOptions {
    protected List<Middleware<ObjectStore>> middlewareList = new ArrayList<>();
    protected IRubbishReducer reducer;
    protected ImmutableList<Stage> stages = ImmutableList.of();
    protected HashMap<String, Stage> stageNameMap = new HashMap<>();
    protected HashMap<Class, ImmutableList<StageWrap>> actionStageMap = new HashMap<>();
    protected ArrayList<TickSystem> registeredTickSystems = new ArrayList<>();
    protected TimeKeeper timeKeeper;
    protected HashMap<Stage, ArrayList<ActionTrackListener>> listeners = new HashMap<>();
    protected boolean isSteppedActionTracker;

    public void setSteppedActionTracker(boolean isSteppedActionTracker) {
        this.isSteppedActionTracker = isSteppedActionTracker;
    }

    public RubbishContainerOptions addListener(Stage stage, ActionTrackListener listener) {
        if(!listeners.containsKey(stage)) {
            listeners.put(stage, new ArrayList<>());
        }

        listeners.get(stage).add(listener);

        return this;
    }

    public RubbishContainerOptions registerTickSystem(TickSystem tickSystem) {
        registeredTickSystems.add(tickSystem);

        return this;
    }

    public RubbishContainerOptions addMiddleware(Middleware<ObjectStore> middleware) {
        middlewareList.add(middleware);

        return this;
    }

    public RubbishContainerOptions setTimeKeeper(TimeKeeper timeKeeper) {
        this.timeKeeper = timeKeeper;

        return this;
    }

    public void setReducer(IRubbishReducer reducer) {
        this.reducer = reducer;
    }

    private long stagePriority = 0;

    public Stage createStage(String name) throws Exception {
        if(stageNameMap.get(name) != null) {
            throw new Exception("Stage already exists: " + name);
        }

        Stage stage = new Stage(name, stagePriority++);

        stageNameMap.put(name, stage);

        stages = ImmutableList.<Stage>builder().addAll(stages).add(stage).build();

        return stage;
    }

    public void setStageProcessor(Class clazz, ImmutableList<StageWrap> stageWraps) throws Exception {
        if(actionStageMap.get(clazz) != null) {
            throw new Exception(clazz.getName() + " already has stage processors");
        }

        for(StageWrap stageWrap : stageWraps) {
            if(stageNameMap.get(stageWrap.stage.identifier) == null) {
                throw new Exception("Stage not defined: " + stageWrap.stage.identifier);
            }
        }

        actionStageMap.put(
                clazz,
                stageWraps
        );
    }
}
