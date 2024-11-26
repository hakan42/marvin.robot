package com.assetvisor.marvin.environment.openhab.restclient;

import com.assetvisor.marvin.environment.openhab.restclient.OpenhabStateService.Request;
import com.assetvisor.marvin.environment.openhab.restclient.OpenhabStateService.Response;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentFunction;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
@Profile("openhab")
public class OpenhabStateService implements EnvironmentFunction<Request, Response> {

    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private RestClient restClient;

    @Override
    public String name() {
        return "OpenHABStateService";
    }

    @Override
    public String description() {
        return "Get the state of an openHAB item by itemId";
    }

    @Override
    public Class<?> inputType() {
        return Request.class;
    }

    @Override
    public Response apply(Request request) {
        LOG.info(request);
        try {
            String body = restClient.get()
                .uri("items/" + request.itemId + "/state")
                .retrieve()
                .body(String.class);
            Response response = new Response(body);
            LOG.info(response);
            return response;
        } catch (HttpClientErrorException e) {
            return new Response("Error: " + e.getMessage());
        }
    }

    public record Request(String itemId) {}
    public record Response(String json) {}
}
