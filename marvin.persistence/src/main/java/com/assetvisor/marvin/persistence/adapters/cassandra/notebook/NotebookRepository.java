package com.assetvisor.marvin.persistence.adapters.cassandra.notebook;

import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotebookRepository extends CassandraRepository<NoteBookEntry, UUID> {

}
