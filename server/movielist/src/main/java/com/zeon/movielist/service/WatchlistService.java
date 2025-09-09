package com.zeon.movielist.service;

import com.zeon.movielist.dto.WatchlistDto;

import java.util.List;

public interface WatchlistService {
    WatchlistDto addMovie(Long tmdbId);   // Add a movie to watchlist (expects only TMDb ID)

//    WatchlistDto addMovie(WatchlistDto watchlistDto); //expects a whole DTO object with more details (id, title, poster, etc.).

    List<WatchlistDto> getAllMovies();                 // Get all movies in watchlist
    WatchlistDto getMovieById(Long id);               // Get a movie by its ID
    WatchlistDto updateMovie(Long id, WatchlistDto watchlistDto); // Update movie details
    void deleteMovie(Long id);                        // Remove movie from watchlist
    void clearWatchlist();

}

