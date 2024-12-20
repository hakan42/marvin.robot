package com.assetvisor.marvin.interaction.web.adapters;

import java.security.Principal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public class PrincipalMapper {

    public static String userNameFrom(Principal principal) {
        if(principal instanceof OAuth2AuthenticationToken token) {
            return token.getPrincipal().getAttribute("name");
        }
        return "UNKNOWN";
    }

    public static String conversationIdFrom(Principal principal) {
        return principal.getName();
    }

}
