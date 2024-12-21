package com.assetvisor.marvin.environment.openhab.functions;

import com.assetvisor.marvin.environment.openhab.functions.SendCommandFunction.Command;
import com.assetvisor.marvin.environment.openhab.functions.SendCommandFunction.Response;
import com.assetvisor.marvin.environment.openhab.restclient.SentCommands;
import com.assetvisor.marvin.robot.domain.tools.Tool;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
@Profile("openhab")
public class SendCommandFunction implements Tool<Command, Response> {

    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private RestClient openhabRestClient;
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
    public Class<?> inputType() {
        return Command.class;
    }

    @Override
    public Response apply(Command command) {
        LOG.info(command);
        try {
            ResponseEntity<Void> responseEntity = openhabRestClient.post()
                .uri("items/" + command.itemId)
                .contentType(MediaType.TEXT_PLAIN)
                .body(command.command)
                .retrieve()
                .toBodilessEntity();
            sentCommands.markAsSent(command);
            Response response = responseEntity.getStatusCode() == HttpStatus.OK ? new Response("OK") : new Response("Error: " + responseEntity.getStatusCode());
            LOG.info(response);
            return response;
        } catch (HttpClientErrorException e) {
            LOG.error(e.getMessage(), e);
            return new Response("Error: " + e.getMessage());
        }
    }

    public record Command(String itemId, String command) {}
    public record Response(String json) {}
}
