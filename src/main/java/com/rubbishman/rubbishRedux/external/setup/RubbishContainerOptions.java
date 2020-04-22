package com.rubbishman.rubbishRedux.external.setup;

import com.google.common.collect.ImmutableList;
import com.rubbishman.rubbishRedux.experimental.actionTrack.stage.StageWrap;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.Stage.Stage;
import com.rubbishman.rubbishRedux.external.operational.store.ObjectStore;
import com.rubbishman.rubbishRedux.external.operational.action.multistageAction.MultistageActionResolver;
import redux.api.enhancer.Middleware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RubbishContainerOptions {
    protected List<Middleware<ObjectStore>> middlewareList = new ArrayList<>();
    protected Map<Class,MultistageActionResolver<ObjectStore>> multistageActionList = new HashMap<>();
    protected IRubbishReducer reducer;

    protected ImmutableList<Stage> stages = ImmutableList.of();
    protected HashMap<String, Stage> stageNameMap = new HashMap<>();
    HashMap<Class, ImmutableList<StageWrap>> actionStageMap = new HashMap<>();

    public RubbishContainerOptions addMiddleware(Middleware<ObjectStore> middleware) {
        middlewareList.add(middleware);

        return this;
    }

    public RubbishContainerOptions addMultistageAction(Class clazz, MultistageActionResolver multistageActionResolver) {
        multistageActionList.put(clazz, multistageActionResolver);

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
