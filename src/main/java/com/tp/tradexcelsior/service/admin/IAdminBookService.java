package com.tp.tradexcelsior.service.admin;

import com.tp.tradexcelsior.dto.response.BookDTO;
import com.tp.tradexcelsior.entity.Book;

import java.util.List;
import java.util.Map;

public interface IAdminBookService {
    List<Book> getAllBooks();
    BookDTO getBookById(String id);
    Book createBook(BookDTO bookDTO);
    Map<String, Object> updateBook(String id, BookDTO updatedBookDTO);
    Map<String, Object> deleteBook(String id);
    List<Book> searchBooks(String name);

}
