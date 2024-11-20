package com.assetvisor.marvin.robot.domain.environment;

import com.assetvisor.marvin.robot.domain.watch.LookAtWatch;
import com.assetvisor.marvin.robot.domain.notebook.NoteBook;
import com.assetvisor.marvin.robot.domain.notebook.WriteInNoteBook;
import com.assetvisor.marvin.robot.domain.notebook.ForPersistingNotes;
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
