package com.assetvisor.marvin.robot.domain.brain;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.assetvisor.marvin.robot.domain.communication.ConversationMessage;
import com.assetvisor.marvin.robot.domain.communication.ForConvertingSpeechToText;
import com.assetvisor.marvin.robot.domain.communication.SpeechMessage;
import com.assetvisor.marvin.robot.domain.communication.ForCheckingIfAnybodyIsListening;
import com.assetvisor.marvin.robot.domain.communication.ForConvertingTextToSpeech;
import com.assetvisor.marvin.robot.domain.communication.ForTexting;
import com.assetvisor.marvin.robot.domain.communication.SpeechBuffer;
import com.assetvisor.marvin.robot.domain.communication.TextMessage;
import com.assetvisor.marvin.robot.domain.environment.Observation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BrainTest {
    @Mock
    private ForTexting forTexting;
    @Mock
    private ForConvertingTextToSpeech forConvertingTextToSpeech;
    @Mock
    private ForConvertingSpeechToText forConvertingSpeechToText;
    @Mock
    private ForCheckingIfAnybodyIsListening forCheckingIfAnybodyIsListening;
    @Mock
    private ForInvokingIntelligence forInvokingIntelligence;
    @Spy
    private SpeechBuffer speechBuffer;
    @InjectMocks
    private Brain brain;

    @Test
    public void shouldRespond() {
        // Given
        TextMessage message = new TextMessage("Marvin", "C1", "message");
        SpeechMessage speech = new SpeechMessage("Marvin", "C1", new byte[]{1, 2, 3});

        given(forConvertingTextToSpeech.convert(message.getContent())).willReturn(speech.getAudio());
        given(forCheckingIfAnybodyIsListening.isAnybodyListening()).willReturn(true);

        // When
        brain.respond(message.getContent(), message.conversationId());
        // Then
        verify(forConvertingTextToSpeech).convert(message.getContent());
        verify(speechBuffer).add(speech);
        verify(forTexting).text(message, false);
        verifyNoMoreInteractions(forTexting, forConvertingTextToSpeech, speechBuffer);
    }

    @Test
    public void shouldObserve() {
        // Given
        Observation observation = new Observation("Marvin", "I1", "2", "1", "observation");
        // When
        brain.observe(observation);
        // Then
        verify(forInvokingIntelligence).invoke(
            observation,
            brain
        );
        verifyNoMoreInteractions(forInvokingIntelligence);
        verifyNoInteractions(forTexting, forConvertingTextToSpeech, speechBuffer);
    }

    @Test
    public void shouldRead() {
        // Given
        TextMessage textMessage = new TextMessage("Marvin", "C1", "message");
        // When
        brain.read(textMessage, true);
        // Then
        verify(forInvokingIntelligence).invoke(
            textMessage,
            brain
        );
        verify(forTexting).text(textMessage, true);
        verifyNoMoreInteractions(forInvokingIntelligence, forTexting);
        verifyNoInteractions(forConvertingTextToSpeech, speechBuffer);
    }

    @Test
    public void shouldListenTo() {
        // Given
        SpeechMessage speechMessage = new SpeechMessage("Marvin", "C1", new byte[]{1, 2, 3});
        TextMessage textMessage = new TextMessage("Marvin", "C1", "text");
        given(forConvertingSpeechToText.convert(speechMessage.getAudio())).willReturn("text");
        // When
        brain.listenTo(speechMessage);
        // Then
        verify(forConvertingSpeechToText).convert(speechMessage.getAudio());
        verify(forTexting).text(new TextMessage("Marvin", "C1", "text"), true);
        verify(forInvokingIntelligence).invoke(
            textMessage,
            brain
        );
        verifyNoMoreInteractions(forInvokingIntelligence, forTexting, forConvertingSpeechToText);
        verifyNoInteractions(forConvertingTextToSpeech, speechBuffer);
    }
}