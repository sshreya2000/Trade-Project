package com.tp.tradexcelsior.service.admin.impl;

import com.tp.tradexcelsior.dto.response.BookDTO;
import com.tp.tradexcelsior.entity.Book;
import com.tp.tradexcelsior.exception.BookNotFoundException;
import com.tp.tradexcelsior.exception.DuplicateBookException;
import com.tp.tradexcelsior.repo.BookRepository;
import com.tp.tradexcelsior.service.admin.IAdminBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AdminBookService implements IAdminBookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> getAllBooks() {
        log.info("Fetching all books from the database");
        List<Book> books = bookRepository.findAll();
        log.info("Total books found: {}", books.size());
        return books;
    }

    @Override
    public BookDTO getBookById(String id) {
        log.info("Fetching book with ID: {}", id);
        return bookRepository.findById(id)
                .map(book -> {
                    log.info("Book found: {}", book.getName());
                    return new BookDTO(book);
                })
                .orElseThrow(() -> {
                    log.error("Book with ID {} not found", id);
                    return new BookNotFoundException("Book with ID " + id + " not found.");
                });
    }


    @Override
    public Book createBook(BookDTO bookDTO) {
        log.info("Creating new book: {}", bookDTO.getName());

        bookRepository.findByNameAndDescription(bookDTO.getName(), bookDTO.getDescription())
                .ifPresent(existingBook -> {
                    log.warn("Duplicate book found: {}", bookDTO.getName());
                    throw new DuplicateBookException("A book with the same name and description already exists.");
                });

        Book book = new Book();
        book.setName(bookDTO.getName());
        book.setDescription(bookDTO.getDescription());
        book.setTagLine(bookDTO.getTagLine());
        book.setLinkToBuyBook(bookDTO.getLinkToBuyBook());
        book.setButtonName(bookDTO.getButtonName());
        book.setImageUrl(bookDTO.getImageUrl());

        Book savedBook = bookRepository.save(book);
        log.info("Book created successfully with ID: {}", savedBook.getId());
        return savedBook;
    }

    @Override
    public Map<String, Object> updateBook(String id, BookDTO updatedBookDTO) {
        log.info("Updating book with ID: {}", id);

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cannot update. Book with ID {} not found", id);
                    return new BookNotFoundException("Cannot update. Book with ID " + id + " not found.");
                });

        // Update book details
        existingBook.setName(updatedBookDTO.getName());
        existingBook.setDescription(updatedBookDTO.getDescription());
        existingBook.setTagLine(updatedBookDTO.getTagLine());
        existingBook.setLinkToBuyBook(updatedBookDTO.getLinkToBuyBook());
        existingBook.setButtonName(updatedBookDTO.getButtonName());
        existingBook.setImageUrl(updatedBookDTO.getImageUrl());

        // Save updated book
        Book savedBook = bookRepository.save(existingBook);
        log.info("Book updated successfully with ID: {}", savedBook.getId());

        // Prepare response with message
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Book has been updated successfully");
//        response.put("updatedBook", new BookDTO(savedBook));

        return response;
    }



    @Override
    public Map<String, Object> deleteBook(String id) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Cannot delete. Book with ID " + id + " not found."));

        // Mark the book as deleted
        existingBook.setDeleted(true);
        bookRepository.save(existingBook); // Save the updated book (soft delete)

        // Capture book details
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Book deleted successfully");
//        response.put("bookId", existingBook.getId());
//        response.put("bookName", existingBook.getName());
        return response;
    }

    @Override
    public List<Book> searchBooks(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Book name cannot be empty");
        }

        List<Book> books = bookRepository.findByNameContainingIgnoreCase(name);
        if (books.isEmpty()) {
            throw new BookNotFoundException("No books found for name: " + name);
        }
        return books;
    }
}