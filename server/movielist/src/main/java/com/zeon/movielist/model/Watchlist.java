package com.zeon.movielist.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "watchlist", uniqueConstraints = @UniqueConstraint(columnNames = {"tmdb_id"}))
@Data   // generates getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Watchlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-generate unique id
    private Long id;

    @Column(name = "tmdb_id", nullable = false)
    private Long tmdbId;

    private String title;
    private String overview;
    private String posterPath;
    private String releaseDate;
    private Double rating;

    // you can add extra fields that are unique to your app
    private boolean watched;


//    private Long id;
//    private String title;
//    private String overview;
//    private String posterPath;
//    private String releaseDate;
//    private boolean watched;
}
