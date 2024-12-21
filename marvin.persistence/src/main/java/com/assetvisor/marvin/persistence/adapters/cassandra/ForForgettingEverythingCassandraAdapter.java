package com.assetvisor.marvin.persistence.adapters.cassandra;

import com.assetvisor.marvin.persistence.adapters.cassandra.chatmemory.ChatMemoryPersistenceService;
import com.assetvisor.marvin.persistence.adapters.cassandra.vectorstore.VectorStorePersistenceService;
import com.assetvisor.marvin.robot.domain.brain.ForForgettingEverything;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ForForgettingEverythingCassandraAdapter implements ForForgettingEverything {

    @Resource
    private VectorStorePersistenceService vectorStorePersistenceService;
    @Resource
    private ChatMemoryPersistenceService chatMemoryPersistenceService;

    @Override
    public void forgetEverything() {
        vectorStorePersistenceService.deleteAll();
        chatMemoryPersistenceService.deleteAll();
    }
}
