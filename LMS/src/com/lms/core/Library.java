package com.lms.core;

import com.lms.models.Book;
import com.lms.models.Patron;
import com.lms.search.SearchStrategy;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Manages the library's inventory, patrons, and lending processes.
 */
public class Library {
    private static final Logger logger = Logger.getLogger(Library.class.getName());

    private Map<String, Book> inventory; // Key: ISBN
    private Map<String, Patron> patrons; // Key: Member ID

    public Library() {
        this.inventory = new HashMap<>();
        this.patrons = new HashMap<>();
        logger.info("Library system initialized.");
    }

    // --- Book Management ---

    public void addBook(Book book) {
        if (inventory.containsKey(book.getIsbn())) {
            logger.warning("Book with ISBN " + book.getIsbn() + " already exists.");
            return;
        }
        inventory.put(book.getIsbn(), book);
        logger.info("Added book to library: " + book.getTitle());
    }

    public void removeBook(String isbn) {
        if (inventory.remove(isbn) != null) {
            logger.info("Removed book with ISBN: " + isbn);
        } else {
            logger.warning("Attempted to remove non-existent book with ISBN: " + isbn);
        }
    }

    public void updateBook(Book updatedBook) {
        if (inventory.containsKey(updatedBook.getIsbn())) {
            inventory.put(updatedBook.getIsbn(), updatedBook);
            logger.info("Updated book: " + updatedBook.getTitle());
        } else {
            logger.warning("Attempted to update non-existent book with ISBN: " + updatedBook.getIsbn());
        }
    }

    public Collection<Book> getAllBooks() {
        return inventory.values();
    }

    // --- Patron Management ---

    public void addPatron(Patron patron) {
        if (patrons.containsKey(patron.getMemberId())) {
            logger.warning("Patron with ID " + patron.getMemberId() + " already exists.");
            return;
        }
        patrons.put(patron.getMemberId(), patron);
        logger.info("Added patron to library: " + patron.getName());
    }

    public void removePatron(String memberId) {
        if (patrons.remove(memberId) != null) {
            logger.info("Removed patron with ID: " + memberId);
        } else {
            logger.warning("Attempted to remove non-existent patron with ID: " + memberId);
        }
    }

    public Patron getPatron(String memberId) {
        return patrons.get(memberId);
    }

    // --- Search Functionality (Strategy Pattern) ---

    public List<Book> searchBooks(SearchStrategy strategy, String query) {
        logger.info("Searching books using " + strategy.getClass().getSimpleName() + " with query: " + query);
        return strategy.search(inventory.values(), query);
    }

    // --- Lending Process ---

    public boolean checkoutBook(String isbn, String memberId) {
        Book book = inventory.get(isbn);
        Patron patron = patrons.get(memberId);

        if (book == null) {
            logger.warning("Checkout failed: Book with ISBN " + isbn + " not found.");
            return false;
        }
        if (patron == null) {
            logger.warning("Checkout failed: Patron with ID " + memberId + " not found.");
            return false;
        }

        if (book.isAvailable()) {
            book.setAvailable(false);
            patron.addToHistory(book);
            logger.info("Book '" + book.getTitle() + "' checked out by " + patron.getName());
            return true;
        } else {
            logger.info("Checkout failed: Book '" + book.getTitle() + "' is currently not available.");
            return false;
        }
    }

    public boolean returnBook(String isbn) {
        Book book = inventory.get(isbn);
        if (book == null) {
            logger.warning("Return failed: Book with ISBN " + isbn + " not found in inventory.");
            return false;
        }

        if (!book.isAvailable()) {
            book.setAvailable(true);
            logger.info("Book '" + book.getTitle() + "' has been returned.");
            notifyReservations(book);
            return true;
        } else {
            logger.warning("Return failed: Book '" + book.getTitle() + "' was not checked out.");
            return false;
        }
    }

    // --- Reservation System (Observer Pattern) ---

    private void notifyReservations(Book book) {
        for (Patron patron : patrons.values()) {
            if (patron.getReservedBooks().contains(book)) {
                patron.notify("The book '" + book.getTitle() + "' is now available!");
                patron.removeReservation(book);
                // Note: In a real system, we might only notify the first person in a queue.
                // For simplicity, we notify everyone who reserved it and remove their
                // reservation.
            }
        }
    }
}
