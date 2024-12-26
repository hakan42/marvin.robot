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

    private final ForInvokingIntelligence forInvokingIntelligence;
    private final ForPersistingEnvironmentDescriptions forPersistingEnvironmentDescriptions;
    private final ForGettingEnvironmentDescriptions forGettingEnvironmentDescriptions;

    public Teacher(
        ForInvokingIntelligence forInvokingIntelligence,
        ForPersistingEnvironmentDescriptions forPersistingEnvironmentDescriptions,
        ForGettingEnvironmentDescriptions forGettingEnvironmentDescriptions
    ) {
        this.forInvokingIntelligence = forInvokingIntelligence;
        this.forPersistingEnvironmentDescriptions = forPersistingEnvironmentDescriptions;
        this.forGettingEnvironmentDescriptions = forGettingEnvironmentDescriptions;
    }

    public void teach() {
        LOG.info("Teaching brain about environment...");
        List<EnvironmentDescription> environmentDescriptions = new ArrayList<>(forPersistingEnvironmentDescriptions.all());
        environmentDescriptions.addAll(forGettingEnvironmentDescriptions.getEnvironmentDescriptions());
        forInvokingIntelligence.teach(
            environmentDescriptions
        );
        LOG.info("Teaching done.");
    }
}
