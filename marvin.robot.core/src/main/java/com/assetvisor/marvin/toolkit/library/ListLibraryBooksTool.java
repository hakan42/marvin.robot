package com.assetvisor.marvin.toolkit.library;

import com.assetvisor.marvin.robot.domain.tools.Tool;
import com.assetvisor.marvin.toolkit.library.ForListingBooks.Book;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ListLibraryBooksTool implements Tool<Void, List<Book>> {

    private static final Log LOG = LogFactory.getLog(ListLibraryBooksTool.class);
    private final ForListingBooks forListingBooks;

    public ListLibraryBooksTool(ForListingBooks forListingBooks) {
        this.forListingBooks = forListingBooks;
    }

    @Override
    public String name() {
        return "ListLibraryBooks";
    }

    @Override
    public String description() {
        return "This tool is used for listing books.";
    }

    @Override
    public Class<?> inputType() {
        return Void.class;
    }

    @Override
    public List<Book> apply(Void unused) {
        return forListingBooks.listBooks();
    }

}
