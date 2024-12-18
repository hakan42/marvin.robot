package com.assetvisor.marvin.persistence.adapters.cassandra.vectorstore;

import com.assetvisor.marvin.robot.domain.brain.ForForgettingEverything;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class VectorStoreCassandraAdapter implements ForForgettingEverything {

    @Resource
    private VectorStoreRepository vectorStoreRepository;

    @Override
    public void forgetEverything() {
        vectorStoreRepository.deleteAll();
    }
}
