package com.tp.tradexcelsior.repo;

import com.tp.tradexcelsior.entity.User;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepo extends MongoRepository<User, String> {
  Optional<User> findByEmail(String email);
  Optional<User> findByMobileNumber(String mobileNumber);

  Page<User> findAll(Pageable pageable);
  long countBySubscriptionEndDateAfter(LocalDate date);
}
