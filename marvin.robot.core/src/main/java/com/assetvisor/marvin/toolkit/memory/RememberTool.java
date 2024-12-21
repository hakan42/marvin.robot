package com.assetvisor.marvin.toolkit.memory;

import com.assetvisor.marvin.toolkit.memory.RememberTool.Thought;
import com.assetvisor.marvin.robot.domain.tools.Tool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RememberTool implements Tool<Thought, Void> {

    private static final Log LOG = LogFactory.getLog(RememberTool.class);

    private final ForRemembering forRemembering;

    public RememberTool(ForRemembering forRemembering) {
        this.forRemembering = forRemembering;
    }

    @Override
    public String name() {
        return "Remember";
    }

    @Override
    public String description() {
        return "This tool is used to remember things.";
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
