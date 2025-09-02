package com.zeon.movielist.repo;


//This is a dedicated client class responsible only for talking to TMDb API.
//keeps HTTP details separated from business logic. Makes code cleaner, more testable, and maintainable.

import com.zeon.movielist.dto.MovieDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TmdbClient {

    private final WebClient webClient;

    @Value("${tmdb.api.key}")
    private String apiKey; // store your API key in application.properties

    public TmdbClient(@Value("${tmdb.api.url}") String baseUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public List<MovieDto> getPopularMovies() {
        Mono<TmdbResponse> responseMono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/upcoming")
                        .queryParam("api_key", apiKey)
                        .queryParam("language", "en-US")
                        .queryParam("page", 1)
                        .build())
//                        .queryParam("api_key", apiKey)
                .retrieve()
                .bodyToMono(TmdbResponse.class);

        TmdbResponse response = responseMono.block(); // blocking call for simplicity

        if (response == null || response.getResults() == null) return List.of();


        // Map API response to MovieDto
        return response.getResults().stream()
                .map(r -> MovieDto.builder()
                        .title(r.getTitle())
                        .overview(r.getOverview())
                        .posterPath(r.getPosterPath())
                        .releaseDate(r.getReleaseDate())
                        .rating(r.getVoteAverage())
                        .build())
                .collect(Collectors.toList());
    }

    // Inner static class to map TMDb response
    private static class TmdbResponse {
        private List<MovieResult> results;

        public List<MovieResult> getResults() {
            return results;
        }

        public void setResults(List<MovieResult> results) {
            this.results = results;
        }
    }

    private static class MovieResult {
        private String title;
        private String overview;
        private String poster_path;
        private String release_date;
        private double vote_average;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getOverview() { return overview; }
        public void setOverview(String overview) { this.overview = overview; }

        public String getPosterPath() { return poster_path; }
        public void setPosterPath(String poster_path) { this.poster_path = poster_path; }

        public String getReleaseDate() { return release_date; }
        public void setReleaseDate(String release_date) { this.release_date = release_date; }

        public double getVoteAverage() { return vote_average; }
        public void setVoteAverage(double vote_average) { this.vote_average = vote_average; }
    }
}
