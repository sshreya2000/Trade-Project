package com.tp.tradexcelsior.repo;

import com.tp.tradexcelsior.entity.Reference;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenceRepository extends MongoRepository<Reference, String> {

    boolean existsByNameAndType(String name, String type);
}
