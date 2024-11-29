package com.assetvisor.marvin.robot.domain.environment;

import com.assetvisor.marvin.equipment.notebook.NoteBook;
import com.assetvisor.marvin.equipment.notebook.WriteInNoteBook;
import com.assetvisor.marvin.equipment.watch.LookAtWatch;
import com.assetvisor.marvin.robot.domain.brain.ForRemembering;
import com.assetvisor.marvin.robot.domain.brain.Remember;
import java.util.ArrayList;
import java.util.List;

public class Functions {

    private final ForGettingEnvironmentFunctions forGettingEnvironmentFunctions;
    private final NoteBook noteBook;
    private final ForRemembering forRemembering;

    public Functions(ForGettingEnvironmentFunctions forGettingEnvironmentFunctions, NoteBook noteBook,
        ForRemembering forRemembering) {
        this.forGettingEnvironmentFunctions = forGettingEnvironmentFunctions;
        this.noteBook = noteBook;
        this.forRemembering = forRemembering;
    }

    public List<EnvironmentFunction<?,?>> all() {
        List<EnvironmentFunction<?, ?>> environmentFunctions = new ArrayList<>();
        environmentFunctions.addAll(forGettingEnvironmentFunctions.getEnvironmentFunctions());
        environmentFunctions.addAll(equipmentFunctions());
        return environmentFunctions;
    }

    private List<EnvironmentFunction<?,?>> equipmentFunctions() {
        return List.of(
            new LookAtWatch(),
//            new WriteInNoteBook(noteBook),
            new Remember(forRemembering)
        );
    }
}
