package com.assetvisor.marvin.persistence.adapters.cassandra.notebook;

import com.assetvisor.marvin.toolkit.notebook.CalendarNote;
import com.assetvisor.marvin.toolkit.notebook.ForPersistingNotes;
import jakarta.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
//@Profile("cassandra")
public class PersistingNotesCassandraAdapter implements ForPersistingNotes {

    @Resource
    private NotebookRepository notebookRepository;

    @Override
    public void persist(CalendarNote calendarNote) {
        notebookRepository.save(toNoteBookEntry(calendarNote));
    }

    @Override
    public Optional<CalendarNote> getFirstOverdueAndDelete() {
        Optional<NoteBookEntry> first = notebookRepository.findAll().stream()
            .sorted(Comparator.comparing(NoteBookEntry::getNoteDate))
            .filter(note -> note.getNoteDate().isBefore(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
            .findFirst();

        if(first.isPresent()) {
            notebookRepository.deleteById(first.get().getId());
            return Optional.of(toCalendarNote(first.get()));
        } else {
            return Optional.empty();
        }
    }

    private NoteBookEntry toNoteBookEntry(CalendarNote calendarNote) {
        return new NoteBookEntry(
            UUID.randomUUID(),
            Instant.now().atOffset(ZoneOffset.UTC).toInstant(),
            Instant.from(calendarNote.noteDate().atOffset(ZoneOffset.UTC).toInstant()),
            calendarNote.note()
        );
    }

    private CalendarNote toCalendarNote(NoteBookEntry noteBookEntry) {
        return new CalendarNote(
            LocalDateTime.from(noteBookEntry.getNoteDate().atOffset(ZoneOffset.UTC)),
            noteBookEntry.getNote()
        );
    }
}
