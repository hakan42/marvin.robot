package com.assetvisor.marvin.persistence.adapters.cassandra.environment;

import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvironmentDescriptionRepository extends CassandraRepository<EnvironmentDescriptionEntry, UUID> {

}
