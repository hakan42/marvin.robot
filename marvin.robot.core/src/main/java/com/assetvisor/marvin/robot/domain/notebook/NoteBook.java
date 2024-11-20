package com.assetvisor.marvin.robot.domain.notebook;

import com.assetvisor.marvin.robot.domain.brain.ForInvokingBrain;
import java.time.LocalDateTime;

public class NoteBook {
    private final ForPersistingNotes forPersistingNotes;
    private final ForInvokingBrain forInvokingBrain;

    public NoteBook(ForPersistingNotes forPersistingNotes, ForInvokingBrain forInvokingBrain) {
        this.forPersistingNotes = forPersistingNotes;
        this.forInvokingBrain = forInvokingBrain;
    }

    public void takeNote(CalendarNote note) {
        forPersistingNotes.persist(note);
    }

    public void readNoteForNow() {
        forPersistingNotes.all().stream()
            .filter(note -> note.noteDate().isBefore(LocalDateTime.now()))
            .forEach(note -> {
                forInvokingBrain.invoke("Note read from notebook, act on it: " + note.note(), true);
                forPersistingNotes.delete(note);
            });
    }
}
