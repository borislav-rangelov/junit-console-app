package com.brangelov.junitapp.services;

import com.brangelov.junitapp.entities.Hall;
import com.brangelov.junitapp.entities.Movie;
import com.brangelov.junitapp.entities.Viewing;
import com.brangelov.junitapp.storage.Storage;

import java.util.*;
import java.util.stream.Collectors;

public class ViewingServiceImpl extends BaseServiceImpl<Viewing> implements ViewingService {

    private final MovieService movieService;
    private final HallService hallService;

    public ViewingServiceImpl(Storage<Viewing> storage, MovieService movieService, HallService hallService) {
        super(storage);

        assert movieService != null;
        assert hallService != null;

        this.movieService = movieService;
        this.hallService = hallService;
    }

    @Override
    public List<Viewing> getByMovieId(String movieId) {
        return getAll().stream()
                .filter(v -> v.getMovieId().equals(movieId))
                .sorted(Comparator.comparing(Viewing::getAiring))
                .collect(Collectors.toList());
    }

    @Override
    public void save(Viewing entity) {

        if (entity.getMovieId() == null) {
            throw new IllegalArgumentException("Movie id is null");
        }

        Movie movie = movieService.getById(entity.getMovieId());
        if (movie == null) {
            throw new IllegalArgumentException("Movie id is invalid");
        }

        Hall hall = hallService.getByNumber(entity.getHallNumber());
        if (hall == null) {
            throw new IllegalArgumentException("Hall number is invalid");
        }

        super.save(entity);
    }
}
