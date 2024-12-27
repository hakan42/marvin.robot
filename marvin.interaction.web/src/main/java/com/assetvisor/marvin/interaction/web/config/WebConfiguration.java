package com.assetvisor.marvin.interaction.web.config;

import com.assetvisor.marvin.interaction.web.adapters.OAuth2UserServiceToMarvinAdapter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, OAuth2UserServiceToMarvinAdapter userService) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/whatsapp", "/error", "/webjars").permitAll()
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/index.html") // Specify your custom login page URL
                .defaultSuccessUrl("/index.html", true) // Redirect after successful login
                .permitAll()
            )
            .logout(logout -> logout.logoutSuccessUrl("/").permitAll())
            .oauth2Login(oauth -> oauth.userInfoEndpoint(userInfo -> userInfo.userService(userService)))
            .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }
}
