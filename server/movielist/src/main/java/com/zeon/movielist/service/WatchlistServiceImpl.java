package com.zeon.movielist.service;

import com.zeon.movielist.dto.MovieDto;
import com.zeon.movielist.dto.WatchlistDto;
import com.zeon.movielist.mapper.WatchlistMapper;
import com.zeon.movielist.model.Watchlist;
import com.zeon.movielist.repo.TmdbClient;
import com.zeon.movielist.repo.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor //same thing as Autowired? i was mentioning it 2 times? so i was mixing "Styles"
public class WatchlistServiceImpl implements WatchlistService {
    //    @Autowired
    private final WatchlistRepository watchlistRepository; //used for DB operations
    private final TmdbClient tmdbClient; // injecting client (used to call TMDb API when adding a movie)

    @Override
    public WatchlistDto addMovie(Long tmdbId) {
        // 1. prevent duplicates
        if (watchlistRepository.existsByTmdbId(tmdbId)) {
            throw new RuntimeException("Movie already in watchlist");
        }
        // 1. Fetch movie info from TMDb using the tmdbId from DTO
        MovieDto movieFromApi = tmdbClient.getMovieById(tmdbId);

        // 3. map MovieDto → Watchlist entity
        Watchlist entity = Watchlist.builder()
                .tmdbId(tmdbId)
                .title(movieFromApi.getTitle())
                .overview(movieFromApi.getOverview())
                .posterPath(movieFromApi.getPosterPath())
                .releaseDate(movieFromApi.getReleaseDate())
                .rating(movieFromApi.getRating())
                .watched(false) // default
                .build();

        // 3. Save to DB
        Watchlist saved = watchlistRepository.save(entity);

        // 4. Convert back to DTO and return
        return WatchlistMapper.toDto(saved);
    }

    //    Get All Movies (consider pagination)
    @Override
    public List<WatchlistDto> getAllMovies() {
        return watchlistRepository.findAll()//Service calls the repository (findAll() → fetches all rows from DB)
                .stream()
                .map(WatchlistMapper::toDto) //Each Watchlist entity is mapped to WatchlistDto (because we don’t want to expose DB objects directly
                .collect(Collectors.toList());
    }

    @Override
    public WatchlistDto getMovieById(Long id) {
        Watchlist entity = watchlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return WatchlistMapper.toDto(entity);
    }

    @Override
    public WatchlistDto updateMovie(Long id, WatchlistDto watchlistDto) {
        Watchlist existing = watchlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

//        existing.setTitle(watchlistDto.getTitle());
//        existing.setOverview(watchlistDto.getOverview());
//        existing.setPosterPath(watchlistDto.getPosterPath());
//        existing.setReleaseDate(watchlistDto.getReleaseDate());
        existing.setWatched(watchlistDto.isWatched());

        Watchlist updated = watchlistRepository.save(existing);
        return WatchlistMapper.toDto(updated);
    }

    @Override
    public void deleteMovie(Long id) {
        watchlistRepository.deleteById(id);
    }


    @Override
    public void clearWatchlist() {
        watchlistRepository.deleteAll();
    }
}



// 1. prevent duplicates
//        if (watchlistRepository.existsByTmdbId(tmdbId)) {
//            throw new RuntimeException("Movie already in watchlist");
//        }
//
//        // 2. fetch from TMDb API
//        MovieDto movieDto = tmdbClient.getMovieById(tmdbId);
//
//        // 3. map MovieDto → Watchlist entity
//        Watchlist entity = Watchlist.builder()
//                .tmdbId(tmdbId)
//                .title(movieDto.getTitle())
//                .overview(movieDto.getOverview())
//                .posterPath(movieDto.getPosterPath())
//                .releaseDate(movieDto.getReleaseDate())
//                .rating(movieDto.getRating())
//                .watched(false) // default
//                .build();
//
//        // 4. save entity
//        Watchlist saved = watchlistRepository.save(entity);
//
//        // 5. map back to DTO
//        return WatchlistMapper.toDto(saved);
//    }



//    @Override
//    public WatchlistDto addMovie(WatchlistDto watchlistDto) {
//        Watchlist entity = WatchlistMapper.toEntity(watchlistDto); // Convert DTO -> Entity
//        Watchlist saved = watchlistRepository.save(entity);        // Save to DB
//        return WatchlistMapper.toDto(saved);                        // Convert back Entity -> DTO
//    }

