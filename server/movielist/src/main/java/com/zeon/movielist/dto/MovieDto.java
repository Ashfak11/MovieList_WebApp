package com.zeon.movielist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDto {
//    @Column(name = "tmdb_id")//, nullable = false)
//    private Long tmdbId;
    private String title;
    private String overview;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("vote_average")
    private double rating;
}
