package com.assetvisor.marvin.persistence.adapters.cassandra;

import java.time.Instant;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class NoteBookEntry {

    @Id
    @PrimaryKeyColumn(
        name = "id",
        type = PrimaryKeyType.PARTITIONED
    )
    private UUID id;
    @Column(
        "created_date"
    )
    private Instant createdDate;
    @Column(
        "note_date"
    )
    private Instant noteDate;
    @Column(
        "note"
    )
    private String note;

    public NoteBookEntry() {
    }

    public NoteBookEntry(UUID id, Instant createdDate, Instant noteDate, String note) {
        this.id = id;
        this.createdDate = createdDate;
        this.noteDate = noteDate;
        this.note = note;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(Instant noteDate) {
        this.noteDate = noteDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
