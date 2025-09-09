package com.zeon.movielist.repo;

import com.zeon.movielist.model.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    Optional<Watchlist> findByTmdbId(Long tmdbId);
    boolean existsByTmdbId(Long tmdbId);


}
