package com.assetvisor.marvin.robot.domain.ports;

import com.assetvisor.marvin.robot.domain.CalendarNote;
import java.util.List;

public interface ForPersistingNotes {

    void persist(CalendarNote calendarNote);
    List<CalendarNote> all();
    void delete(CalendarNote calendarNote);
}
