package com.tp.tradexcelsior.repo;

import com.tp.tradexcelsior.entity.Checklist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChecklistRepository extends MongoRepository<Checklist, String>  {
    boolean existsByDescriptionAndButtonName(String description, String buttonName);
    Optional<Checklist> findByDescriptionAndButtonNameAndLink(String description, String buttonName, String link);
}
