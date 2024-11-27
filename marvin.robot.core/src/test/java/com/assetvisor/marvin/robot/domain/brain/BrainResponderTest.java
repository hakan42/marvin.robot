package com.assetvisor.marvin.robot.domain.brain;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.assetvisor.marvin.robot.domain.communication.ForConvertingTextToSpeech;
import com.assetvisor.marvin.robot.domain.communication.ForMessaging;
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
    @Spy
    private SpeechBuffer speechBuffer;
    @InjectMocks
    private BrainResponder brainResponder;

    @Test
    public void shouldRespond() {
        // Given
        String message = "message";
        byte[] bytes = {1, 2, 3};

        given(forConvertingTextToSpeech.convert(message)).willReturn(bytes);

        // When
        brainResponder.respond(message);
        // Then
        verify(forConvertingTextToSpeech).convert(message);
        verify(speechBuffer).add(bytes);
        verify(forMessaging).message(message);
        verifyNoMoreInteractions(forMessaging, forConvertingTextToSpeech, speechBuffer);
    }
}