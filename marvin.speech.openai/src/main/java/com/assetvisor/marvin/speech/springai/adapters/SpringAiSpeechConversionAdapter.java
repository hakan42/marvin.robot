package com.assetvisor.marvin.speech.springai.adapters;

import com.assetvisor.marvin.robot.domain.communication.ForConvertingTextToSpeech;
import jakarta.annotation.Resource;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi.SpeechRequest.AudioResponseFormat;
import org.springframework.ai.openai.api.OpenAiAudioApi.SpeechRequest.Voice;
import org.springframework.ai.openai.api.OpenAiAudioApi.TtsModel;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.stereotype.Component;

@Component
public class SpringAiSpeechConversionAdapter implements ForConvertingTextToSpeech {

    @Resource
    private OpenAiAudioSpeechModel openAiAudioSpeechModel;

    @Override
    public byte[] convert(String text) {
        OpenAiAudioSpeechOptions speechOptions = OpenAiAudioSpeechOptions.builder()
            .withResponseFormat(AudioResponseFormat.MP3)
            .withSpeed(1.0f)
            .withModel(TtsModel.TTS_1.value)
            .withVoice(Voice.FABLE)
            .build();

        var speechPrompt = new SpeechPrompt(text, speechOptions);
        SpeechResponse response = openAiAudioSpeechModel.call(speechPrompt);
        return response.getResult().getOutput();
    }
}
