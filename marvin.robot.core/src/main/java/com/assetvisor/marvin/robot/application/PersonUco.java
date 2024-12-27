package com.assetvisor.marvin.robot.application;

import com.assetvisor.marvin.robot.domain.relationships.Person.PersonId;

public class PersonUco {
    private final PersonId id;
    private final String name;
    private final String email;

    private PersonUco(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
    }

    public PersonId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public static Builder builder(PersonId id) {
        return new Builder(id);
    }

    public static class Builder {
        private final PersonId id;
        private String name;
        private String email;

        public Builder(PersonId id) {
            this.id = id;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public PersonUco build() {
            return new PersonUco(this);
        }
    }
}
