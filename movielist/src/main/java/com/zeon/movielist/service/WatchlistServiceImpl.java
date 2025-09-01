package com.zeon.movielist.service;

import com.zeon.movielist.dto.WatchlistDto;
import com.zeon.movielist.mapper.WatchlistMapper;
import com.zeon.movielist.model.Watchlist;
import com.zeon.movielist.repo.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WatchlistServiceImpl implements WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepository;

    @Override
    public WatchlistDto addMovie(WatchlistDto watchlistDto) {
        Watchlist entity = WatchlistMapper.toEntity(watchlistDto); // Convert DTO -> Entity
        Watchlist saved = watchlistRepository.save(entity);        // Save to DB
        return WatchlistMapper.toDto(saved);                        // Convert back Entity -> DTO
    }

    @Override
    public List<WatchlistDto> getAllMovies() {
        return watchlistRepository.findAll()
                .stream()
                .map(WatchlistMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public WatchlistDto getMovieById(Long id) {
        Watchlist entity = watchlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return WatchlistMapper.toDto(entity);
    }

    @Override
    public WatchlistDto updateMovie(Long id, WatchlistDto watchlistDto) {
        Watchlist existing = watchlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        existing.setTitle(watchlistDto.getTitle());
        existing.setOverview(watchlistDto.getOverview());
        existing.setPosterPath(watchlistDto.getPosterPath());
        existing.setReleaseDate(watchlistDto.getReleaseDate());
        existing.setWatched(watchlistDto.isWatched());

        Watchlist updated = watchlistRepository.save(existing);
        return WatchlistMapper.toDto(updated);
    }

    @Override
    public void deleteMovie(Long id) {
        watchlistRepository.deleteById(id);
    }
}

