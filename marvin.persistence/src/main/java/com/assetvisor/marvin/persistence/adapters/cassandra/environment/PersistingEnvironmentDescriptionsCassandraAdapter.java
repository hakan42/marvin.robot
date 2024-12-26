package com.assetvisor.marvin.persistence.adapters.cassandra.environment;

import com.assetvisor.marvin.robot.domain.environment.EnvironmentDescription;
import com.assetvisor.marvin.robot.domain.environment.ForPersistingEnvironmentDescriptions;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
//@Profile("cassandra")
public class PersistingEnvironmentDescriptionsCassandraAdapter implements ForPersistingEnvironmentDescriptions {

    @Resource
    private EnvironmentDescriptionRepository environmentDescriptionRepository;


    @Override
    public List<EnvironmentDescription> load() {
        return environmentDescriptionRepository.findAll().stream()
            .map(this::toEnvironmentDescription)
            .toList();
    }

    @Override
    public void save(EnvironmentDescription description) {
        environmentDescriptionRepository.save(toEnvironmentDescriptionEntry(description));
    }

    private EnvironmentDescription toEnvironmentDescription(EnvironmentDescriptionEntry environmentDescriptionEntry) {
        return new EnvironmentDescription(
            environmentDescriptionEntry.getDescription()
        );
    }

    private EnvironmentDescriptionEntry toEnvironmentDescriptionEntry(EnvironmentDescription environmentDescription) {
        EnvironmentDescriptionEntry environmentDescriptionEntry = new EnvironmentDescriptionEntry();
        environmentDescriptionEntry.setId(UUID.randomUUID());
        environmentDescriptionEntry.setDescription(environmentDescription.text());
        return environmentDescriptionEntry;
    }

}
