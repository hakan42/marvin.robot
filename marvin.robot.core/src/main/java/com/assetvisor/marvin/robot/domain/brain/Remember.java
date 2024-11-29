package com.assetvisor.marvin.robot.domain.brain;

import com.assetvisor.marvin.robot.domain.brain.Remember.Thought;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentFunction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Remember implements EnvironmentFunction<Thought, Void> {

    private static final Log LOG = LogFactory.getLog(Remember.class);

    private final ForRemembering forRemembering;

    public Remember(ForRemembering forRemembering) {
        this.forRemembering = forRemembering;
    }

    @Override
    public String name() {
        return "Remember";
    }

    @Override
    public String description() {
        return "This function is used for remembering thoughts that will be added to your context.";
    }

    @Override
    public Class<?> inputType() {
        return Thought.class;
    }

    @Override
    public Void apply(Thought thought) {
        LOG.info("Remembering: " + thought);
        forRemembering.remember(thought.input);
        return null;
    }

    public record Thought(String input) {}
}
