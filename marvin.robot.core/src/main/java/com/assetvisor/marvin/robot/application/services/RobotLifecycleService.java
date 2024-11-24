package com.assetvisor.marvin.robot.application.services;

import com.assetvisor.marvin.equipment.notebook.NoteBook;
import com.assetvisor.marvin.robot.domain.brain.ForInvokingBrain;
import com.assetvisor.marvin.robot.domain.environment.ForGettingEnvironmentDescriptions;
import com.assetvisor.marvin.robot.domain.environment.ForGettingEnvironmentFunctions;
import com.assetvisor.marvin.robot.domain.environment.Functions;
import com.assetvisor.marvin.robot.domain.jobdescription.ForPersistingRobotDescription;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RobotLifecycleService {

    @Resource
    private ForInvokingBrain forInvokingBrain;
    @Resource
    private ForPersistingRobotDescription forPersistingRobotDescription;
    @Resource
    private NoteBook noteBook;
    @Resource
    private ForGettingEnvironmentDescriptions forGettingEnvironmentDescriptions;
    @Resource
    private ForGettingEnvironmentFunctions forGettingEnvironmentFunctions;
    @Resource
    private InteractionService interactionService;

    @PostConstruct
    public void born() {
        Functions functions = new Functions(
            forGettingEnvironmentFunctions,
            noteBook
        );
        forInvokingBrain.initialise(
            forPersistingRobotDescription.read(),
            forGettingEnvironmentDescriptions.getEnvironmentDescriptions(),
            functions.all()
        );
        interactionService.listenTo("You are born!");
        interactionService.listenTo("Check the current time");
    }
}
