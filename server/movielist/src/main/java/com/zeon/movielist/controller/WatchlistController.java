package com.zeon.movielist.controller;

import com.zeon.movielist.dto.WatchlistDto;
import com.zeon.movielist.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //This class will handle HTTP requests and return data (usually JSON)
@RequestMapping("/api/watchlist") //Global URl thing kind of
@RequiredArgsConstructor
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService; //private cause it keeps things encapsulated

    @PostMapping("/add")
    public WatchlistDto addMovie(@RequestBody WatchlistDto watchlistDto) {
        return watchlistService.addMovie(watchlistDto.getTmdbId());
    }

    @GetMapping("/all")
    public List<WatchlistDto> getAllMovies() {
        return watchlistService.getAllMovies();
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        watchlistService.deleteMovie(id);
    }

    @DeleteMapping("/clear")
    public void clearWatchlist() {
        watchlistService.clearWatchlist();
    }

    @PutMapping("/{id}")
    public WatchlistDto updateMovie(@PathVariable Long id, @RequestBody WatchlistDto watchlistDto) {
        return watchlistService.updateMovie(id, watchlistDto);
    }


//    @GetMapping("/{id}")
//    public WatchlistDto getMovieById(@PathVariable Long id) {
//        return watchlistService.getMovieById(id);
//    }
//
}

