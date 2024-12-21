package com.assetvisor.marvin.robot.domain.tools;

import java.util.ArrayList;
import java.util.List;

public class ToolCollector {

    private final ForGettingEnvironmentTools forGettingEnvironmentTools;
    private final ForGettingOwnTools forGettingOwnTools;

    public ToolCollector(
        ForGettingEnvironmentTools forGettingEnvironmentTools,
        ForGettingOwnTools forGettingOwnTools
    ) {
        this.forGettingEnvironmentTools = forGettingEnvironmentTools;
        this.forGettingOwnTools = forGettingOwnTools;
    }

    public List<Tool<?,?>> all() {
        List<Tool<?, ?>> allTools = new ArrayList<>();
        allTools.addAll(forGettingEnvironmentTools.getEnvironmentTools());
        allTools.addAll(forGettingOwnTools.getOwnTools());
        return allTools;
    }
}
