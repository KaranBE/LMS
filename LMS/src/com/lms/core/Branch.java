package com.lms.core;

import com.lms.models.Book;

import java.util.logging.Logger;

/**
 * Represents a library branch.
 * Contains its own library inventory and supports transferring books.
 */
public class Branch {
    private static final Logger logger = Logger.getLogger(Branch.class.getName());

    private String branchName;
    private Library branchLibrary; // Use Library class to manage branch's inventory and patrons

    public Branch(String branchName) {
        this.branchName = branchName;
        this.branchLibrary = new Library();
        logger.info("Initialized Branch: " + branchName);
    }

    public String getBranchName() {
        return branchName;
    }

    public Library getBranchLibrary() {
        return branchLibrary;
    }

    /**
     * Transfers a book from this branch to another branch.
     * 
     * @param isbn         The ISBN of the book to transfer.
     * @param targetBranch The branch receiving the book.
     * @return true if successful, false otherwise.
     */
    public boolean transferBook(String isbn, Branch targetBranch) {
        // Find if book exists in this branch
        for (Book book : branchLibrary.getAllBooks()) {
            if (book.getIsbn().equals(isbn)) {
                // Must be available to transfer
                if (book.isAvailable()) {
                    branchLibrary.removeBook(isbn);
                    targetBranch.getBranchLibrary().addBook(book);
                    logger.info("Transferred book '" + book.getTitle() + "' from " + branchName + " to "
                            + targetBranch.getBranchName());
                    return true;
                } else {
                    logger.warning("Cannot transfer book '" + book.getTitle()
                            + "'. It is currently not available (checked out).");
                    return false;
                }
            }
        }
        logger.warning("Cannot transfer book ISBN " + isbn + ". Not found in branch: " + branchName);
        return false;
    }
}
