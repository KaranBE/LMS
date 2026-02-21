package com.lms.recommendation;

import com.lms.models.Book;
import com.lms.models.Patron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Strategy/Factory based recommendation engine.
 */
public class RecommendationEngine {
    private static final Logger logger = Logger.getLogger(RecommendationEngine.class.getName());

    /**
     * Recommends books based on the author of books the patron previously borrowed.
     * 
     * @param patron            The patron to generate recommendations for.
     * @param allAvailableBooks The full inventory of the library or branch.
     * @return A list of recommended books.
     */
    public List<Book> recommendBasedOnHistory(Patron patron, List<Book> allAvailableBooks) {
        if (patron == null || allAvailableBooks == null || allAvailableBooks.isEmpty()) {
            return new ArrayList<>();
        }

        List<Book> history = patron.getBorrowingHistory();
        if (history.isEmpty()) {
            logger.info("Patron has no history. Returning default recommendations.");
            // If no history, just return some available books
            return allAvailableBooks.subList(0, Math.min(allAvailableBooks.size(), 3));
        }

        // Count frequency of authors in history
        Map<String, Integer> authorCounts = new HashMap<>();
        for (Book book : history) {
            authorCounts.put(book.getAuthor(), authorCounts.getOrDefault(book.getAuthor(), 0) + 1);
        }

        // Find the most frequent author
        String favoriteAuthor = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : authorCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                favoriteAuthor = entry.getKey();
            }
        }

        logger.info("Most frequent author for patron " + patron.getMemberId() + " is " + favoriteAuthor);

        // Find available books by this author
        List<Book> recommendations = new ArrayList<>();
        for (Book book : allAvailableBooks) {
            if (book.isAvailable() && book.getAuthor().equals(favoriteAuthor) && !history.contains(book)) {
                recommendations.add(book);
            }
        }

        return recommendations;
    }
}
