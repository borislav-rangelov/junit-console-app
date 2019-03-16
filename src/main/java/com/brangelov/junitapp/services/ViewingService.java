package com.brangelov.junitapp.services;

import com.brangelov.junitapp.entities.Viewing;

import java.util.List;

public interface ViewingService extends BaseService<Viewing> {
    List<Viewing> getByMovieId(String movieId);
}
