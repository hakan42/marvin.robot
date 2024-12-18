package com.assetvisor.marvin.persistence.adapters.cassandra.vectorstore;

import com.assetvisor.marvin.persistence.adapters.cassandra.vectorstore.VectorStoreRepository.AiVectorStore;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VectorStoreRepository extends CassandraRepository<AiVectorStore, String> {

    @Table("ai_vector_store")
    class AiVectorStore {
        @Id
        private String id;
    }
}
