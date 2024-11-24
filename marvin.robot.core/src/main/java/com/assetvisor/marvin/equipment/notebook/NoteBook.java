package com.assetvisor.marvin.equipment.notebook;

import com.assetvisor.marvin.robot.application.RobotListensUseCase;
import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NoteBook {
    @Resource
    private ForPersistingNotes forPersistingNotes;
    @Resource
    private RobotListensUseCase robotListensUseCase;

    public void takeNote(CalendarNote note) {
        forPersistingNotes.persist(note);
    }

    public void readNoteForNow() {
        forPersistingNotes.all().stream()
            .filter(note -> note.noteDate().isBefore(LocalDateTime.now()))
            .forEach(note -> {
                robotListensUseCase.listenTo("Note read from notebook, act on it: " + note.note());
                forPersistingNotes.delete(note);
            });
    }

    @Scheduled(fixedDelay = 60000)
    public void processNoteBook() {
        readNoteForNow();
    }
}
