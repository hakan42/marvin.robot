package com.assetvisor.marvin.interaction.web.adapters;

import com.assetvisor.marvin.robot.application.SomethingWasSaidUseCase;
import com.assetvisor.marvin.robot.application.SomethingWasTextedUseCase;
import com.assetvisor.marvin.robot.domain.communication.SpeechMessage;
import com.assetvisor.marvin.robot.domain.communication.TextMessage;
import jakarta.annotation.Resource;
import java.security.Principal;
import org.springframework.stereotype.Component;

@Component
public class ListenUseCaseHumanInteractionAdapter {
    @Resource
    private SomethingWasSaidUseCase listenUseCase;
    @Resource
    private SomethingWasTextedUseCase somethingWasTextedUseCase;

    public void listenTo(String text, Principal principal) {
        somethingWasTextedUseCase.read(
            new TextMessage(
                PrincipalMapper.userNameFrom(principal),
                PrincipalMapper.conversationIdFrom(principal),
                text
            ));
    }

    public void listenTo(byte[] audio, Principal principal) {
        listenUseCase.listenTo(
            new SpeechMessage(
                PrincipalMapper.userNameFrom(principal),
                PrincipalMapper.conversationIdFrom(principal),
                audio
            ));
    }

}
