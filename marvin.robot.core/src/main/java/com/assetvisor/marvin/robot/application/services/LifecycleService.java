package com.assetvisor.marvin.robot.application.services;

import com.assetvisor.marvin.robot.application.InitialiseUseCase;
import com.assetvisor.marvin.robot.domain.brain.ForForgettingEverything;
import com.assetvisor.marvin.robot.domain.brain.ForInvokingIntelligence;
import com.assetvisor.marvin.robot.domain.brain.Teacher;
import com.assetvisor.marvin.robot.domain.environment.ForGettingEnvironmentDescriptions;
import com.assetvisor.marvin.robot.domain.environment.ForPersistingEnvironmentDescriptions;
import com.assetvisor.marvin.robot.domain.jobdescription.ForPersistingRobotDescription;
import com.assetvisor.marvin.robot.domain.tools.ForGettingEnvironmentTools;
import com.assetvisor.marvin.robot.domain.tools.ForGettingOwnTools;
import com.assetvisor.marvin.robot.domain.tools.ToolCollector;
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
    private ForInvokingIntelligence forInvokingIntelligence;
    @Resource
    private ForPersistingRobotDescription forPersistingRobotDescription;
    @Resource
    private ForGettingEnvironmentTools forGettingEnvironmentTools;
    @Resource
    private ForGettingOwnTools forGettingOwnTools;
    @Resource
    private ForPersistingEnvironmentDescriptions forPersistingEnvironmentDescriptions;
    @Resource
    private ForGettingEnvironmentDescriptions forGettingEnvironmentDescriptions;

    @Override
    public void initialise() {
        LOG.info("Forgetting everything...");
        forForgettingEverything.forgetEverything();

        Teacher teacher = new Teacher(
            forInvokingIntelligence,
            forPersistingEnvironmentDescriptions,
            forGettingEnvironmentDescriptions
        );
        teacher.teach();
    }

    @PostConstruct
    public void wakeUp() {
        ToolCollector toolCollector = new ToolCollector(
            forGettingEnvironmentTools,
            forGettingOwnTools
        );
        forInvokingIntelligence.wakeUp(
            forPersistingRobotDescription.read(),
            toolCollector.all()
        );
    }
}
