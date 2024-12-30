package com.assetvisor.marvin.environment.openhab.functions;

import com.assetvisor.marvin.environment.openhab.functions.GetItemStateFunction.Request;
import com.assetvisor.marvin.environment.openhab.functions.GetItemStateFunction.Response;
import com.assetvisor.marvin.robot.domain.tools.Tool;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
@Profile("environment-openhab")
public class GetItemStateFunction implements Tool<Request, Response> {

    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private RestClient openhabRestClient;

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
            String body = openhabRestClient.get()
                .uri("items/" + request.itemId + "/state")
                .accept(MediaType.TEXT_PLAIN)
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
