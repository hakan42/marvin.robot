package com.assetvisor.marvin.robot.domain;

import java.util.function.Function;

public interface EnvironmentFunction<I, O> extends Function<I, O> {
    String name();
    String description();
}
