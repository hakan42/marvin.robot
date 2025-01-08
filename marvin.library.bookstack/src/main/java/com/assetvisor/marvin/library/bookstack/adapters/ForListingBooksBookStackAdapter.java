package com.assetvisor.marvin.library.bookstack.adapters;

import static com.assetvisor.marvin.toolkit.ProfileChecker.LIBRARY_BOOKSTACK;

import com.assetvisor.marvin.toolkit.library.ForListingBooks;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import java.io.IOException;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
@Profile(LIBRARY_BOOKSTACK)
public class ForListingBooksBookStackAdapter implements ForListingBooks {
    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private RestClient bookStackRestClient;

    @Override
    public List<Book> listBooks() {
        LOG.info("Listing books");
        try {
            String json = bookStackRestClient.get()
                .uri("books")
                .retrieve()
                .body(String.class);
            return map(json);
        } catch (HttpClientErrorException | IOException e) {
            LOG.error("Failed to list books", e);
            return List.of();
        }
    }

    private List<Book> map(String json) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();

            List<BookJsonEntry> bookJsonEntries = objectMapper.readTree(json)
                .get("data")
                .traverse(objectMapper)
                .readValueAs(new TypeReference<List<BookJsonEntry>>() {});

            return bookJsonEntries.stream()
                .map(bookJsonEntry -> new Book(bookJsonEntry.id(), bookJsonEntry.name(), bookJsonEntry.description()))
                .toList();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record BookJsonEntry(String id, String name, String description) {}
}
