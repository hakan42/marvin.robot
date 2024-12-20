package com.assetvisor.marvin.equipment.notebook;

import com.assetvisor.marvin.robot.application.ListenUseCase;
import com.assetvisor.marvin.robot.domain.communication.TextMessage;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NoteBook {
    @Resource
    private ForPersistingNotes forPersistingNotes;
    @Resource
    private ListenUseCase listenUseCase;

    public void takeNote(CalendarNote note) {
        forPersistingNotes.persist(note);
    }

    public void readNoteForNow() {
        forPersistingNotes.getFirstOverdueAndDelete()
            .ifPresent(note -> {
                listenUseCase.listenTo(new TextMessage("Notebook", "notebook", note.note()));
            });
    }

    @Scheduled(fixedDelay = 60000)
    public void processNoteBook() {
        readNoteForNow();
    }
}
