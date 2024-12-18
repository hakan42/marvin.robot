package com.assetvisor.marvin.robot.application.services;

import com.assetvisor.marvin.equipment.internet.ForSearchingInternet;
import com.assetvisor.marvin.equipment.notebook.NoteBook;
import com.assetvisor.marvin.robot.application.InitialiseUseCase;
import com.assetvisor.marvin.robot.domain.brain.ForForgettingEverything;
import com.assetvisor.marvin.robot.domain.brain.ForInvokingBrain;
import com.assetvisor.marvin.robot.domain.brain.ForRemembering;
import com.assetvisor.marvin.robot.domain.brain.Teacher;
import com.assetvisor.marvin.robot.domain.environment.ForGettingEnvironmentDescriptions;
import com.assetvisor.marvin.robot.domain.environment.ForGettingEnvironmentFunctions;
import com.assetvisor.marvin.robot.domain.environment.ForPersistingEnvironmentDescriptions;
import com.assetvisor.marvin.robot.domain.environment.Functions;
import com.assetvisor.marvin.robot.domain.jobdescription.ForPersistingRobotDescription;
import com.assetvisor.marvin.robot.domain.relationships.ForAddingPerson;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class LifecycleService implements InitialiseUseCase {
    private static final Log LOG = LogFactory.getLog(LifecycleService.class);

    @Resource
    private ForForgettingEverything forForgettingEverything;
    @Resource
    private ForInvokingBrain forInvokingBrain;
    @Resource
    private ForRemembering forRemembering;
    @Resource
    private ForSearchingInternet forSearchingInternet;
    @Resource
    private ForAddingPerson forAddingPerson;
    @Resource
    private ForPersistingRobotDescription forPersistingRobotDescription;
    @Resource
    private NoteBook noteBook;
    @Resource
    private ForGettingEnvironmentFunctions forGettingEnvironmentFunctions;
    @Resource
    private ForPersistingEnvironmentDescriptions forPersistingEnvironmentDescriptions;
    @Resource
    private ForGettingEnvironmentDescriptions forGettingEnvironmentDescriptions;

    @Override
    public void initialise() {
        LOG.info("Forgetting everything...");
        forForgettingEverything.forgetEverything();

        Teacher teacher = new Teacher(
            forInvokingBrain,
            forPersistingEnvironmentDescriptions,
            forGettingEnvironmentDescriptions
        );
        teacher.teach();
    }

    @PostConstruct
    public void wakeUp() {
        Functions functions = new Functions(
            forGettingEnvironmentFunctions,
            noteBook,
            forRemembering,
            forSearchingInternet,
            forAddingPerson
        );
        forInvokingBrain.wakeUp(
            forPersistingRobotDescription.read(),
            functions.all()
        );
    }
}
