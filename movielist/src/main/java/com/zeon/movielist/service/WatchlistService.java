package com.zeon.movielist.service;

import com.zeon.movielist.dto.WatchlistDto;

import java.util.List;

public interface WatchlistService {
    WatchlistDto addMovie(WatchlistDto watchlistDto);   // Add a movie to watchlist
    List<WatchlistDto> getAllMovies();                 // Get all movies in watchlist
    WatchlistDto getMovieById(Long id);               // Get a movie by its ID
    WatchlistDto updateMovie(Long id, WatchlistDto watchlistDto); // Update movie details
    void deleteMovie(Long id);                        // Remove movie from watchlist
}
