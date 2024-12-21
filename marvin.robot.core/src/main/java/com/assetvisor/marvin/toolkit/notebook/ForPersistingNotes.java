package com.assetvisor.marvin.toolkit.notebook;

import java.util.Optional;

public interface ForPersistingNotes {
    void persist(CalendarNote calendarNote);
    Optional<CalendarNote> getFirstOverdueAndDelete();
}
