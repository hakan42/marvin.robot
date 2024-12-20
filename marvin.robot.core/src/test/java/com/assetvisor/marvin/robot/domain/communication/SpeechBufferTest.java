package com.assetvisor.marvin.robot.domain.communication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SpeechBufferTest {
    @Mock
    private ForSpeaking forSpeaking;
    @InjectMocks
    private SpeechBuffer speechBuffer;

    @BeforeEach
    public void setUp() {
        speechBuffer.start();
    }

    @Test
    public void shouldSayMessage() throws InterruptedException {
        // Given
        Speech speech = new Speech("Marvin", new byte[]{1, 2, 3});
        givenSlowSpeaker();

        // When
        speechBuffer.add(speech);
        speechBuffer.add(speech);
        speechBuffer.add(speech);
        speechBuffer.add(speech);

        // Then
        Thread.sleep(400);
        verify(forSpeaking, times(4)).say(speech);
    }

    private void givenSlowSpeaker() {
        //noinspection unused
        doAnswer(invocation -> {
            Thread.sleep(100); // Simulate slow response
            return null;
        }).when(forSpeaking).say(any());
    }
}