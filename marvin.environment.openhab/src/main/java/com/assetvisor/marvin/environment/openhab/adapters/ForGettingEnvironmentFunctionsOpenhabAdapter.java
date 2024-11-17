package com.assetvisor.marvin.environment.openhab.adapters;

import com.assetvisor.marvin.environment.openhab.restclient.OpenhabCommandService;
import com.assetvisor.marvin.environment.openhab.restclient.OpenhabStateService;
import com.assetvisor.marvin.robot.domain.EnvironmentFunction;
import com.assetvisor.marvin.robot.domain.ports.ForGettingEnvironmentFunctions;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ForGettingEnvironmentFunctionsOpenhabAdapter implements ForGettingEnvironmentFunctions {

    @Resource
    private OpenhabStateService openhabStateService;
    @Resource
    private OpenhabCommandService openhabCommandService;

    @Override
    public List<EnvironmentFunction<?, ?>> getEnvironmentFunctions() {
        return new ArrayList<>(List.of(
            openhabStateService,
            openhabCommandService
        ));
    }
}
