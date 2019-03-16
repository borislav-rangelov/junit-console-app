package com.brangelov.junitapp.entities;

import java.time.LocalDateTime;

public class Movie extends Entity {

    public Movie() {
    }

    public Movie(String title, String description, LocalDateTime airDate) {
        this.title = title;
        this.description = description;
        this.airDate = airDate;
    }

    private String title;

    private String description;

    private LocalDateTime airDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getAirDate() {
        return airDate;
    }

    public void setAirDate(LocalDateTime airDate) {
        this.airDate = airDate;
    }
}
