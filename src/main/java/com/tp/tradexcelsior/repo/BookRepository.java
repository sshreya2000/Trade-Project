package com.tp.tradexcelsior.repo;

import com.tp.tradexcelsior.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByNameContainingIgnoreCase(String name);
    boolean existsByNameAndDescription(String name, String description);
    Optional<Book> findTopByOrderByIdDesc();
    Optional<Book> findByNameAndDescription(String name, String description);
    List<Book> findByIsDeletedFalse();
}