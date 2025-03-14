package com.assetvisor.marvin.interaction.web.config;

import com.assetvisor.marvin.robot.domain.relationships.ForPersistingPerson;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PersistingPersonsUserDetailsService implements UserDetailsService {

    @Resource
    private ForPersistingPerson forPersistingPerson;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var person = forPersistingPerson.byEmail(username);
        if (person == null) {
            throw new UsernameNotFoundException(username);
        }

        return User.builder()
            .username(person.email())
            .password(person.password())
            .build();
    }
}
