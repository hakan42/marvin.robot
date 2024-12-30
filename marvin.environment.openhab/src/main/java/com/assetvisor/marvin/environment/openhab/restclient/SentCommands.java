package com.assetvisor.marvin.environment.openhab.restclient;

import com.assetvisor.marvin.environment.openhab.functions.SendCommandFunction.Command;
import com.assetvisor.marvin.robot.domain.environment.Observation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("environment-openhab")
public class SentCommands {
    private final List<Command> commandList = new ArrayList<>();

    public void markAsSent(Command command) {
        commandList.add(command);
    }

    public void markAsNotRelevant(Command command) {
        commandList.remove(command);
    }

    public Optional<Command> relatedCommand(Observation observation) {
        return commandList.stream()
            .filter(command -> match(command, observation))
            .findFirst();
    }

    private boolean match(Command command, Observation observation) {
        return command.itemId().equals(observation.itemId());
    }
}
