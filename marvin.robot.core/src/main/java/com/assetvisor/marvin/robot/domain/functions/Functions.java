package com.assetvisor.marvin.robot.domain.functions;

import com.assetvisor.marvin.robot.domain.EnvironmentFunction;
import com.assetvisor.marvin.robot.domain.NoteBook;
import com.assetvisor.marvin.robot.domain.ports.ForGettingEnvironmentFunctions;
import com.assetvisor.marvin.robot.domain.ports.ForPersistingNotes;
import java.util.List;

public class Functions {

    private final ForGettingEnvironmentFunctions forGettingEnvironmentFunctions;
    private final ForPersistingNotes forPersistingNotes;

    public Functions(ForGettingEnvironmentFunctions forGettingEnvironmentFunctions,
        ForPersistingNotes forPersistingNotes) {
        this.forGettingEnvironmentFunctions = forGettingEnvironmentFunctions;
        this.forPersistingNotes = forPersistingNotes;
    }

    public List<EnvironmentFunction<?,?>> all() {
        List<EnvironmentFunction<?, ?>> environmentFunctions = forGettingEnvironmentFunctions.getEnvironmentFunctions();
        environmentFunctions.addAll(builtInFunctions());
        return environmentFunctions;
    }

    private List<EnvironmentFunction<?,?>> builtInFunctions() {
        return List.of(
            new LookAtWatch(),
            new WriteInNoteBook(new NoteBook(forPersistingNotes, null))
        );
    }
}
