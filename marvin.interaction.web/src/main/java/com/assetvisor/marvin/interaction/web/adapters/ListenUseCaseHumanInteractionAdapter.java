package com.assetvisor.marvin.interaction.web.adapters;

import com.assetvisor.marvin.robot.application.ListenUseCase;
import com.assetvisor.marvin.robot.domain.communication.AudioMessage;
import com.assetvisor.marvin.robot.domain.communication.TextMessage;
import jakarta.annotation.Resource;
import java.security.Principal;
import org.springframework.stereotype.Component;

@Component
public class ListenUseCaseHumanInteractionAdapter {
    @Resource
    private ListenUseCase listenUseCase;

    public void listenTo(String text, Principal principal) {
        listenUseCase.listenTo(
            new TextMessage(
                PrincipalMapper.userNameFrom(principal),
                PrincipalMapper.conversationIdFrom(principal),
                text
            ));
    }

    public void listenTo(byte[] audio, Principal principal) {
        listenUseCase.listenTo(
            new AudioMessage(
                PrincipalMapper.userNameFrom(principal),
                PrincipalMapper.conversationIdFrom(principal),
                audio
            ));
    }

}
