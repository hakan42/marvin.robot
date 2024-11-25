package com.assetvisor.marvin.robot.domain.brain;

public class AsleepException extends RuntimeException {

    public AsleepException(String message) {
        super(message);
    }

    public AsleepException(String message, Throwable cause) {
        super(message, cause);
    }

}
