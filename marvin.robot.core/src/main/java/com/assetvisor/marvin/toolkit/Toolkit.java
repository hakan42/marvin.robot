package com.assetvisor.marvin.toolkit;

import com.assetvisor.marvin.robot.domain.brain.ForRemembering;
import com.assetvisor.marvin.robot.domain.brain.Remember;
import com.assetvisor.marvin.robot.domain.tools.ForGettingOwnTools;
import com.assetvisor.marvin.robot.domain.tools.Tool;
import com.assetvisor.marvin.toolkit.internet.ForSearchingInternet;
import com.assetvisor.marvin.toolkit.internet.SearchInternetTool;
import com.assetvisor.marvin.toolkit.notebook.NoteBook;
import com.assetvisor.marvin.toolkit.notebook.WriteInNoteBookTool;
import com.assetvisor.marvin.toolkit.watch.LookAtWatchTool;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class Toolkit implements ForGettingOwnTools {

    @Resource
    private NoteBook noteBook;
    @Resource
    private ForRemembering forRemembering;
    @Resource
    private ForSearchingInternet forSearchingInternet;


    @Override
    public List<Tool<?, ?>> getOwnTools() {
        return List.of(
            new LookAtWatchTool(),
            new WriteInNoteBookTool(noteBook),
            new Remember(forRemembering),
            new SearchInternetTool(forSearchingInternet)
        );
    }
}
