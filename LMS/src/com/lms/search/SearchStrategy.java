package com.lms.search;

import com.lms.models.Book;
import java.util.Collection;
import java.util.List;

/**
 * Strategy interface for searching books.
 */
public interface SearchStrategy {
    /**
     * Searches for books matching the given query.
     *
     * @param books The collection of books to search through.
     * @param query The search query.
     * @return A list of books matching the query.
     */
    List<Book> search(Collection<Book> books, String query);
}
