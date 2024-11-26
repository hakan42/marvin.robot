package com.assetvisor.marvin.environment.openhab.restclient;

import com.assetvisor.marvin.robot.application.ObserveUseCase;
import com.assetvisor.marvin.robot.domain.environment.Observation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Profile("openhab")
public class OpenhabSseEventReceiver {

    private static final Log LOG = LogFactory.getLog(OpenhabSseEventReceiver.class);
    private static final int DEBOUNCE_TIME_MS = 2000; // Debounce time in milliseconds

    @Resource
    private ObserveUseCase observeUseCase;
    @Resource
    private WebClient webClient;
    @Resource
    private OpenhabEventPublishingItemsService openhabEventPublishingItemsService;
    @Resource
    private SentCommands sentCommands;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Map<String, ScheduledFuture<?>> debounceTasks = new ConcurrentHashMap<>();
    private final Map<String, String> initialOldValues = new ConcurrentHashMap<>(); // Cache for old values

    @PostConstruct
    public void listenForSseEvents() {
        openhabEventPublishingItemsService.asIds().forEach(this::connect);
    }

    private void connect(String itemId) {
        // Set up the SSE stream and process each event
        @SuppressWarnings("rawtypes") Flux<Map> eventStream = webClient.get()
            .uri("events?topics=openhab/items/" + itemId + "/statechanged") // Replace with the correct SSE endpoint
            .accept(MediaType.TEXT_EVENT_STREAM)
            .retrieve()
            .bodyToFlux(Map.class);

        eventStream.subscribe(
            event -> handleEvent(itemId, event), // Process each event
            error -> LOG.error("Error: " + error), // Handle errors
            () -> LOG.info("Stream completed for item: " + itemId) // Handle stream completion
        );
        LOG.info("Connected to " + itemId);
    }

    private void handleEvent(String itemId, Map<?, ?> event) {
        if (!"ItemStateChangedEvent".equals(event.get("type"))) {
            return; // Only process ItemStateChangedEvent events
        }

        String currentOldValue = extractOldValue(event);

        // Cache the initial oldValue if not already cached for this itemId
        initialOldValues.computeIfAbsent(itemId, key -> currentOldValue);

        Runnable debounceTask = () -> {
            Observation observation = toObservation(event, initialOldValues.get(itemId));

            sentCommands.relatedCommand(observation)
                .ifPresentOrElse(
                    command -> {
                        LOG.debug("Command found for observation: " + observation);
                        sentCommands.markAsNotRelevant(command);
                    },
                    () -> {
                        LOG.debug("No command found for observation: " + observation);
                        observeUseCase.observe(observation);
                    }
                );

            // Clean up the cache and task map
            initialOldValues.remove(itemId);
            debounceTasks.remove(itemId);
        };

        // Cancel any existing task for this itemId
        if (debounceTasks.containsKey(itemId)) {
            debounceTasks.get(itemId).cancel(false); // Cancel without interrupting
        }

        // Schedule the new debounce task and store its Future
        ScheduledFuture<?> futureTask = scheduler.schedule(debounceTask, DEBOUNCE_TIME_MS, TimeUnit.MILLISECONDS);
        debounceTasks.put(itemId, futureTask);
    }

    private String extractOldValue(Map<?, ?> event) {
        // Extract the oldValue from the payload
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> payload = objectMapper.readValue((String) event.get("payload"), new TypeReference<>() {});
            return (String) payload.get("oldValue");
        } catch (JsonProcessingException e) {
            LOG.error("Failed to parse event payload", e);
            return null;
        }
    }

    private Observation toObservation(Map<?, ?> event, String initialOldValue) {
        String[] topicParts = ((String) event.get("topic")).split("/");
        String itemId = topicParts[2];
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> payloadMap;
        try {
            payloadMap = objectMapper.readValue((String) event.get("payload"), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String value = (String) payloadMap.get("value");
        String oldValue = initialOldValue == null ? (String) payloadMap.get("oldValue") : initialOldValue;
        return new Observation(itemId, value, oldValue, "state changed");
    }

    @PreDestroy
    public void shutdownScheduler() {
        LOG.info("Shutting down the scheduler");
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                LOG.warn("Scheduler did not terminate in time; forcing shutdown.");
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            LOG.error("Scheduler shutdown interrupted", e);
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
