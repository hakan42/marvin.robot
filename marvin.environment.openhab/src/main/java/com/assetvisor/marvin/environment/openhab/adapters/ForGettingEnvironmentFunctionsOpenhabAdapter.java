package com.assetvisor.marvin.environment.openhab.adapters;

import com.assetvisor.marvin.environment.openhab.functions.GetRuleActionsFunction;
import com.assetvisor.marvin.environment.openhab.functions.SendCommandFunction;
import com.assetvisor.marvin.environment.openhab.functions.GetItemStateFunction;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentFunction;
import com.assetvisor.marvin.robot.domain.environment.ForGettingEnvironmentFunctions;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("openhab")
public class ForGettingEnvironmentFunctionsOpenhabAdapter implements ForGettingEnvironmentFunctions {

    @Resource
    private GetItemStateFunction getItemStateFunction;
    @Resource
    private SendCommandFunction sendCommandFunction;
    @Resource
    private GetRuleActionsFunction getRuleActionsFunction;

    @Override
    public List<EnvironmentFunction<?, ?>> getEnvironmentFunctions() {
        return new ArrayList<>(List.of(
            getItemStateFunction,
            sendCommandFunction,
            getRuleActionsFunction
        ));
    }
}
