package com.assetvisor.marvin.interaction.web.adapters;

import com.assetvisor.marvin.robot.application.AddStrangerUseCase;
import com.assetvisor.marvin.robot.application.FindPersonUseCase;
import com.assetvisor.marvin.robot.application.FindPersonUseCase.PersonUco;
import com.assetvisor.marvin.robot.domain.relationships.Person.Relationship;
import jakarta.annotation.Resource;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class OAuth2UserServiceToMarvinAdapter extends DefaultOAuth2UserService {

    @Resource
    private FindPersonUseCase findPersonUseCase;
    @Resource
    private AddStrangerUseCase addStrangerUseCase;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuth2User(oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2User oAuth2User) {
        PersonUco person = findPersonUseCase.findPersonByEmail(
            oAuth2User.getAttributes().get("email").toString());
        if(person == null) {
            addStrangerUseCase.addStranger(
                oAuth2User.getAttributes().get("name").toString(),
                oAuth2User.getAttributes().get("email").toString());
        } else {
            if(person.relationship() == Relationship.FRIEND) {
                return oAuth2User;
            }
        }
        throw new OAuth2AuthenticationException("Not a friend");
    }
}
