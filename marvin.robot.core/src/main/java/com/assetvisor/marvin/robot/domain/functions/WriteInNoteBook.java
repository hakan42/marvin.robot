package com.assetvisor.marvin.robot.domain.functions;

import com.assetvisor.marvin.robot.domain.CalendarNote;
import com.assetvisor.marvin.robot.domain.EnvironmentFunction;
import com.assetvisor.marvin.robot.domain.NoteBook;
import com.assetvisor.marvin.robot.domain.functions.WriteInNoteBook.Request;
import com.assetvisor.marvin.robot.domain.functions.WriteInNoteBook.Response;
import java.time.LocalDateTime;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WriteInNoteBook implements EnvironmentFunction<Request, Response> {

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
    public Response apply(Request request) {
        noteBook.takeNote(new CalendarNote(
            request.noteDate(),
            request.note()
        ));
        LOG.info("Written in notebook: " + request);
        return new Response(1);
    }

    public record Request(LocalDateTime noteDate, String note) {}
    public record Response(Integer noteCount) {}

}
