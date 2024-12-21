package com.assetvisor.marvin.toolkit.notebook;

import com.assetvisor.marvin.robot.domain.tools.Tool;
import com.assetvisor.marvin.toolkit.notebook.WriteInNoteBookTool.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WriteInNoteBookTool implements Tool<CalendarNote, Response> {

    private final Log LOG = LogFactory.getLog(getClass());
    private final NoteBook noteBook;

    public WriteInNoteBookTool(NoteBook noteBook) {
        this.noteBook = noteBook;
    }

    @Override
    public String name() {
        return "WriteInNotebook";
    }

    @Override
    public String description() {
        return """
            This tool is used for writing reminders to yourself in your notebook.
            Check that you have the correct time before writing a reminder.
            The notebook will automatically remind you at the time you specify.
            """;
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
