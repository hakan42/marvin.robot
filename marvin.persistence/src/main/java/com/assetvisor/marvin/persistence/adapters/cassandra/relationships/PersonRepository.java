package com.assetvisor.marvin.persistence.adapters.cassandra.relationships;

import java.util.List;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CassandraRepository<PersonEntry, UUID> {

    @Query("SELECT * FROM personentry WHERE email = ?0 ALLOW FILTERING")
    List<PersonEntry> findByEmail(String email);

    @Query("SELECT * FROM personentry WHERE github_id = ?0 ALLOW FILTERING")
    List<PersonEntry> findByGithubId(String githubId);
}
