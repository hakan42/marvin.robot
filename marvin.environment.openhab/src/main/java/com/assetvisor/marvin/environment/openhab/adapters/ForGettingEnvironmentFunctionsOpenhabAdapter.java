package com.assetvisor.marvin.environment.openhab.adapters;

import com.assetvisor.marvin.environment.openhab.functions.GetRuleActionsFunction;
import com.assetvisor.marvin.environment.openhab.functions.SendCommandFunction;
import com.assetvisor.marvin.environment.openhab.functions.GetItemStateFunction;
import com.assetvisor.marvin.environment.openhab.functions.UpdateRuleActionFunction;
import com.assetvisor.marvin.robot.domain.tools.Tool;
import com.assetvisor.marvin.robot.domain.tools.ForGettingEnvironmentTools;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("environment-openhab")
public class ForGettingEnvironmentFunctionsOpenhabAdapter implements ForGettingEnvironmentTools {

    @Resource
    private GetItemStateFunction getItemStateFunction;
    @Resource
    private SendCommandFunction sendCommandFunction;
    @Resource
    private GetRuleActionsFunction getRuleActionsFunction;
    @Resource
    private UpdateRuleActionFunction updateRuleActionFunction;

    @Override
    public List<Tool<?, ?>> getEnvironmentTools() {
        return new ArrayList<>(List.of(
            getItemStateFunction,
            sendCommandFunction,
            getRuleActionsFunction,
            updateRuleActionFunction
        ));
    }
}
