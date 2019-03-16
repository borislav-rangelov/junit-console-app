package com.brangelov.junitapp.services;

import com.brangelov.junitapp.entities.Hall;
import com.brangelov.junitapp.storage.Storage;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class HallServiceImpl extends BaseServiceImpl<Hall> implements HallService {

    public HallServiceImpl(Storage<Hall> storage) {
        super(storage);
    }

    @Override
    public Hall getByNumber(int number) {
        for (Hall hall : getAll()) {
            if (hall.getHallNumber() == number) {
                return hall;
            }
        }
        return null;
    }

    @Override
    public void save(Hall entity) {

        Hall hall = getByNumber(entity.getHallNumber());
        if (hall != null && !Objects.equals(entity.getId(), hall.getId())) {
            throw new IllegalArgumentException("Duplicate hall number");
        }

        if (entity.getSeats() == null) {
            throw new IllegalArgumentException("Seats required");
        }

        Hall.Seat[][] seats = entity.getSeats();

        if (seats.length == 0) {
            throw new IllegalArgumentException("Rows cannot be empty");
        }

        for (int rowIndex = 0; rowIndex < seats.length; rowIndex++) {
            Hall.Seat[] row = seats[rowIndex];

            if (row == null) {
                throw new IllegalArgumentException("Rows cannot be null");
            }

            Set<Integer> seatNumbers = new HashSet<>();

            for (Hall.Seat seat : row) {

                if (seat == null) {
                    throw new IllegalArgumentException("Seat is null");
                }

                if (seat.getRowNumber() != rowIndex + 1) {
                    throw new IllegalArgumentException("Bad row number on seat");
                }

                if (seat.getSeatNumber() < 1) {
                    throw new IllegalArgumentException("Seat numbers cannot be less than 1");
                }

                if (seatNumbers.contains(seat.getSeatNumber())) {
                    throw new IllegalArgumentException("Duplicate seat numbers on same row");
                }

                seatNumbers.add(seat.getSeatNumber());
            }
        }

        super.save(entity);
    }
}
