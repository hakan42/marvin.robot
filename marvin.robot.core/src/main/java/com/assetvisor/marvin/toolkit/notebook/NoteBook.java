package com.assetvisor.marvin.toolkit.notebook;

import com.assetvisor.marvin.robot.application.SomethingWasTextedUseCase;
import com.assetvisor.marvin.robot.domain.communication.TextMessage;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NoteBook {
    @Resource
    private ForPersistingNotes forPersistingNotes;
    @Resource
    private SomethingWasTextedUseCase somethingWasTextedUseCase;

    public void takeNote(CalendarNote note) {
        forPersistingNotes.persist(note);
    }

    public void readNoteForNow() {
        forPersistingNotes.getFirstOverdueAndDelete()
            .ifPresent(note -> {
                somethingWasTextedUseCase.read(
                    new TextMessage(
                        "Notebook",
                        "notebook",
                        "Note from the notebook to the user: " + note.note()
                    ),
                    false
                );
            });
    }

    @Scheduled(fixedDelay = 60000)
    public void processNoteBook() {
        readNoteForNow();
    }
}
