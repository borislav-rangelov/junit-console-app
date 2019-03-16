package com.brangelov.junitapp.services;

import com.brangelov.junitapp.entities.Hall;
import com.brangelov.junitapp.storage.Storage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HallServiceImplTests {

    private HallServiceImpl service;
    private Storage<Hall> mockStorage;

    @Before
    public void setUp() throws Exception {
        mockStorage = mock(Storage.class);
        service = new HallServiceImpl(mockStorage);
    }

    @Test
    public void returnsNotNull() {
        when(mockStorage.getAll()).thenReturn(new ArrayList<>());

        List<Hall> all = service.getAll();

        assertNotNull("Doesn't return null", all);
    }

    @Test
    public void returnsFullList() {
        when(mockStorage.getAll()).thenReturn(Stream.of(
                new Hall(0, new Hall.Seat[][]{
                        new Hall.Seat[]{
                                new Hall.Seat(0, 0, null)
                        }
                }),
                new Hall(1, new Hall.Seat[][]{
                        new Hall.Seat[]{
                                new Hall.Seat(0, 0, null)
                        }
                })
        ).collect(Collectors.toList()));

        List<Hall> list = service.getAll();

        assertNotNull("Doesn't return null", list);
        assertEquals("Returns both elements", list.size(), 2);
    }

    @Test
    public void returnsById() {
        Hall hall = new Hall(0, new Hall.Seat[][]{
                new Hall.Seat[]{
                        new Hall.Seat(0, 0, null)
                }
        });
        hall.setId("id");
        when(mockStorage.getById(eq("id"))).thenReturn(hall);

        Hall result = service.getById("id");

        assertEquals("Returned hall", result, hall);
    }

    @Test
    public void correctlyValidatesHall() {

        Hall existing = new Hall(1, null);
        existing.setId("id1");

        Hall hall = new Hall(1, null);
        hall.setId("id2");

        when(mockStorage.getAll()).thenReturn(Collections.singletonList(existing));

        try {
            service.save(hall);
            fail("Failed to check hall number");
        } catch (IllegalArgumentException e) {
            assertEquals("Existing hall number", e.getMessage(), "Duplicate hall number");
        }

        existing = new Hall(2, null);
        existing.setId("id2");
        when(mockStorage.getAll()).thenReturn(Collections.singletonList(existing));

        try {
            service.save(hall);
            fail("Failed to check seats");
        } catch (IllegalArgumentException e) {
            assertEquals("null seats error", e.getMessage(), "Seats required");
        }

        hall = new Hall(1, new Hall.Seat[][]{
                null,
                new Hall.Seat[]{}
        });

        try {
            service.save(hall);
            fail("Failed to check rows for nulls");
        } catch (IllegalArgumentException e) {
            assertEquals("Rows null error", e.getMessage(), "Rows cannot be null");
        }

        hall = new Hall(1, new Hall.Seat[][]{});

        try {
            service.save(hall);
            fail("Failed to check rows empty");
        } catch (IllegalArgumentException e) {
            assertEquals("Rows empty error", e.getMessage(), "Rows cannot be empty");
        }

        hall = new Hall(1, new Hall.Seat[][]{new Hall.Seat[]{null, null}});

        try {
            service.save(hall);
            fail("Failed to check null seats");
        } catch (IllegalArgumentException e) {
            assertEquals("Seat null error", e.getMessage(), "Seat is null");
        }

        hall = new Hall(1, new Hall.Seat[][]{
                new Hall.Seat[]{new Hall.Seat(2, 123, null)}
        });

        try {
            service.save(hall);
            fail("Failed to check seat's row number");
        } catch (IllegalArgumentException e) {
            assertEquals("Bad row number error", e.getMessage(), "Bad row number on seat");
        }

        hall = new Hall(1, new Hall.Seat[][]{
                new Hall.Seat[]{new Hall.Seat(1, 0, null)}
        });

        try {
            service.save(hall);
            fail("Failed to check seat number <= 0");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid seat number", e.getMessage(), "Seat numbers cannot be less than 1");
        }

        hall = new Hall(1, new Hall.Seat[][]{
                new Hall.Seat[]{
                        new Hall.Seat(1, 1, null),
                        new Hall.Seat(1, 1, null)
                }
        });

        try {
            service.save(hall);
            fail("Failed to check duplicate seat number");
        } catch (IllegalArgumentException e) {
            assertEquals("Duplicate seat number", e.getMessage(), "Duplicate seat numbers on same row");
        }
    }

    @Test
    public void correctlySavesHall() {

        Hall hall = new Hall(1, new Hall.Seat[][]{
                new Hall.Seat[]{
                        new Hall.Seat(1, 1, null),
                        new Hall.Seat(1, 2, null)
                }
        });

        when(mockStorage.getAll()).thenReturn(new ArrayList<>());

        Mockito.doAnswer(invocation -> {
            Hall arg = invocation.getArgumentAt(0, Hall.class);
            assertEquals("Hall delivered to storage", arg, hall);
            return null;
        }).when(mockStorage).save(any(Hall.class));

        service.save(hall);
    }
}
