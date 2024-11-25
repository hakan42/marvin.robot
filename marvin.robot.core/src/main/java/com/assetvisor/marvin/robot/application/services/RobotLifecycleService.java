package com.assetvisor.marvin.robot.application.services;

import com.assetvisor.marvin.equipment.notebook.NoteBook;
import com.assetvisor.marvin.robot.domain.brain.ForInvokingBrain;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentDescription;
import com.assetvisor.marvin.robot.domain.environment.ForGettingEnvironmentDescriptions;
import com.assetvisor.marvin.robot.domain.environment.ForGettingEnvironmentFunctions;
import com.assetvisor.marvin.robot.domain.environment.ForPersistingEnvironmentDescriptions;
import com.assetvisor.marvin.robot.domain.environment.Functions;
import com.assetvisor.marvin.robot.domain.jobdescription.ForPersistingRobotDescription;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class RobotLifecycleService {
    @Resource
    private Environment environment;
    @Resource
    private ForInvokingBrain forInvokingBrain;
    @Resource
    private ForPersistingRobotDescription forPersistingRobotDescription;
    @Resource
    private ForPersistingEnvironmentDescriptions forPersistingEnvironmentDescriptions;
    @Resource
    private NoteBook noteBook;
    @Resource
    private ForGettingEnvironmentDescriptions forGettingEnvironmentDescriptions;
    @Resource
    private ForGettingEnvironmentFunctions forGettingEnvironmentFunctions;
    @Resource
    private InteractionService interactionService;

    @PostConstruct
    public void init() {
        if (environment.getProperty("spring.ai.vectorstore.pgvector.remove-existing-vector-store-table", Boolean.class, true)) {
            born();
            wakeUp();
        } else {
            wakeUp();
        }
    }

    public void born() {
        List<EnvironmentDescription> environmentDescriptions = new ArrayList<>(forPersistingEnvironmentDescriptions.load());
        environmentDescriptions.addAll(forGettingEnvironmentDescriptions.getEnvironmentDescriptions());
        forInvokingBrain.born(
            forPersistingRobotDescription.read(),
            environmentDescriptions
        );
    }

    public void wakeUp() {
        Functions functions = new Functions(
            forGettingEnvironmentFunctions,
            noteBook
        );
        forInvokingBrain.wokenUp(
            forPersistingRobotDescription.read(),
            functions.all()
        );
        interactionService.listenTo("You were just woken up, check the current time");
    }
}
