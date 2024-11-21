package com.assetvisor.marvin.environment.openhab.restclient;

import com.assetvisor.marvin.robot.application.RobotWatchesUseCase;
import com.assetvisor.marvin.robot.domain.environment.Observation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class OpenhabSseEventReceiver {

    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private RobotWatchesUseCase robotWatchesUseCase;
    @Resource
    private WebClient webClient;
    @Resource
    private OpenhabAiItemsService openhabAiItemsService;
    @Resource
    private SentCommands sentCommands;

    @PostConstruct
    public void listenForSseEvents() {
        openhabAiItemsService.asIds().forEach(this::connect);
    }

    private void connect(String itemId) {
        // Set up the SSE stream and process each event
        @SuppressWarnings("rawtypes") Flux<Map> eventStream = webClient.get()
            .uri("events?topics=openhab/items/" + itemId + "/statechanged") // Replace with the correct SSE endpoint
            .accept(MediaType.TEXT_EVENT_STREAM)
            .retrieve()
            .bodyToFlux(Map.class);

        eventStream.subscribe(
            this::handleEvent,       // Process each event
            error -> LOG.error("Error: " + error), // Handle errors
            () -> LOG.info("Stream completed")    // Handle stream completion
        );
        LOG.info("Connected to " + itemId);
    }

    private void handleEvent(Map<?,?> event) {
        // Custom logic to process each event
        if(event.get("type").equals("ALIVE")) return;
        if(event.get("type").equals("ItemStateChangedEvent")) {
            Observation observation = toObservation(event);
            sentCommands.relatedCommand(observation)
                .ifPresentOrElse(
                    command -> {
                        LOG.info("Command found for observation: " + observation);
                        sentCommands.markAsNotRelevant(command);
                    },
                    () -> {
                        LOG.info("No command found for observation: " + observation);
                        robotWatchesUseCase.observe(observation);
                    }
                );
        }
    }

    private Observation toObservation(Map<?,?> event) {
        //Split the topic into parts and build an observation object
        String[] topicParts = ((String) event.get("topic")).split("/");
        String itemId = topicParts[2];
        ObjectMapper objectMapper = new ObjectMapper();
        // Convert JSON string to a Map
        Map<String, Object> payloadMap;
        try {
            payloadMap = objectMapper.readValue((String) event.get("payload"),
                new TypeReference<>() {
                });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String value = (String) payloadMap.get("value");
        String oldValue = (String) payloadMap.get("oldValue");
        return new Observation(
            itemId,
            value,
            oldValue,
            "state changed "
        );
    }
}
