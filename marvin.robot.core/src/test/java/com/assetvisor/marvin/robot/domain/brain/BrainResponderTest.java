package com.assetvisor.marvin.robot.domain.brain;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.assetvisor.marvin.robot.domain.communication.AudioMessage;
import com.assetvisor.marvin.robot.domain.communication.ForCheckingIfAnybodyIsListening;
import com.assetvisor.marvin.robot.domain.communication.ForConvertingTextToSpeech;
import com.assetvisor.marvin.robot.domain.communication.ForTexting;
import com.assetvisor.marvin.robot.domain.communication.SpeechBuffer;
import com.assetvisor.marvin.robot.domain.communication.TextMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BrainResponderTest {
    @Mock
    private ForTexting forMessaging;
    @Mock
    private ForConvertingTextToSpeech forConvertingTextToSpeech;
    @Mock
    private ForCheckingIfAnybodyIsListening forCheckingIfAnybodyIsListening;
    @Spy
    private SpeechBuffer speechBuffer;
    @InjectMocks
    private BrainResponder brainResponder;

    @Test
    public void shouldRespond() {
        // Given
        TextMessage message = new TextMessage("Marvin", "C1", "message");
        AudioMessage speech = new AudioMessage("Marvin", "C1", new byte[]{1, 2, 3});

        given(forConvertingTextToSpeech.convert(message.getContent())).willReturn(speech.getAudio());
        given(forCheckingIfAnybodyIsListening.isAnybodyListening()).willReturn(true);

        // When
        brainResponder.respond(message.getContent(), message.getConversationId());
        // Then
        verify(forConvertingTextToSpeech).convert(message.getContent());
        verify(speechBuffer).add(speech);
        verify(forMessaging).text(message);
        verifyNoMoreInteractions(forMessaging, forConvertingTextToSpeech, speechBuffer);
    }
}