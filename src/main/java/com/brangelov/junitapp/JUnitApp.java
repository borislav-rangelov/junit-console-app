package com.brangelov.junitapp;

import com.brangelov.junitapp.controllers.ConsoleController;
import com.brangelov.junitapp.entities.Hall;
import com.brangelov.junitapp.entities.Movie;
import com.brangelov.junitapp.entities.Reservation;
import com.brangelov.junitapp.entities.Viewing;
import com.brangelov.junitapp.services.*;
import com.brangelov.junitapp.storage.InMemoryStorage;

import java.time.LocalDateTime;

public class JUnitApp {

    public static void main(String[] args) {

        Movie movie = new Movie("Test", "Test description",
                LocalDateTime.now().toLocalDate().atStartOfDay().minusDays(1));
        movie.generateId();

        Viewing viewing = new Viewing(
                movie.getId(),
                1,
                movie.getAirDate().toLocalDate().atTime(18, 0));
        viewing.generateId();

        Reservation reservation = new Reservation(
                "John", "Doe", "00012313", viewing.getId(), 2, 2
        );
        reservation.generateId();

        Hall hall = new Hall(1, new Hall.Seat[][]{
                new Hall.Seat[]{
                        new Hall.Seat(1, 1, null),
                        new Hall.Seat(1, 2, null),
                        new Hall.Seat(1, 3, null),
                },
                new Hall.Seat[]{
                        new Hall.Seat(2, 1, null),
                        new Hall.Seat(2, 2, null),
                        new Hall.Seat(2, 3, null),
                },
                new Hall.Seat[]{
                        new Hall.Seat(3, 1, null),
                        new Hall.Seat(3, 2, null),
                        new Hall.Seat(3, 3, null),
                        new Hall.Seat(3, 4, null),
                        new Hall.Seat(3, 5, null),
                }
        });

        MovieServiceImpl movieService = new MovieServiceImpl(new InMemoryStorage<>());
        HallServiceImpl hallService = new HallServiceImpl(new InMemoryStorage<>());
        ViewingServiceImpl viewingService = new ViewingServiceImpl(new InMemoryStorage<>(), movieService, hallService);
        ReservationService reservationService = new ReservationServiceImpl(new InMemoryStorage<>(), viewingService, hallService);

        movieService.save(movie);
        hallService.save(hall);
        viewingService.save(viewing);
        reservationService.save(reservation);

        ConsoleController controller = new ConsoleController(
                movieService,
                viewingService,
                reservationService,
                hallService
        );

        controller.drawMainMenu(System.in, System.out);
    }
}
