package com.brangelov.junitapp.entities;

import java.time.LocalDateTime;

public class Viewing extends Entity {

    public Viewing() {
    }

    public Viewing(String movieId, int hallNumber, LocalDateTime airing) {
        this.movieId = movieId;
        this.hallNumber = hallNumber;
        this.airing = airing;
    }

    private String movieId;

    private int hallNumber;

    private LocalDateTime airing;

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public int getHallNumber() {
        return hallNumber;
    }

    public void setHallNumber(int hallNumber) {
        this.hallNumber = hallNumber;
    }

    public LocalDateTime getAiring() {
        return airing;
    }

    public void setAiring(LocalDateTime airing) {
        this.airing = airing;
    }
}
