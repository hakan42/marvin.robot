package com.assetvisor.marvin.persistence.adapters;

import com.assetvisor.marvin.robot.domain.environment.EnvironmentDescription;
import com.assetvisor.marvin.robot.domain.environment.ForPersistingEnvironmentDescriptions;
import com.assetvisor.marvin.robot.domain.jobdescription.ForPersistingRobotDescription;
import com.assetvisor.marvin.robot.domain.jobdescription.RobotDescription;
import com.assetvisor.marvin.robot.domain.notebook.CalendarNote;
import com.assetvisor.marvin.robot.domain.notebook.ForPersistingNotes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class PersistingLocalFileAdapter implements ForPersistingRobotDescription, ForPersistingEnvironmentDescriptions,
    ForPersistingNotes {

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

    @Override
    public void persist(CalendarNote calendarNote) {
        String userHome = System.getProperty("user.home");
        Path filePath = Paths.get(userHome, "marvin-notes.txt");

        try {
            String note = calendarNote.noteDate() + " - " + calendarNote.note();
            Files.writeString(filePath, note + System.lineSeparator(), StandardOpenOption.APPEND);
            LOG.info("Persisted note to file: " + filePath);
        } catch (IOException e) {
            LOG.error("Error writing note to file: " + e.getMessage());
        }
    }



    @Override
    public List<CalendarNote> all() {
        String userHome = System.getProperty("user.home");
        Path filePath = Paths.get(userHome, "marvin-notes.txt");

        try {
            List<String> lines = Files.readAllLines(filePath);
            LOG.info("Read notes from file: " + filePath);

            return lines.stream()
                .map(line -> line.split(" - "))
                .filter(parts -> parts.length == 2)
                .map(parts -> new CalendarNote(LocalDateTime.parse(parts[0]), parts[1]))
                .collect(Collectors.toList());

        } catch (IOException e) {
            LOG.error("Error reading notes file: " + e.getMessage());
        }
        return List.of();
    }

    @Override
    public void delete(CalendarNote calendarNote) {
        String userHome = System.getProperty("user.home");
        Path filePath = Paths.get(userHome, "marvin-notes.txt");

        try {
            List<String> lines = Files.readAllLines(filePath);

            Optional<String> note = lines.stream()
                .filter(line -> line.startsWith(calendarNote.noteDate().toString()))
                .findFirst();

            note.ifPresent(line -> {
                lines.remove(line);
                try {
                    Files.write(filePath, lines);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                LOG.info("Deleted note: " + line);
            });

        } catch (IOException e) {
            LOG.error("Error deleting note from file: " + e.getMessage());
        }
    }
}
