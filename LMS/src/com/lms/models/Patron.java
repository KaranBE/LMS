package com.lms.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Represents a library member (Patron) who can borrow books.
 */
public class Patron {
    private static final Logger logger = Logger.getLogger(Patron.class.getName());

    private String memberId;
    private String name;
    private List<Book> borrowingHistory;
    private List<Book> reservedBooks; // For Observer Pattern

    public Patron(String memberId, String name) {
        this.memberId = memberId;
        this.name = name;
        this.borrowingHistory = new ArrayList<>();
        this.reservedBooks = new ArrayList<>();
        logger.info("New patron added: " + name + " (ID: " + memberId + ")");
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        logger.fine("Updated name for patron ID " + memberId + " to " + name);
    }

    public List<Book> getBorrowingHistory() {
        return borrowingHistory;
    }

    public void addToHistory(Book book) {
        borrowingHistory.add(book);
        logger.fine("Added book " + book.getIsbn() + " to history of patron " + memberId);
    }

    public List<Book> getReservedBooks() {
        return reservedBooks;
    }

    public void addReservation(Book book) {
        if (!reservedBooks.contains(book)) {
            reservedBooks.add(book);
            logger.info("Patron " + memberId + " reserved book " + book.getIsbn());
        }
    }

    public void removeReservation(Book book) {
        reservedBooks.remove(book);
        logger.info("Patron " + memberId + " removed reservation for book " + book.getIsbn());
    }

    /**
     * Called when a reserved book becomes available (Observer Pattern callback).
     * @param message The notification message.
     */
    public void notify(String message) {
        System.out.println("Notification for " + name + " (" + memberId + "): " + message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patron patron = (Patron) o;
        return memberId.equals(patron.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }

    @Override
    public String toString() {
        return "Patron{" +
                "memberId='" + memberId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
