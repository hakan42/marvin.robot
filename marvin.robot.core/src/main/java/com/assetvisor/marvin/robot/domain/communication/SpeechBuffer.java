package com.assetvisor.marvin.robot.domain.communication;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.util.LinkedList;
import java.util.Queue;
import org.springframework.stereotype.Component;

/**
 * A buffer for speech messages that need to be spoken.
 * This class handles the queueing of messages and the processing of messages.
 * The messages are processed in a separate thread.
 * The messages are added to the queue by calling the {@link #add(byte[])} method.
 */
@Component
public class SpeechBuffer {

    @Resource
    private ForSpeaking forSpeaking;

    private final Queue<byte[]> messageQueue = new LinkedList<>();
    private final Object lock = new Object();

    @PostConstruct
    public void start() {
        new Thread(() -> {
            while (true) {
                byte[] message;
                synchronized (lock) {
                    while (messageQueue.isEmpty()) {
                        try {
                            lock.wait();  // Wait until there's a message in the queue
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    message = messageQueue.poll();  // Retrieve and remove the head of the queue
                }
                forSpeaking.say(message);  // Process the message
            }
        }).start();
    }

    public void add(byte[] message) {
        synchronized (lock) {
            messageQueue.offer(message);
            lock.notify();
        }
    }
}
