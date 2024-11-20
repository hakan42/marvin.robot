package com.assetvisor.marvin.interaction.speech.adapters;

import com.assetvisor.marvin.robot.domain.communication.ForTellingHumans;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.Queue;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi.SpeechRequest.AudioResponseFormat;
import org.springframework.ai.openai.api.OpenAiAudioApi.SpeechRequest.Voice;
import org.springframework.ai.openai.api.OpenAiAudioApi.TtsModel;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.stereotype.Component;

@Component("speech")
public class ForTellingHumansSpeechAdapter implements ForTellingHumans {

    private final Log LOG = LogFactory.getLog(getClass());

    @Resource
    private OpenAiAudioSpeechModel openAiAudioSpeechModel;

    private final Queue<String> messageQueue = new LinkedList<>();
    private final Object lock = new Object();

    @PostConstruct
    public void start() {
        new Thread(() -> {
            while (true) {
                String message;
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
                say(message);  // Process the message
            }
        }).start();
    }

    @Override
    public void tell(String message) {
        LOG.debug("Telling: " + message);
        synchronized (lock) {
            messageQueue.offer(message);
            lock.notify();
        }
    }

    private void say(String message) {
        LOG.debug("Saying: " + message);
        OpenAiAudioSpeechOptions speechOptions = OpenAiAudioSpeechOptions.builder()
            .withResponseFormat(AudioResponseFormat.MP3)
            .withSpeed(1.0f)
            .withModel(TtsModel.TTS_1.value)
            .withVoice(Voice.ALLOY)
            .build();

        var speechPrompt = new SpeechPrompt(message, speechOptions);
        SpeechResponse response = openAiAudioSpeechModel.call(speechPrompt);
        byte[] output = response.getResult().getOutput();

        Player player = null;
        try {
            player = new Player(new ByteArrayInputStream(output));
            player.play();
        } catch (JavaLayerException e) {
            throw new RuntimeException(e);
        } finally {
            assert player != null;
            player.close();
        }
    }
}
