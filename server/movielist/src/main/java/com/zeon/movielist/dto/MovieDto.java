package com.zeon.movielist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDto {
    private String title;
    private String overview;
    private String posterPath;
    private String releaseDate;
    private double rating;
}
