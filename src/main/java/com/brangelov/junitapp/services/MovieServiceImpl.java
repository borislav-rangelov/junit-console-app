package com.brangelov.junitapp.services;

import com.brangelov.junitapp.entities.Movie;
import com.brangelov.junitapp.storage.Storage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MovieServiceImpl extends BaseServiceImpl<Movie> implements MovieService {

    public MovieServiceImpl(Storage<Movie> storage) {
        super(storage);
    }

    @Override
    public List<Movie> getAll() {
        return super.getAll().stream()
                .sorted(Comparator.comparing(Movie::getAirDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void save(Movie entity) {

        if (entity.getTitle() == null || entity.getTitle().length() == 0) {
            throw new IllegalArgumentException("Movie title is null");
        }

        if (entity.getAirDate() == null) {
            throw new IllegalArgumentException("Movie requires an air date");
        }

        super.save(entity);
    }
}
