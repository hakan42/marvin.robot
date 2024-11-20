package com.assetvisor.marvin.robot.application.services;

import com.assetvisor.marvin.robot.domain.notebook.NoteBook;
import com.assetvisor.marvin.robot.domain.environment.Functions;
import com.assetvisor.marvin.robot.domain.environment.ForGettingEnvironmentDescriptions;
import com.assetvisor.marvin.robot.domain.environment.ForGettingEnvironmentFunctions;
import com.assetvisor.marvin.robot.domain.brain.ForInvokingBrain;
import com.assetvisor.marvin.robot.domain.notebook.ForPersistingNotes;
import com.assetvisor.marvin.robot.domain.jobdescription.ForPersistingRobotDescription;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RobotLifecycleService {

    @Resource
    private ForInvokingBrain forInvokingBrain;
    @Resource
    private ForPersistingRobotDescription forPersistingRobotDescription;
    @Resource
    private ForPersistingNotes forPersistingNotes;
    @Resource
    private ForGettingEnvironmentDescriptions forGettingEnvironmentDescriptions;
    @Resource
    private ForGettingEnvironmentFunctions forGettingEnvironmentFunctions;

    @PostConstruct
    public void born() {
        Functions functions = new Functions(
            forGettingEnvironmentFunctions,
            forPersistingNotes
        );
        forInvokingBrain.initialise(
            forPersistingRobotDescription.read(),
            forGettingEnvironmentDescriptions.getEnvironmentDescriptions(),
            functions.all()
        );
        forInvokingBrain.invoke("You are born!", true);
        forInvokingBrain.invoke("Check the current time", true);
    }

    @Scheduled(fixedDelay = 60000)
    public void processNoteBook() {
        NoteBook noteBook = new NoteBook(forPersistingNotes, forInvokingBrain);
        noteBook.readNoteForNow();
    }
}
