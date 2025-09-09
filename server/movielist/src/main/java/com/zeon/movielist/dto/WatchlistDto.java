package com.zeon.movielist.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder   // handy if you want to build objects step by step
public class WatchlistDto {
    private Long tmdbId;
    private String title;
    private String overview;
    private String posterPath;
    private String releaseDate;
    private Double rating;
    private boolean watched;
}

