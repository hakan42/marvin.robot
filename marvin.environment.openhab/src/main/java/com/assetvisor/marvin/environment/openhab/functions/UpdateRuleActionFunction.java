package com.assetvisor.marvin.environment.openhab.functions;

import com.assetvisor.marvin.environment.openhab.functions.UpdateRuleActionFunction.Command;
import com.assetvisor.marvin.environment.openhab.functions.UpdateRuleActionFunction.Response;
import com.assetvisor.marvin.environment.openhab.restclient.SentCommands;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentFunction;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@Profile("openhab")
public class UpdateRuleActionFunction implements EnvironmentFunction<Command, Response> {

    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private RestClient openhabRestClient;
    @Resource
    private SentCommands sentCommands;

    @Override
    public String name() {
        return "UpdateRuleActionFunction";
    }

    @Override
    public String description() {
        return "Update an openHAB rule script by ruleUid";
    }

    @Override
    public Class<?> inputType() {
        return Command.class;
    }

    @Override
    public Response apply(Command command) {
        LOG.info(command);
        Map<String, Object> rule = getRule(command.ruleId());
        Map<String, Object> action = ((List<Map<String, Object>>) rule.get("actions")).get(0);
        Map<String, Object> configuration = ((Map<String, Object>) action.get("configuration"));
        configuration.put("script", command.script());
        updateRule(command.ruleId(), rule);
        return new Response("Done");
    }

    public record Command(String ruleId, String script) {}
    public record Response(String json) {}

    private Map<String, Object> getRule(String ruleId) {
        try {
            Map<String, Object> body = openhabRestClient.get()
                .uri("rules/" + ruleId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Map.class);
            return body;
        } catch (HttpClientErrorException e) {
            throw new RestClientException(e.getMessage(), e);
        }
    }

    private void updateRule(String ruleId, Map<String, Object> rule) {
        try {
            openhabRestClient.put()
                .uri("rules/" + ruleId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(rule)
                .retrieve()
                .toBodilessEntity();
        } catch (HttpClientErrorException e) {
            throw new RestClientException(e.getMessage(), e);
        }
    }
}
