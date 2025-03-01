package com.tp.tradexcelsior.repo;

import com.tp.tradexcelsior.entity.CoreWatchlist;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CoreWatchlistRepo extends MongoRepository<CoreWatchlist, String> {

  Optional<CoreWatchlist> findByCompany(String company);
  Page<CoreWatchlist> findAll(Pageable pageable);
}
