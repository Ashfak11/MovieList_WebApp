package com.zeon.movielist.mapper;

import com.zeon.movielist.dto.WatchlistDto;
import com.zeon.movielist.model.Watchlist;

public class WatchlistMapper {

    // DTO -> Entity
    public static Watchlist toEntity(WatchlistDto dto) {
        return Watchlist.builder() // if you add @Builder to Watchlist
                .id(dto.getId())
                .title(dto.getTitle())
                .overview(dto.getOverview())
                .posterPath(dto.getPosterPath())
                .releaseDate(dto.getReleaseDate())
                .watched(dto.isWatched())
                .build();
    }

    // Entity -> DTO
    public static WatchlistDto toDto(Watchlist entity) {
        return WatchlistDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .overview(entity.getOverview())
                .posterPath(entity.getPosterPath())
                .releaseDate(entity.getReleaseDate())
                .watched(entity.isWatched())
                .build();
    }
}