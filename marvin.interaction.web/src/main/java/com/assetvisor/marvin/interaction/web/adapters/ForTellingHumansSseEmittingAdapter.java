package com.assetvisor.marvin.interaction.web.adapters;

import com.assetvisor.marvin.interaction.web.AudioBuffer;
import com.assetvisor.marvin.robot.domain.communication.ForCheckingIfAnybodyIsListening;
import com.assetvisor.marvin.robot.domain.communication.ForTexting;
import com.assetvisor.marvin.robot.domain.communication.ForSpeaking;
import com.assetvisor.marvin.robot.domain.communication.Message;
import com.assetvisor.marvin.robot.domain.communication.AudioMessage;
import com.assetvisor.marvin.robot.domain.communication.TextMessage;
import jakarta.annotation.Resource;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component("web")
public class ForTellingHumansSseEmittingAdapter implements ForSpeaking, ForTexting, ForCheckingIfAnybodyIsListening {

    private final Log LOG = LogFactory.getLog(getClass());

    @Resource
    private AudioBuffer audioBuffer;

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();//conversationId -> emitter

    public void addEmitter(Principal principal, SseEmitter emitter) {
        emitters.put(PrincipalMapper.conversationIdFrom(principal), emitter);
        LOG.info("Added emitter: " + emitter);
        emitter.onCompletion(() -> {
            emitters.entrySet().removeIf(entry -> entry.getValue().equals(emitter));
            LOG.info("Removed emitter: " + emitter);
        });
        emitter.onTimeout(() -> {
            emitters.entrySet().removeIf(entry -> entry.getValue().equals(emitter));
            LOG.info("Removed emitter: " + emitter);
        });
        //noinspection unused
        emitter.onError((e) -> {
            emitters.entrySet().removeIf(entry -> entry.getValue().equals(emitter));
            LOG.error("Error in SSE connection, removed emitter: " + emitter);
        });
    }

    @Override
    public void text(TextMessage message) {
        resolveEmitter(message).ifPresent(emitter -> {
            try {
                emitter.send(message);
            } catch (IOException e) {
                emitters.remove(message.getSender());
            }
        });
    }

    @Override
    public void say(AudioMessage speech) {
        audioBuffer.set(speech.getAudio());
        resolveEmitter(speech).ifPresent(emitter -> {
            try {
                emitter.send(SseEmitter.event().name("chatReady").data("audio").build());
            } catch (IOException e) {
                emitters.remove(speech.getSender());
            }
        });
    }

    private Optional<SseEmitter> resolveEmitter(Message message) {
        return Optional.ofNullable(emitters.get(message.getSender()))
            .or(() -> Optional.ofNullable(emitters.get(message.getConversationId())));
    }

    @Override
    public boolean isAnybodyListening() {
        return !emitters.isEmpty();
    }
}