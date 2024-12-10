package com.assetvisor.marvin.speech.springai.adapters;

import com.assetvisor.marvin.robot.domain.communication.ForConvertingSpeechToText;
import com.assetvisor.marvin.robot.domain.communication.ForConvertingTextToSpeech;
import jakarta.annotation.Resource;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi.SpeechRequest.AudioResponseFormat;
import org.springframework.ai.openai.api.OpenAiAudioApi.SpeechRequest.Voice;
import org.springframework.ai.openai.api.OpenAiAudioApi.TranscriptResponseFormat;
import org.springframework.ai.openai.api.OpenAiAudioApi.TtsModel;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.stereotype.Component;

@Component
public class SpringAiSpeechConversionAdapter implements ForConvertingTextToSpeech, ForConvertingSpeechToText {

    @Resource
    private OpenAiAudioSpeechModel openAiAudioSpeechModel;
    @Resource
    private OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;

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

    @Override
    public String convert(byte[] speech) {
        OpenAiAudioTranscriptionOptions transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
            .withResponseFormat(TranscriptResponseFormat.TEXT)
            .withTemperature(0.0f)
            .build();

        org.springframework.core.io.Resource audioResource = new org.springframework.core.io.ByteArrayResource(speech);

        var audioTranscriptionPrompt = new AudioTranscriptionPrompt(audioResource, transcriptionOptions);
        var response = openAiAudioTranscriptionModel.call(audioTranscriptionPrompt);
        return response.getResult().getOutput();
    }
}
