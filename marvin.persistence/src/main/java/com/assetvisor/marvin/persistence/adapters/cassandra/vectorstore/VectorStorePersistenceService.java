package com.assetvisor.marvin.persistence.adapters.cassandra.vectorstore;

import jakarta.annotation.Resource;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.stereotype.Component;

@Component
public class VectorStorePersistenceService {

    @Resource
    private CqlTemplate cqlTemplate;

    public void deleteAll() {
        String query = "TRUNCATE ai_vector_store";
        cqlTemplate.execute(query);
    }
}
