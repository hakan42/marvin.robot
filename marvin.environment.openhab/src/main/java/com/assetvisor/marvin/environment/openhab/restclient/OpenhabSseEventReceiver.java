package com.assetvisor.marvin.environment.openhab.restclient;

import com.assetvisor.marvin.robot.application.SomethingHappenedInTheEnvironmentUseCase;
import com.assetvisor.marvin.robot.domain.environment.Observation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@Profile("environment-openhab")
public class OpenhabSseEventReceiver {

    private static final Log LOG = LogFactory.getLog(OpenhabSseEventReceiver.class);
    private static final int DEBOUNCE_TIME_MS = 3000; // Debounce time in milliseconds

    @Resource
    private SomethingHappenedInTheEnvironmentUseCase observeUseCase;
    @Resource
    private WebClient webClient;
    @Resource
    private GetEventPublishingItemsRestClient getEventPublishingItemsRestClient;
    @Resource
    private SentCommands sentCommands;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Map<String, ScheduledFuture<?>> debounceTasks = new ConcurrentHashMap<>();
    private final Map<String, String> initialOldValues = new ConcurrentHashMap<>(); // Cache for initial context
    private final List<String> itemIds = new ArrayList<>();

    @PostConstruct
    public void listenForSseEvents() {
        itemIds.addAll(getEventPublishingItemsRestClient.asIds());
        connect();
    }

    private void connect() {
        @SuppressWarnings("rawtypes")
        Flux<Map> eventStream = webClient.get()
            .uri("events") // Replace with the correct SSE endpoint
            .accept(MediaType.TEXT_EVENT_STREAM)
            .retrieve()
            .bodyToFlux(Map.class);

        eventStream.subscribe(
            this::handleEvent,
            error -> LOG.error("Error: " + error),
            () -> LOG.info("Stream completed")
        );
        LOG.info("Receiving events for: " + itemIds);
    }

    private void handleEvent(Map<?, ?> event) {
        if ("ALIVE".equals(event.get("type"))) {
            return;
        }
        Topic topic = parseTopic(event);
        if (!isRelevant(topic)) {
            return;
        }

        Runnable debounceTask = () -> {
            Observation observation = toObservation(event);

            // Correctly detect state changes
            if (observation.oldValue().equals(observation.value())) {
                LOG.warn("Redundant state change detected for item: " + topic.itemId);
                return;
            }

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

            // Update the initialOldValues cache after processing
            initialOldValues.put(topic.itemId, observation.oldValue());
            debounceTasks.remove(topic.itemId);
        };

        // Cancel any existing task for this itemId
        if (debounceTasks.containsKey(topic.itemId)) {
            debounceTasks.get(topic.itemId).cancel(false);
        }

        // Schedule the new debounce task and store its Future
        ScheduledFuture<?> futureTask = scheduler.schedule(debounceTask, DEBOUNCE_TIME_MS, TimeUnit.MILLISECONDS);
        debounceTasks.put(topic.itemId, futureTask);
    }

    private boolean isRelevant(Topic topic) {
        return itemIds.contains(topic.itemId)
            && (
            topic.type.equals("ItemStateChangedEvent")
                || topic.type.equals("GroupItemStateChangedEvent")
        );
    }

    private Topic parseTopic(Map<?, ?> event) {
        String topic = (String) event.get("topic");
        String[] parts = topic.split("/");
        return new Topic(parts[2], event.get("type").toString(), event.get("payload").toString());
    }

    private record Topic(String itemId, String type, String payload) {}

    private Observation toObservation(Map<?, ?> event) {
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
        String oldValue = (String) payloadMap.get("oldValue");
        return new Observation("OpenHAB", itemId, value, oldValue, "state changed");
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
