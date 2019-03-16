package com.brangelov.junitapp.controllers;

import com.brangelov.junitapp.entities.Hall;
import com.brangelov.junitapp.entities.Movie;
import com.brangelov.junitapp.entities.Reservation;
import com.brangelov.junitapp.entities.Viewing;
import com.brangelov.junitapp.services.HallService;
import com.brangelov.junitapp.services.MovieService;
import com.brangelov.junitapp.services.ReservationService;
import com.brangelov.junitapp.services.ViewingService;

import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiConsumer;

public class ConsoleController {

    private final MovieService movieService;
    private final ViewingService viewingService;
    private final ReservationService reservationService;
    private final HallService hallService;

    public ConsoleController(MovieService movieService, ViewingService viewingService,
                             ReservationService reservationService, HallService hallService) {
        assert movieService != null;
        assert viewingService != null;
        assert reservationService != null;
        assert hallService != null;

        this.movieService = movieService;
        this.viewingService = viewingService;
        this.reservationService = reservationService;
        this.hallService = hallService;
    }

    public void drawMainMenu(InputStream in, PrintStream out) {
        while (true) {
            out.println("\n\n################################################");
            out.println("Main Menu");
            out.println("---------");
            out.println("1. List movies");
            out.println("2. New reservation\n");
            out.println("0. Quit\n\n");
            out.print(" Choose: ");

            Scanner scanner = new Scanner(in);
            int action = scanner.nextInt();

            switch (action) {
                case 1:
                    listMovies(in, out);
                    break;
                case 2:
                    makeReservation(in, out);
                    break;
                case 0:
                    return;
                default:
                    break;
            }
        }
    }

    private void listMovies(InputStream in, PrintStream out) {
        while (true) {

            List<Movie> movies = movieService.getAll();
            out.println("\n\n################################################");
            out.println("List Movies");
            out.println("-----------");

            forEach(movies, (i, movie) -> {
                out.println((i + 1) + ": " + movie.getTitle() + " - " + movie.getAirDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE));
                out.println(movie.getDescription());
            });

            out.println("\n\n0: Back");

            out.print(" Choose: ");
            Scanner scanner = new Scanner(in);
            int action = scanner.nextInt();

            if (action == 0) return;
        }
    }

    private void makeReservation(InputStream in, PrintStream out) {
        while (true) {

            List<Movie> movies = movieService.getAll();
            out.println("\n\n################################################");
            out.println("Pick Movie");
            out.println("-----------");

            forEach(movies, (i, movie) -> {
                out.println((i + 1) + ": " + movie.getTitle() + " - " + movie.getAirDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE));
            });

            out.println("\n\n0: Back");

            out.print(" Choose: ");
            Scanner scanner = new Scanner(in);
            int action = scanner.nextInt();

            if (action < 0) continue;
            if (action == 0) return;
            if (action > movies.size()) continue;
            Movie movie = movies.get(action - 1);
            action = listViewings(in, out, movie);
            if (action == 0) return;
        }
    }

    private int listViewings(InputStream in, PrintStream out, Movie movie) {
        while (true) {

            List<Viewing> viewings = viewingService.getByMovieId(movie.getId());
            out.println("\n\n################################################");
            out.println("List Viewings");
            out.println("-----------");
            out.println("Title: " + movie.getTitle());
            out.println("Description: " + movie.getDescription() + "\n");
            out.println("Viewings:");

            forEach(viewings, (i, viewing) -> {
                out.println((i + 1) + ": " + formatDateTime(viewing.getAiring()) +
                        " - Hall: " + viewing.getHallNumber());
            });

            out.println("0: Back");
            out.print(" Choose: ");
            Scanner scanner = new Scanner(in);
            int action = scanner.nextInt();

            if (action < 0) continue;
            if (action == 0) return 1;
            if (action > viewings.size()) continue;

            Viewing viewing = viewings.get(action - 1);
            action = pickSeat(in, out, movie, viewing);
            if (action == 0) return 0;
        }
    }

    private int pickSeat(InputStream in, PrintStream out, Movie movie, Viewing viewing) {
        int row = 0;
        int seat = 0;
        Hall.Seat selectedSeat = null;

        while (true) {

            Hall hall = hallService.getByNumber(viewing.getHallNumber());
            out.println("\n\n################################################");
            out.println("Movie: " + movie.getTitle());
            out.println("Date: " + formatDateTime(viewing.getAiring()));
            out.println("Hall: " + hall.getHallNumber() + "\n");

            out.println("Pick Seat");
            out.println("-----------");

            int maxSeatN = hall.maxSeatN();
            out.println("Free: O, Reserved: X");
            out.println("Row:");
            out.print("\\  ");
            for (int i = 1; i <= maxSeatN; i++) {
                out.print(i);
                out.print(". ");
            }
            out.println(" <- Seat");

            for (int rowN = 0; rowN < hall.getSeats().length; rowN++) {
                out.print((rowN + 1) + ". ");

                Hall.Seat[] seats = hall.getSeats()[rowN];

                for (int seatN = 1; seatN <= maxSeatN; seatN++) {
                    for (Hall.Seat s : seats) {
                        if (s.getSeatNumber() == seatN) {
                            out.print(s.getReservationId() == null ? "O" : "X");
                            out.print("  ");
                            break;
                        }
                    }
                }
                out.println();
            }

            Scanner scanner = new Scanner(in);

            if (row == 0) {
                out.println("0: Back");
                out.print(" Choose Row: ");
                int action = scanner.nextInt();
                if (action < 0) continue;
                if (action == 0) return 1;
                if (action > hall.getSeats().length) continue;
                row = action;
                continue;
            }
            if (seat == 0) {
                out.println("0: Undo");
                out.println("   Row: " + row);
                out.print(" Choose Seat: ");
                int action = scanner.nextInt();
                if (action < 0) continue;
                if (action == 0) {
                    row = 0;
                    continue;
                }

                Hall.Seat found = hall.getSeat(row, action);

                if (found == null) continue;

                selectedSeat = found;
                seat = action;
                continue;
            }

            out.println("   Row: " + row);
            out.println("   Seat: " + seat);

            if (selectedSeat.getReservationId() != null) {
                out.println("\nSeat already reserved!\n");
            }

            out.print(" Confirm (Press 1, Undo: 0): ");
            int action = scanner.nextInt();
            if (action == 0) {
                seat = 0;
                continue;
            }
            if (action == 1 && selectedSeat.getReservationId() == null) {
                action = createReservation(in, out, movie, viewing, hall, selectedSeat);
                if (action == 0) return 0;
            }
        }
    }

    private int createReservation(InputStream in, PrintStream out, Movie movie,
                                   Viewing viewing, Hall hall, Hall.Seat seat) {
        Reservation reservation = new Reservation();
        reservation.setViewingId(viewing.getId());
        reservation.setRow(seat.getRowNumber());
        reservation.setSeat(seat.getSeatNumber());

        while (true) {
            out.println("\n\n################################################");
            out.println("Movie: " + movie.getTitle());
            out.println("Date: " + formatDateTime(viewing.getAiring()));
            out.println("Hall: " + hall.getHallNumber() + "\n");
            out.println("Seat: " + seat.getRowNumber() + ":" + seat.getSeatNumber() + "\n");

            out.println("0: Back / Undo");

            Scanner scanner = new Scanner(in);
            out.print("First name: ");
            if (reservation.getFirstName() == null) {
                String next = scanner.next().trim();
                if (next.length() == 0) continue;
                if (next.equals("0")) return 1;
                reservation.setFirstName(next);
            } else {
                out.println(reservation.getFirstName());
            }

            out.print("Last name: ");
            if (reservation.getLastName() == null) {
                String next = scanner.next().trim();
                if (next.length() == 0) continue;
                if (next.equals("0")) {
                    reservation.setFirstName(null);
                    continue;
                }
                reservation.setLastName(next);
            } else {
                out.println(reservation.getLastName());
            }

            out.print("Phone number: ");
            if (reservation.getPhoneNumber() == null) {
                String next = scanner.next().trim();
                if (next.length() == 0) continue;
                if (next.equals("0")) {
                    reservation.setLastName(null);
                    continue;
                }
                reservation.setPhoneNumber(next);
                continue;
            } else {
                out.println(reservation.getPhoneNumber());
            }

            out.print("\n Confirm (Press 1):");
            int i = scanner.nextInt();
            if (i == 0) {
                reservation.setPhoneNumber(null);
                continue;
            }
            if (i == 1) {
                reservationService.save(reservation);
                return 0;
            }
        }
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private <T> void forEach(Collection<T> collection, BiConsumer<Integer, T> consumer) {
        int i = 0;
        for (T t : collection) {
            consumer.accept(i++, t);
        }
    }
}
