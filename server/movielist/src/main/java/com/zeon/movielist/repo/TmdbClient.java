package com.zeon.movielist.repo;


//This is a dedicated client class responsible only for talking to TMDb API.
//keeps HTTP details separated from business logic. Makes code cleaner, more testable, and maintainable.

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zeon.movielist.dto.MovieDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TmdbClient {

    private final WebClient webClient; //Spring’s non-blocking HTTP client (modern replacement for RestTemplate

    @Value("${tmdb.api.key}")
    private String apiKey; // API key is stored in application.properties

//  the builder library is doing the heavy lifting of combining base + path + query parameters into one valid HTTP URL.
    //baseUrl is injected from application.properties
    public TmdbClient(@Value("${tmdb.api.url}") String baseUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build(); //baseUrl(baseUrl) → stores the root TMDb URL (https://api.themoviedb.org/3)
    }

    public List<MovieDto> getPopularMovies() {
        Mono<TmdbResponse> responseMono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/upcoming")
                        .queryParam("api_key", apiKey)
                        .queryParam("language", "en-US")
                        .queryParam("page", 1)
                        .build())
//  WebClient automatically appends the path (/movie/upcoming) to the base URL and adds query params.

                        .retrieve()//after the URL has been built this sends the HTTP GET reqs to the combined URL
                        .bodyToMono(TmdbResponse.class); //tells weBClient the response would be in JSON you have to translate it into TmdbResponse.class (.bodyToMono() only defines how to convert the JSON, but the fetching doesn’t happen until you subscribe, like with .block().)

        TmdbResponse response = responseMono.block(); // blocking call for simplicity so basically it waits for the response to arrive first before actually presenting the TmdbResponse

//        If the API fails or gives no results → return an empty list instead of null.
        if (response == null || response.getResults() == null) return List.of();


        // Map raw API response to MovieDto
        return response.getResults().stream()
                .map(r -> MovieDto.builder() //maps to movieDto
                        .title(r.getTitle())
                        .overview(r.getOverview())
                        .posterPath(r.getPosterPath())
                        .releaseDate(r.getReleaseDate())
                        .rating(r.getVoteAverage())
                        .build())
                .collect(Collectors.toList());
    }

    public MovieDto getMovieById(Long movieId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/movie/{id}")
                        .queryParam("api_key", apiKey)
                        .queryParam("language", "en-US")
                        .build(movieId))
                .retrieve()
                .bodyToMono(MovieDto.class)
                .block();
    }


    // Inner static class to map TMDb response(Wrapper)
    // exact structure of TMDb JSON response
    private static class TmdbResponse  {
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
        @JsonProperty("poster_path") // Used to explicitly match DTO field names and TMDb’s JSON field names.
        private String posterPath;
        @JsonProperty("release_date")
        private String release_date;
        @JsonProperty("vote_average")
        private double vote_average;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getOverview() { return overview; }
        public void setOverview(String overview) { this.overview = overview; }

        public String getPosterPath() {
            if (posterPath != null) {
                return "https://image.tmdb.org/t/p/w500" + posterPath;
            }
            return null;
        }

        public String getReleaseDate() { return release_date; }
        public void setReleaseDate(String release_date) { this.release_date = release_date; }

        public double getVoteAverage() { return vote_average; }
        public void setVoteAverage(double vote_average) { this.vote_average = vote_average; }
    }
}
