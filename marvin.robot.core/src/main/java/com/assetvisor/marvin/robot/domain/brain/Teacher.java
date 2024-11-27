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
    private final ForPersistingEnvironmentDescriptions forPersistingEnvironmentDescriptions;
    private final ForGettingEnvironmentDescriptions forGettingEnvironmentDescriptions;

    public Teacher(
        ForInvokingBrain forInvokingBrain,
        ForPersistingEnvironmentDescriptions forPersistingEnvironmentDescriptions,
        ForGettingEnvironmentDescriptions forGettingEnvironmentDescriptions
    ) {
        this.forInvokingBrain = forInvokingBrain;
        this.forPersistingEnvironmentDescriptions = forPersistingEnvironmentDescriptions;
        this.forGettingEnvironmentDescriptions = forGettingEnvironmentDescriptions;
    }

    public void teach() {
        LOG.info("Teaching brain about environment...");
        List<EnvironmentDescription> environmentDescriptions = new ArrayList<>(forPersistingEnvironmentDescriptions.load());
        environmentDescriptions.addAll(forGettingEnvironmentDescriptions.getEnvironmentDescriptions());
        forInvokingBrain.teach(
            environmentDescriptions
        );
        LOG.info("Teaching done.");
    }
}
