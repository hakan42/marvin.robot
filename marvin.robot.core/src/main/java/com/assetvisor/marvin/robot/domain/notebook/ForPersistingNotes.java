package com.assetvisor.marvin.robot.domain.notebook;

import java.util.List;

public interface ForPersistingNotes {

    void persist(CalendarNote calendarNote);
    List<CalendarNote> all();
    void delete(CalendarNote calendarNote);
}
