package com.zeon.movielist.model;

import jakarta.persistence.*;
import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "watchlist")
@Data   // generates getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Watchlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-generate unique id
    private Long id;

    private String title;
    private String overview;
    private String posterPath;
    private String releaseDate;

    // you can add extra fields that are unique to your app
    private boolean watched;

}
