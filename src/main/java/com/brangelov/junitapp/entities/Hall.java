package com.brangelov.junitapp.entities;

public class Hall extends Entity {

    public Hall() {
    }

    public Hall(int hallNumber, Seat[][] seats) {
        this.hallNumber = hallNumber;
        this.seats = seats;
    }

    private int hallNumber;

    private Seat[][] seats;

    public int getHallNumber() {
        return hallNumber;
    }

    public void setHallNumber(int hallNumber) {
        this.hallNumber = hallNumber;
    }

    public Seat[][] getSeats() {
        return seats;
    }

    public void setSeats(Seat[][] seats) {
        this.seats = seats;
    }

    public Seat getSeat(int row, int seat) {
        if (row < 1) {
            throw new IllegalArgumentException("Rows cannot be less than 0");
        }
        if (seat < 1) {
            throw new IllegalArgumentException("Seats cannot be less than 0");
        }

        if (row > seats.length) {
            return null;
        }

        Seat[] r = seats[row - 1];
        for (Seat s : r) {
            if (s.getSeatNumber() == seat) {
                return s;
            }
        }

        return null;
    }

    public int maxSeatN() {
        int max = 0;
        for (Seat[] row : seats) {
            for (Seat seat : row) {
                max = Math.max(seat.seatNumber, max);
            }
        }
        return max;
    }

    public static class Seat {

        public Seat() {
        }

        public Seat(int rowNumber, int seatNumber, String reservationId) {
            this.rowNumber = rowNumber;
            this.seatNumber = seatNumber;
            this.reservationId = reservationId;
        }

        private int rowNumber;

        private int seatNumber;

        private String reservationId;

        public int getRowNumber() {
            return rowNumber;
        }

        public void setRowNumber(int rowNumber) {
            this.rowNumber = rowNumber;
        }

        public int getSeatNumber() {
            return seatNumber;
        }

        public void setSeatNumber(int seatNumber) {
            this.seatNumber = seatNumber;
        }

        public String getReservationId() {
            return reservationId;
        }

        public void setReservationId(String reservationId) {
            this.reservationId = reservationId;
        }
    }
}
