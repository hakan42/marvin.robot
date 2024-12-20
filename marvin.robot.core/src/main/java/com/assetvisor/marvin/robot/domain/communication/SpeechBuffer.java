package com.assetvisor.marvin.robot.domain.communication;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.LinkedList;
import java.util.Queue;
import org.springframework.stereotype.Component;

/**
 * A buffer for speeches that need to be spoken.
 * This class handles the queueing of speeches and the processing of speeches.
 * The speeches are processed in a separate thread.
 * The speeches are added to the queue by calling the {@link #add(Speech)} method.
 */
@Component
public class SpeechBuffer {

    @Resource
    private ForSpeaking forSpeaking;

    private final Queue<Speech> messageQueue = new LinkedList<>();
    private final Object lock = new Object();

    @PostConstruct
    public void start() {
        new Thread(() -> {
            while (true) {
                Speech speech;
                synchronized (lock) {
                    while (messageQueue.isEmpty()) {
                        try {
                            lock.wait();  // Wait until there's a message in the queue
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    speech = messageQueue.poll();  // Retrieve and remove the head of the queue
                }
                forSpeaking.say(speech);  // Process the message
            }
        }).start();
    }

    public void add(Speech speech) {
        synchronized (lock) {
            messageQueue.offer(speech);
            lock.notify();
        }
    }
}
