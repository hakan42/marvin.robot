package com.assetvisor.marvin.persistence.adapters.cassandra.relationships;

public class PersonExistsException extends RuntimeException {

    public PersonExistsException(String message) {
        super(message);
    }
}
