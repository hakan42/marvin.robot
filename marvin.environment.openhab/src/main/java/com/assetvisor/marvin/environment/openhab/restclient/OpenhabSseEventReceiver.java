package com.assetvisor.marvin.environment.openhab.restclient;

import com.assetvisor.marvin.robot.application.RobotWatchesUseCase;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
        robotWatchesUseCase.observe(event);
    }
}
