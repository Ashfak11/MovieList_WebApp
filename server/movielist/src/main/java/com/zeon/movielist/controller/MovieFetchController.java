package com.zeon.movielist.controller;


import com.zeon.movielist.dto.MovieDto;
import com.zeon.movielist.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//Exposes endpoints to frontend and calls the service layer
@RestController
public class MovieFetchController {
    private final MovieService movieService;

    public MovieFetchController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/api/movie/upcoming")
    public List<MovieDto> getPopularMovies() {
        return movieService.getPopularMovies();
    }
}
