package com.assetvisor.marvin.robot.domain.environment;

import com.assetvisor.marvin.equipment.notebook.NoteBook;
import com.assetvisor.marvin.equipment.notebook.WriteInNoteBook;
import com.assetvisor.marvin.equipment.watch.LookAtWatch;
import java.util.List;

public class Functions {

    private final ForGettingEnvironmentFunctions forGettingEnvironmentFunctions;
    private final NoteBook noteBook;

    public Functions(ForGettingEnvironmentFunctions forGettingEnvironmentFunctions, NoteBook noteBook) {
        this.forGettingEnvironmentFunctions = forGettingEnvironmentFunctions;
        this.noteBook = noteBook;
    }

    public List<EnvironmentFunction<?,?>> all() {
        List<EnvironmentFunction<?, ?>> environmentFunctions = forGettingEnvironmentFunctions.getEnvironmentFunctions();
        environmentFunctions.addAll(builtInFunctions());
        return environmentFunctions;
    }

    private List<EnvironmentFunction<?,?>> builtInFunctions() {
        return List.of(
            new LookAtWatch(),
            new WriteInNoteBook(noteBook)
        );
    }
}
