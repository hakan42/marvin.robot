package com.assetvisor.marvin.environment.openhab.restclient;

import com.assetvisor.marvin.environment.openhab.restclient.OpenhabCommandService.Request;
import com.assetvisor.marvin.environment.openhab.restclient.OpenhabCommandService.Response;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentFunction;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class OpenhabCommandService implements EnvironmentFunction<Request, Response> {

    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private RestClient restClient;

    @Override
    public String name() {
        return "OpenHABCommandService";
    }

    @Override
    public String description() {
        return "Send a command to an openHAB item by itemId";
    }

    @Override
    public Response apply(Request request) {
        LOG.info("Command: " + request);
        try {
            ResponseEntity<Void> response = restClient.post()
                .uri("items/" + request.itemId)
                .contentType(MediaType.TEXT_PLAIN)
                .body(request.command)
                .retrieve()
                .toBodilessEntity();
            return response.getStatusCode() == HttpStatus.OK ? new Response("OK") : new Response("Error: " + response.getStatusCode());
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return new Response("Error: " + e.getMessage());
        }
    }

    public record Request(String itemId, String command) {}
    public record Response(String json) {}
}
