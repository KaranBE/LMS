package com.lms.search;

import com.lms.models.Book;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy to search books by author (case-insensitive substring match).
 */
public class SearchByAuthor implements SearchStrategy {
    @Override
    public List<Book> search(Collection<Book> books, String query) {
        if (query == null || query.trim().isEmpty()) return List.of();
        String lowerCaseQuery = query.toLowerCase();
        
        return books.stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());
    }
}
