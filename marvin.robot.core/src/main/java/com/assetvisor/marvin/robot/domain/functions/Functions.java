package com.assetvisor.marvin.robot.domain.functions;

import com.assetvisor.marvin.robot.domain.EnvironmentFunction;
import com.assetvisor.marvin.robot.domain.ports.ForGettingEnvironmentFunctions;
import java.util.List;

public class Functions {

    private final ForGettingEnvironmentFunctions forGettingEnvironmentFunctions;

    public Functions(ForGettingEnvironmentFunctions forGettingEnvironmentFunctions) {
        this.forGettingEnvironmentFunctions = forGettingEnvironmentFunctions;
    }

    public List<EnvironmentFunction<?,?>> all() {
        List<EnvironmentFunction<?, ?>> environmentFunctions = forGettingEnvironmentFunctions.getEnvironmentFunctions();
        environmentFunctions.addAll(builtInFunctions());
        return environmentFunctions;
    }

    private List<EnvironmentFunction<?,?>> builtInFunctions() {
        return List.of(
            new Clock()
        );
    }
}
