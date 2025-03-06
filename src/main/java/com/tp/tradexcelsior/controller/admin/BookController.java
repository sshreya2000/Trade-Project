package com.tp.tradexcelsior.controller.admin;

import com.tp.tradexcelsior.dto.response.BookDTO;
import com.tp.tradexcelsior.entity.Book;
import com.tp.tradexcelsior.exception.BookNotFoundException;
import com.tp.tradexcelsior.exception.InvalidBookException;
import com.tp.tradexcelsior.service.admin.impl.AdminBookService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Book Management", description = "APIs for managing books")
@RestController
@RequestMapping("/api/v1/admin/book")
public class BookController {

    private final AdminBookService bookService;

    public BookController(AdminBookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Get all books", description = "Fetch all available books")
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Add a new book", description = "Create a new book in the system")
    @PostMapping("/addbook")
    public ResponseEntity<?> createBook(@Valid @RequestBody BookDTO bookDTO) {
        try {
            Book savedBook = bookService.createBook(bookDTO);
            return ResponseEntity.status(201).body(savedBook);  // 201 Created
        } catch (InvalidBookException e) {
            return ResponseEntity.badRequest().body("Invalid book: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something went wrong: " + e.getMessage());
        }
    }

    @Operation(summary = "Get book by ID", description = "Fetch a book using its ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable String id) {
        try {
            BookDTO bookDTO = bookService.getBookById(id);  // ✅ Fetch BookDTO
            return ResponseEntity.ok(bookDTO);
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(404).body("Book not found: " + e.getMessage());
        } catch (IllegalArgumentException e) {  // This catches invalid ObjectId formats
            return ResponseEntity.status(400).body("Invalid book ID format: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    @Operation(summary = "Update book details", description = "Modify an existing book using its ID")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateBook(@PathVariable String id, @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDTO));
    }

    @Operation(summary = "Delete a book", description = "Soft delete a book by marking it as inactive")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteBook(@PathVariable String id) {
        Map<String, Object> response = bookService.deleteBook(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(@RequestParam String name) {
        try {
            List<Book> books = bookService.searchBooks(name);
            return ResponseEntity.ok(books);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }
}