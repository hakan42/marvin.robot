package com.assetvisor.marvin.environment.openhab.functions;

import com.assetvisor.marvin.environment.openhab.functions.GetRuleActionsFunction.Request;
import com.assetvisor.marvin.robot.domain.tools.Tool;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
@Profile("environment-openhab")
public class GetRuleActionsFunction implements Tool<Request, List<Map<String, Object>>> {

    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private RestClient openhabRestClient;

    @Override
    public String name() {
        return "GetRuleActionsFunction";
    }

    @Override
    public String description() {
        return "Get the actions of an openHAB rule by ruleUid";
    }

    @Override
    public Class<?> inputType() {
        return Request.class;
    }

    @Override
    public List<Map<String, Object>> apply(Request request) {
        LOG.info(request);
        try {
            return openhabRestClient.get()
                .uri("rules/{ruleId}/actions", request.ruleId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(LIST_OF_MAP_TYPE);
        } catch (HttpClientErrorException e) {
            return List.of();
        }
    }

    public record Request(String ruleId) {}
    public record Response(String code) {}

    private static final ParameterizedTypeReference<List<Map<String, Object>>> LIST_OF_MAP_TYPE =
        new ParameterizedTypeReference<>() {};
}
