package com.assetvisor.marvin.persistence.adapters.cassandra.chatmemory;

import jakarta.annotation.Resource;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.stereotype.Component;

@Component
public class ChatMemoryPersistenceService {

    @Resource
    private CqlTemplate cqlTemplate;

    public void deleteAll() {
        String query = "TRUNCATE ai_chat_memory";
        cqlTemplate.execute(query);
    }
}
