package com.lms.search;

import com.lms.models.Book;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy to search books by exact ISBN match.
 */
public class SearchByISBN implements SearchStrategy {
    @Override
    public List<Book> search(Collection<Book> books, String query) {
        if (query == null || query.trim().isEmpty()) return List.of();
        
        return books.stream()
                .filter(book -> book.getIsbn().equals(query.trim()))
                .collect(Collectors.toList());
    }
}
