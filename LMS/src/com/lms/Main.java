package com.lms;

import com.lms.core.Branch;
import com.lms.models.Book;
import com.lms.models.Patron;
import com.lms.recommendation.RecommendationEngine;
import com.lms.search.SearchByAuthor;
import com.lms.search.SearchByTitle;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Starting Library Management System Demo ===");

        // 1. Core Requirements: Book and Patron Management
        Branch mainBranch = new Branch("Main Downtown Library");
        Branch northBranch = new Branch("Northside Library");

        Book book1 = new Book("Effective Java", "Joshua Bloch", "978-0134685991", 2017);
        Book book2 = new Book("Clean Code", "Robert C. Martin", "978-0132350884", 2008);
        Book book3 = new Book("Java Concurrency in Practice", "Brian Goetz", "978-0321349606", 2006);
        Book book4 = new Book("Refactoring", "Martin Fowler", "978-0134757599", 2018);

        mainBranch.getBranchLibrary().addBook(book1);
        mainBranch.getBranchLibrary().addBook(book2);
        mainBranch.getBranchLibrary().addBook(book3);

        northBranch.getBranchLibrary().addBook(book4);

        Patron patron1 = new Patron("P001", "Alice Smith");
        Patron patron2 = new Patron("P002", "Bob Jones");

        mainBranch.getBranchLibrary().addPatron(patron1);
        mainBranch.getBranchLibrary().addPatron(patron2);

        // 2. Search Functionality (Strategy Pattern)
        System.out.println("\n--- Searching Books in Main Branch ---");
        List<Book> searchResults = mainBranch.getBranchLibrary().searchBooks(new SearchByTitle(), "Java");
        System.out.println("Search By Title 'Java': " + searchResults.size() + " found.");

        searchResults = mainBranch.getBranchLibrary().searchBooks(new SearchByAuthor(), "Martin");
        System.out.println("Search By Author 'Martin': " + searchResults.size() + " found.");

        // 3. Lending Process and Reservation (Observer Pattern)
        System.out.println("\n--- Lending & Reservation ---");
        // Alice checks out 'Effective Java'
        mainBranch.getBranchLibrary().checkoutBook("978-0134685991", "P001");

        // Bob tries to check out the same book and fails, so he reserves it
        boolean bobSuccess = mainBranch.getBranchLibrary().checkoutBook("978-0134685991", "P002");
        if (!bobSuccess) {
            System.out.println("Bob is reserving the book.");
            patron2.addReservation(book1);
        }

        // Alice returns the book, triggering notification to Bob
        mainBranch.getBranchLibrary().returnBook("978-0134685991");

        // 4. Multi-branch Support
        System.out.println("\n--- Branch Transfers ---");
        System.out.println("North Branch has " + northBranch.getBranchLibrary().getAllBooks().size() + " books.");
        System.out.println("Transferring 'Clean Code' to North Branch...");
        mainBranch.transferBook("978-0132350884", northBranch);
        System.out.println(
                "Main Branch has 'Clean Code': " + mainBranch.getBranchLibrary().getAllBooks().contains(book2));
        System.out.println(
                "North Branch has 'Clean Code': " + northBranch.getBranchLibrary().getAllBooks().contains(book2));

        // 5. Recommendation System (Strategy Pattern extension)
        System.out.println("\n--- Generating Recommendations ---");
        // Force some history for Alice to test recommendations
        // Alice borrowed Effective Java (Joshua Bloch). Let's add another Joshua Bloch
        // book to main branch
        Book book5 = new Book("Java Puzzlers", "Joshua Bloch", "978-0321336781", 2005);
        mainBranch.getBranchLibrary().addBook(book5);

        RecommendationEngine engine = new RecommendationEngine();
        List<Book> allAvailable = new ArrayList<>(mainBranch.getBranchLibrary().getAllBooks());
        List<Book> recommended = engine.recommendBasedOnHistory(patron1, allAvailable);

        System.out.println("Recommendations for Alice (who borrowed Joshua Bloch):");
        for (Book b : recommended) {
            System.out.println("- " + b.getTitle() + " by " + b.getAuthor());
        }

        System.out.println("\n=== Demo Completed ===");
    }
}
