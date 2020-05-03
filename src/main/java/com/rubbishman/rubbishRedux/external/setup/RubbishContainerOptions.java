package com.rubbishman.rubbishRedux.external.setup;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishRedux.experimental.actionTrack.TickSystem;
import com.rubbishman.rubbishRedux.experimental.actionTrack.stage.StageWrap;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.Stage.Stage;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
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

    public RubbishContainerOptions registerTickSystem(TickSystem tickSystem) {
        registeredTickSystems.add(tickSystem);

        return this;
    }

    public RubbishContainerOptions addMiddleware(Middleware<ObjectStore> middleware) {
        middlewareList.add(middleware);

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
