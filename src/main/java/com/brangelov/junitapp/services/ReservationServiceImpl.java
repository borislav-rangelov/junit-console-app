package com.brangelov.junitapp.services;

import com.brangelov.junitapp.entities.Hall;
import com.brangelov.junitapp.entities.Reservation;
import com.brangelov.junitapp.entities.Viewing;
import com.brangelov.junitapp.storage.Storage;

import static com.brangelov.junitapp.StringUtil.isEmpty;

public class ReservationServiceImpl extends BaseServiceImpl<Reservation> implements ReservationService {

    private final ViewingService viewingService;
    private final HallService hallService;

    public ReservationServiceImpl(Storage<Reservation> storage,
                                  ViewingService viewingService,
                                  HallService hallService) {
        super(storage);

        assert viewingService != null;
        assert hallService != null;

        this.viewingService = viewingService;
        this.hallService = hallService;
    }

    @Override
    public void save(Reservation entity) {

        if (entity.getId() == null) {
            entity.generateId();
        }

        if (isEmpty(entity.getFirstName())) {
            throw new IllegalArgumentException("First name is null / empty");
        }

        if (isEmpty(entity.getLastName())) {
            throw new IllegalArgumentException("Last name is null / empty");
        }

        if (isEmpty(entity.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number is null / empty");
        }

        if (entity.getViewingId() == null) {
            throw new IllegalArgumentException("Viewing id is null");
        }

        Viewing viewing = viewingService.getById(entity.getViewingId());
        if (viewing == null) {
            throw new IllegalArgumentException("Viewing id invalid");
        }

        Hall hall = hallService.getByNumber(viewing.getHallNumber());
        if (hall == null) {
            throw new IllegalArgumentException("Hall number invalid");
        }

        Hall.Seat seat;

        try {
            seat = hall.getSeat(entity.getRow(), entity.getSeat());
        } catch (Exception e) {
            seat = null;
        }

        if (seat == null) {
            throw new IllegalArgumentException("Row or seat number invalid");
        }

        if (seat.getReservationId() != null && !seat.getReservationId().equals(entity.getId())) {
            throw new IllegalArgumentException("Seat is taken");
        }
        seat.setReservationId(entity.getId());

        hallService.save(hall);


        super.save(entity);
    }
}
