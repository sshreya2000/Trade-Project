package com.tp.tradexcelsior.repo;

import com.tp.tradexcelsior.entity.SuccessStories;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SuccessStoriesRepo extends MongoRepository<SuccessStories, String> {
  Optional<SuccessStories> findByUserName(String userName);
}
