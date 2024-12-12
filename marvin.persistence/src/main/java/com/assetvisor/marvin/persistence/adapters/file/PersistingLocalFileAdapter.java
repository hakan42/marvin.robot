package com.assetvisor.marvin.persistence.adapters.file;

import com.assetvisor.marvin.robot.domain.environment.EnvironmentDescription;
import com.assetvisor.marvin.robot.domain.environment.ForPersistingEnvironmentDescriptions;
import com.assetvisor.marvin.robot.domain.jobdescription.ForPersistingRobotDescription;
import com.assetvisor.marvin.robot.domain.jobdescription.RobotDescription;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class PersistingLocalFileAdapter implements ForPersistingRobotDescription, ForPersistingEnvironmentDescriptions {

    private final Log LOG = LogFactory.getLog(getClass());

    @Override
    public RobotDescription read() {
        String userHome = System.getProperty("user.home");
        Path filePath = Paths.get(userHome, "marvin-robot.txt");

        try {
            String content = Files.readString(filePath);
            LOG.info("Read robot description from file: " + filePath);
            return new RobotDescription(content);
        } catch (IOException e) {
            LOG.error("Error reading file: " + e.getMessage());
        }
        return new RobotDescription("");
    }

    @Override
    public List<EnvironmentDescription> load() {
        String userHome = System.getProperty("user.home");
        Path filePath = Paths.get(userHome, "marvin-environments.txt");

        try {
            List<String> lines = Files.readAllLines(filePath);
            LOG.info("Read environment descriptions from file: " + filePath);

            return lines.stream()
                .map(line -> {
                    try {
                        return new EnvironmentDescription(line);
                    } catch (Exception e) {
                        LOG.error("Error parsing line: " + line + " - " + e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        } catch (IOException e) {
            LOG.error("Error reading environments file: " + e.getMessage());
        }

        return List.of();
    }
}
