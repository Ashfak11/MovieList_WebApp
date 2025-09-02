package com.zeon.movielist.service;

import com.zeon.movielist.dto.MovieDto;
import com.zeon.movielist.repo.TmdbClient;
import org.springframework.stereotype.Service;

import java.util.List;


//will call TmdbClient to get the raw Data
@Service
public class MovieService {
    private final TmdbClient tmdbClient;

    public MovieService(TmdbClient tmdbClient) {
        this.tmdbClient = tmdbClient;
    }

    public List<MovieDto> getPopularMovies() {
        return tmdbClient.getPopularMovies();
    }
}
