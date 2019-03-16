package com.brangelov.junitapp.services;

import com.brangelov.junitapp.entities.Movie;

import java.util.List;

public interface MovieService extends BaseService<Movie> {
    /**
     * Get all available movies, ordered by air date.
     *
     * @return All available movies. Never null.
     */
    @Override
    List<Movie> getAll();
}
