package com.assetvisor.marvin.toolkit;

import static com.assetvisor.marvin.toolkit.ProfileChecker.LIBRARY_BOOKSTACK;

import com.assetvisor.marvin.robot.domain.tools.ForGettingOwnTools;
import com.assetvisor.marvin.robot.domain.tools.Tool;
import com.assetvisor.marvin.toolkit.internet.ForSearchingInternet;
import com.assetvisor.marvin.toolkit.internet.SearchInternetTool;
import com.assetvisor.marvin.toolkit.library.ForListingBooks;
import com.assetvisor.marvin.toolkit.library.ListLibraryBooksTool;
import com.assetvisor.marvin.toolkit.memory.ForRemembering;
import com.assetvisor.marvin.toolkit.memory.RememberTool;
import com.assetvisor.marvin.toolkit.notebook.NoteBook;
import com.assetvisor.marvin.toolkit.notebook.WriteInNoteBookTool;
import com.assetvisor.marvin.toolkit.watch.LookAtWatchTool;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class Toolkit implements ForGettingOwnTools {

    @Resource
    private ProfileChecker profileChecker;
    @Resource
    private NoteBook noteBook;
    @Resource
    private ForRemembering forRemembering;
    @Resource
    private ForSearchingInternet forSearchingInternet;
    @Resource
    @Lazy
    private ForListingBooks forListingBooks;


    @Override
    public List<Tool<?, ?>> getOwnTools() {
        List<Tool<?,?>> ret = new ArrayList<>();
        ret.add(new LookAtWatchTool());
        ret.add(new WriteInNoteBookTool(noteBook));
        ret.add(new RememberTool(forRemembering));
        ret.add(new SearchInternetTool(forSearchingInternet));
        if (profileChecker.isProfileActive(LIBRARY_BOOKSTACK)) {
            ret.add(new ListLibraryBooksTool(forListingBooks));
        }
        return ret;
    }
}
