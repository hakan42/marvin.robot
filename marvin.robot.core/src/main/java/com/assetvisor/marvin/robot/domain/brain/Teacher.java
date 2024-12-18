package com.assetvisor.marvin.robot.domain.brain;

import com.assetvisor.marvin.robot.domain.environment.EnvironmentDescription;
import com.assetvisor.marvin.robot.domain.environment.ForGettingEnvironmentDescriptions;
import com.assetvisor.marvin.robot.domain.environment.ForPersistingEnvironmentDescriptions;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Teacher {

    private final Log LOG = LogFactory.getLog(getClass());

    private final ForInvokingBrain forInvokingBrain;
    private final ForForgettingEverything forForgettingEverything;
    private final ForPersistingEnvironmentDescriptions forPersistingEnvironmentDescriptions;
    private final ForGettingEnvironmentDescriptions forGettingEnvironmentDescriptions;

    public Teacher(
        ForInvokingBrain forInvokingBrain, ForForgettingEverything forForgettingEverything,
        ForPersistingEnvironmentDescriptions forPersistingEnvironmentDescriptions,
        ForGettingEnvironmentDescriptions forGettingEnvironmentDescriptions
    ) {
        this.forInvokingBrain = forInvokingBrain;
        this.forForgettingEverything = forForgettingEverything;
        this.forPersistingEnvironmentDescriptions = forPersistingEnvironmentDescriptions;
        this.forGettingEnvironmentDescriptions = forGettingEnvironmentDescriptions;
    }

    public void teach() {
        LOG.info("Forgetting everything...");
        forForgettingEverything.forgetEverything();
        LOG.info("Teaching brain about environment...");
        List<EnvironmentDescription> environmentDescriptions = new ArrayList<>(forPersistingEnvironmentDescriptions.load());
        environmentDescriptions.addAll(forGettingEnvironmentDescriptions.getEnvironmentDescriptions());
        forInvokingBrain.teach(
            environmentDescriptions
        );
        LOG.info("Teaching done.");
    }
}
