package com.brangelov.junitapp.services;

import com.brangelov.junitapp.entities.Hall;
import com.brangelov.junitapp.entities.Reservation;
import com.brangelov.junitapp.entities.Viewing;
import com.brangelov.junitapp.storage.Storage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class ReservationServiceImplTests {

    private ReservationServiceImpl service;
    private Storage<Reservation> mockStorage;
    private ViewingService mockViewingService;
    private HallService mockHallService;

    @Before
    public void setUp() throws Exception {
        mockStorage = mock(Storage.class);
        mockViewingService = mock(ViewingService.class);
        mockHallService = mock(HallService.class);
        service = new ReservationServiceImpl(mockStorage, mockViewingService, mockHallService);
    }

    @Test
    public void returnsEmptyList() {
        when(mockStorage.getAll()).thenReturn(new ArrayList<>());

        List<Reservation> list = service.getAll();

        assertNotNull("Returns list when no results", list);
    }

    @Test
    public void returnsAllReservations() {
        when(mockStorage.getAll()).thenReturn(Arrays.asList(
                new Reservation(),
                new Reservation()
        ));

        List<Reservation> list = service.getAll();

        assertNotNull("Returns list when no results", list);
        assertEquals("Returns all items", list.size(), 2);
    }

    @Test
    public void returnsById() {
        Reservation reservation = new Reservation();
        reservation.setId("id");
        when(mockStorage.getById(eq("id"))).thenReturn(reservation);

        Reservation result = service.getById("id");

        assertEquals("Returned reservation", result, reservation);
    }

    @Test
    public void validatesCorrectlyReservation() {

        Reservation reservation = new Reservation(
                "", "abc", "abc", "v1", 0, 0);

        try {
            service.save(reservation);
            fail("missed empty first name error");
        } catch (IllegalArgumentException e) {
            assertEquals("Empty first name error", e.getMessage(), "First name is null / empty");
        }

        reservation = new Reservation(
                "abc", "", "abc", "v1", 0, 0);

        try {
            service.save(reservation);
            fail("missed empty last name error");
        } catch (IllegalArgumentException e) {
            assertEquals("Empty last name error", e.getMessage(), "Last name is null / empty");
        }

        reservation = new Reservation(
                "abc", "abc", "", "v1", 0, 0);

        try {
            service.save(reservation);
            fail("missed empty phone number error");
        } catch (IllegalArgumentException e) {
            assertEquals("Empty phone number error", e.getMessage(), "Phone number is null / empty");
        }

        reservation = new Reservation(
                "abc", "abc", "abc", null, 0, 0);

        try {
            service.save(reservation);
            fail("missed viewing id null error");
        } catch (IllegalArgumentException e) {
            assertEquals("Viewing id null error", e.getMessage(), "Viewing id is null");
        }

        reservation = new Reservation(
                "abc", "abc", "abc", "v1", 0, 0);

        when(mockViewingService.getById(eq("v1"))).thenReturn(null);

        try {
            service.save(reservation);
            fail("missed viewing id invalid error");
        } catch (IllegalArgumentException e) {
            assertEquals("Viewing id invalid error", e.getMessage(), "Viewing id invalid");
        }

        reservation = new Reservation(
                "abc", "abc", "abc", "v1", 0, 0);

        Viewing viewing = new Viewing("mov1", 1, LocalDateTime.now());
        when(mockViewingService.getById(eq("v1"))).thenReturn(viewing);
        when(mockHallService.getByNumber(eq(1))).thenReturn(null);

        try {
            service.save(reservation);
            fail("missed hall number invalid error");
        } catch (IllegalArgumentException e) {
            assertEquals("Hall number invalid error", e.getMessage(), "Hall number invalid");
        }

        reservation = new Reservation(
                "abc", "abc", "abc", "v1", 0, 0);

        viewing = new Viewing("mov1", 1, LocalDateTime.now());
        Hall hall = new Hall(1, new Hall.Seat[][]{
                new Hall.Seat[]{new Hall.Seat(1, 1, null)}
        });
        when(mockViewingService.getById(eq("v1"))).thenReturn(viewing);
        when(mockHallService.getByNumber(eq(1))).thenReturn(hall);

        try {
            service.save(reservation);
            fail("missed seat numbers invalid error");
        } catch (IllegalArgumentException e) {
            assertEquals("Seat numbers invalid error", e.getMessage(), "Row or seat number invalid");
        }

        reservation = new Reservation(
                "abc", "abc", "abc", "v1", 1, 2);

        viewing = new Viewing("mov1", 1, LocalDateTime.now());

        hall = new Hall(1, new Hall.Seat[][]{
                new Hall.Seat[]{new Hall.Seat(1, 1, null)}
        });
        when(mockViewingService.getById(eq("v1"))).thenReturn(viewing);
        when(mockHallService.getByNumber(eq(1))).thenReturn(hall);

        try {
            service.save(reservation);
            fail("missed seat numbers invalid error");
        } catch (IllegalArgumentException e) {
            assertEquals("Seat numbers invalid error", e.getMessage(), "Row or seat number invalid");
        }

        reservation = new Reservation(
                "abc", "abc", "abc", "v1", 1, 1);

        viewing = new Viewing("mov1", 1, LocalDateTime.now());

        hall = new Hall(1, new Hall.Seat[][]{
                new Hall.Seat[]{new Hall.Seat(1, 1, "otherReservation")}
        });
        when(mockViewingService.getById(eq("v1"))).thenReturn(viewing);
        when(mockHallService.getByNumber(eq(1))).thenReturn(hall);

        try {
            service.save(reservation);
            fail("missed seat taken error");
        } catch (IllegalArgumentException e) {
            assertEquals("Seat taken error", e.getMessage(), "Seat is taken");
        }
    }

    @Test
    public void correctlySavesReservationAndSetsSeatReservationId() {

        Reservation reservation = new Reservation(
                "abc", "abc", "abc", "v1", 1, 1);

        Viewing viewing = new Viewing("mov1", 1, LocalDateTime.now());

        Hall.Seat seat = new Hall.Seat(1, 1, null);
        Hall hall = new Hall(1, new Hall.Seat[][]{new Hall.Seat[]{seat}});
        when(mockViewingService.getById(eq("v1"))).thenReturn(viewing);
        when(mockHallService.getByNumber(eq(1))).thenReturn(hall);

        Mockito.doNothing().when(mockHallService).save(any(Hall.class));

        doAnswer(invocation -> {
            Reservation arg = invocation.getArgumentAt(0, Reservation.class);
            assertEquals("Served reservation to storage", arg, reservation);
            return null;
        }).when(mockStorage).save(any(Reservation.class));

        service.save(reservation);

        assertEquals("Seat's reservationId was set", seat.getReservationId(), reservation.getId());

    }
}
