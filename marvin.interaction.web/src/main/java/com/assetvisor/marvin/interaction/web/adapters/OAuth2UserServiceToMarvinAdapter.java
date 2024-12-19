package com.assetvisor.marvin.interaction.web.adapters;

import com.assetvisor.marvin.robot.application.PersonUco;
import com.assetvisor.marvin.robot.application.PersonUco.IdType;
import com.assetvisor.marvin.robot.application.PersonUco.PersonId;
import com.assetvisor.marvin.robot.application.PersonWantsToEnterUseCase;
import jakarta.annotation.Resource;
import java.util.Map;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class OAuth2UserServiceToMarvinAdapter extends DefaultOAuth2UserService {

    @Resource
    private PersonWantsToEnterUseCase personWantsToEnter;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest request, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        if (personWantsToEnter.attemptEntry(
                PersonUco.builder(
                        new PersonId(
                            IdType.fromRegistrationId(request.getClientRegistration().getRegistrationId()),
                            attributes.get("id").toString()
                        )
                    )
                    .withName(attributes.get("name").toString())
                    .withEmail(attributes.get("email").toString())
                    .build())
            .ok()
        ) {
            return oAuth2User;
        }

        throw new OAuth2AuthenticationException("Not a friend");
    }
}
