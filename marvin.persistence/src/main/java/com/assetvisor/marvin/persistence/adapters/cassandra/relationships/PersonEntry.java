package com.assetvisor.marvin.persistence.adapters.cassandra.relationships;

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class PersonEntry {

    @Id
    @PrimaryKeyColumn(
        name = "id",
        type = PrimaryKeyType.PARTITIONED
    )
    private UUID id;
    @Column(
        "person_name"
    )
    private String personName;
    @Column(
        "email"
    )
    private String email;
    @Column(
        "relation"
    )
    private String relationship;

    public PersonEntry() {
    }

    public PersonEntry(UUID id, String personName, String email, String relationship) {
        this.id = id;
        this.personName = personName;
        this.email = email;
        this.relationship = relationship;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
