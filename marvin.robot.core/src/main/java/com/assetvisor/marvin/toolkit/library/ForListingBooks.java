package com.assetvisor.marvin.toolkit.library;

import java.util.List;

public interface ForListingBooks {
    List<Book> listBooks();

    record Book(String id, String name, String description) {}

}
