package com.assetvisor.marvin.robot.domain.tools;

import java.util.function.Function;

public interface Tool<I, O> extends Function<I, O> {
    String name();
    String description();
    Class<?> inputType();
}
