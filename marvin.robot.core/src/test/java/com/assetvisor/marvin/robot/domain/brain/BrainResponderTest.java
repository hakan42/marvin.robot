package com.assetvisor.marvin.robot.domain.brain;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.assetvisor.marvin.robot.domain.communication.ForCheckingIfAnybodyIsListening;
import com.assetvisor.marvin.robot.domain.communication.ForConvertingTextToSpeech;
import com.assetvisor.marvin.robot.domain.communication.ForMessaging;
import com.assetvisor.marvin.robot.domain.communication.Message;
import com.assetvisor.marvin.robot.domain.communication.Speech;
import com.assetvisor.marvin.robot.domain.communication.SpeechBuffer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BrainResponderTest {
    @Mock
    private ForMessaging forMessaging;
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
        Message message = new Message("Marvin", "message");
        Speech speech = new Speech("Marvin", new byte[]{1, 2, 3});

        given(forConvertingTextToSpeech.convert(message.content())).willReturn(speech.audio());
        given(forCheckingIfAnybodyIsListening.isAnybodyListening()).willReturn(true);

        // When
        brainResponder.respond(message.content());
        // Then
        verify(forConvertingTextToSpeech).convert(message.content());
        verify(speechBuffer).add(speech);
        verify(forMessaging).message(message);
        verifyNoMoreInteractions(forMessaging, forConvertingTextToSpeech, speechBuffer);
    }
}