package com.zeon.movielist.controller;

import com.zeon.movielist.dto.WatchlistDto;
import com.zeon.movielist.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService;

    @PostMapping
    public WatchlistDto addMovie(@RequestBody WatchlistDto watchlistDto) {
        return watchlistService.addMovie(watchlistDto);
    }

    @GetMapping
    public List<WatchlistDto> getAllMovies() {
        return watchlistService.getAllMovies();
    }

    @GetMapping("/{id}")
    public WatchlistDto getMovieById(@PathVariable Long id) {
        return watchlistService.getMovieById(id);
    }

    @PutMapping("/{id}")
    public WatchlistDto updateMovie(@PathVariable Long id, @RequestBody WatchlistDto watchlistDto) {
        return watchlistService.updateMovie(id, watchlistDto);
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        watchlistService.deleteMovie(id);
    }
}
