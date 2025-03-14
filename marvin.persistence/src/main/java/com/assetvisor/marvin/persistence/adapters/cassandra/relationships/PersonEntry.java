package com.assetvisor.marvin.persistence.adapters.cassandra.relationships;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class PersonEntry {

    @Id
    @PrimaryKeyColumn(name = "id", type = PrimaryKeyType.PARTITIONED)
    private UUID id;

    @Column("person_name")
    private String personName;

    @Column("email")
    private String email;

    @Column("relation")
    private String relationship;

    @Column("github_id")
    private String githubId;

    @Column("google_id")
    private String googleId;

    @Column("password")
    private String password;

    public PersonEntry() {
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

    public Optional<String> getGithubId() {
        return Optional.ofNullable(githubId);
    }

    public void setGithubId(String githubId) {
        this.githubId = githubId;
    }

    public Optional<String> getGoogleId() {
        return Optional.ofNullable(googleId);
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
