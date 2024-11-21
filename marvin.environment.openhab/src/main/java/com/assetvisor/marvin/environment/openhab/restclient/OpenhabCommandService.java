package com.assetvisor.marvin.environment.openhab.restclient;

import com.assetvisor.marvin.environment.openhab.restclient.OpenhabCommandService.Command;
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
public class OpenhabCommandService implements EnvironmentFunction<Command, Response> {

    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private RestClient restClient;
    @Resource
    private SentCommands sentCommands;

    @Override
    public String name() {
        return "OpenHABCommandService";
    }

    @Override
    public String description() {
        return "Send a command to an openHAB item by itemId";
    }

    @Override
    public Response apply(Command command) {
        LOG.info("Command: " + command);
        try {
            ResponseEntity<Void> response = restClient.post()
                .uri("items/" + command.itemId)
                .contentType(MediaType.TEXT_PLAIN)
                .body(command.command)
                .retrieve()
                .toBodilessEntity();
            sentCommands.markAsSent(command);
            return response.getStatusCode() == HttpStatus.OK ? new Response("OK") : new Response("Error: " + response.getStatusCode());
        } catch (HttpClientErrorException e) {
            LOG.error(e.getMessage(), e);
            return new Response("Error: " + e.getMessage());
        }
    }

    public record Command(String itemId, String command) {}
    public record Response(String json) {}
}
