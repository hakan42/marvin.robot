package com.assetvisor.marvin.robot.config;

import com.assetvisor.marvin.robot.domain.brain.ForInvokingBrain;
import com.assetvisor.marvin.robot.domain.brain.Teacher;
import com.assetvisor.marvin.robot.domain.environment.ForGettingEnvironmentDescriptions;
import com.assetvisor.marvin.robot.domain.environment.ForPersistingEnvironmentDescriptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class RobotConfig {

    @Bean()
    @Profile("teach")
    public Teacher teacher(
        ForInvokingBrain forInvokingBrain,
        ForPersistingEnvironmentDescriptions forPersistingEnvironmentDescriptions,
        ForGettingEnvironmentDescriptions forGettingEnvironmentDescriptions
    ) {
        Teacher teacher = new Teacher(
            forInvokingBrain,
            forPersistingEnvironmentDescriptions,
            forGettingEnvironmentDescriptions
        );
        teacher.teach();
        return teacher;
    }
}
