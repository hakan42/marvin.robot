package com.assetvisor.marvin.equipment.notebook;

import com.assetvisor.marvin.robot.domain.environment.EnvironmentFunction;
import com.assetvisor.marvin.equipment.notebook.WriteInNoteBook.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WriteInNoteBook implements EnvironmentFunction<CalendarNote, Response> {

    private final Log LOG = LogFactory.getLog(getClass());
    private final NoteBook noteBook;

    public WriteInNoteBook(NoteBook noteBook) {
        this.noteBook = noteBook;
    }

    @Override
    public String name() {
        return "WriteInNotebook";
    }

    @Override
    public String description() {
        return "This function is used for writing notes in your notebook with date and text so that it can be read to you and acted on later";
    }

    @Override
    public Class<?> inputType() {
        return CalendarNote.class;
    }

    @Override
    public Response apply(CalendarNote calendarNote) {
        noteBook.takeNote(calendarNote);
        LOG.info("Written in notebook: " + calendarNote);
        return new Response(1);
    }

    public record Response(Integer noteCount) {}

}
