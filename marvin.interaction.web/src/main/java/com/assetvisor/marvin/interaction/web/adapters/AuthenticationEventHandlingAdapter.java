package com.assetvisor.marvin.interaction.web.adapters;

import com.assetvisor.marvin.robot.application.PersonEnteredUseCase;
import com.assetvisor.marvin.robot.domain.relationships.Person;
import jakarta.annotation.Resource;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEventHandlingAdapter {

    @Resource
    private PersonEnteredUseCase personEnteredUseCase;

    @EventListener
    public void handle(AuthenticationSuccessEvent event) {
        personEnteredUseCase.personEntered(toId(event));
    }

    private Person.PersonId toId(AuthenticationSuccessEvent event) {
        Object source = event.getSource();
        if (source instanceof OAuth2LoginAuthenticationToken token) {
            OAuth2User principal = token.getPrincipal();
            String id = principal.getName();
            return new Person.PersonId(Person.IdType.fromRegistrationId(
                token
                    .getClientRegistration()
                    .getRegistrationId()
            ),
                id
            );
        }
        throw new IllegalArgumentException("Unsupported event source: " + event.getSource());
    }

}
