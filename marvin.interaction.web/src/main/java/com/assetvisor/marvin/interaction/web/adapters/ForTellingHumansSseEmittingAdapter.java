package com.assetvisor.marvin.interaction.web.adapters;

import com.assetvisor.marvin.interaction.web.AudioBuffer;
import com.assetvisor.marvin.robot.domain.communication.ForCheckingIfAnybodyIsListening;
import com.assetvisor.marvin.robot.domain.communication.ForMessaging;
import com.assetvisor.marvin.robot.domain.communication.ForSpeaking;
import com.assetvisor.marvin.robot.domain.communication.Message;
import com.assetvisor.marvin.robot.domain.communication.Speech;
import jakarta.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component("web")
public class ForTellingHumansSseEmittingAdapter implements ForSpeaking, ForMessaging, ForCheckingIfAnybodyIsListening {

    private final Log LOG = LogFactory.getLog(getClass());

    @Resource
    private AudioBuffer audioBuffer;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        LOG.info("Added emitter: " + emitter);
        emitter.onCompletion(() -> {
            emitters.remove(emitter);
            LOG.info("Removed emitter: " + emitter);
        });
        emitter.onTimeout(() -> {
            emitters.remove(emitter);
            LOG.info("Removed emitter: " + emitter);
        });
        //noinspection unused
        emitter.onError((e) -> {
            emitters.remove(emitter);
            LOG.error("Error in SSE connection, removed emitter: " + emitter);
        });
    }


    @Override
    public void message(Message message) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(message);
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }

    @Override
    public void say(Speech speech) {
        audioBuffer.set(speech.audio());
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("chatReady").data("audio").build());
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }

    @Override
    public boolean isAnybodyListening() {
        return !emitters.isEmpty();
    }
}