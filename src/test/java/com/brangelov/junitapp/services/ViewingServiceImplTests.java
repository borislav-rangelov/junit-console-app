package com.brangelov.junitapp.services;

import com.brangelov.junitapp.entities.Hall;
import com.brangelov.junitapp.entities.Movie;
import com.brangelov.junitapp.entities.Viewing;
import com.brangelov.junitapp.storage.Storage;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class ViewingServiceImplTests {

    private ViewingServiceImpl service;
    private MovieService mockMovieService;
    private HallService mockHallService;
    private Storage<Viewing> mockStorage;

    @Before
    public void setUp() throws Exception {
        mockStorage = mock(Storage.class);
        mockMovieService = mock(MovieService.class);
        mockHallService = mock(HallService.class);
        service = new ViewingServiceImpl(mockStorage, mockMovieService, mockHallService);
    }

    @Test
    public void returnsEmptyList() {
        when(mockStorage.getAll()).thenReturn(new ArrayList<>());

        List<Viewing> list = service.getAll();

        assertNotNull("Returned list when no results", list);
    }

    @Test
    public void returnsAllResults() {
        when(mockStorage.getAll()).thenReturn(Arrays.asList(
                new Viewing("mov1", 1, LocalDateTime.now()),
                new Viewing("mov2", 1, LocalDateTime.now())
        ));

        List<Viewing> list = service.getAll();

        assertNotNull("Returned list", list);
        assertEquals("All elements", list.size(), 2);
    }

    @Test
    public void returnsAllByMovieId() {
        when(mockStorage.getAll()).thenReturn(Arrays.asList(
                new Viewing("mov1", 1, LocalDateTime.now()),
                new Viewing("mov1", 2, LocalDateTime.now()),
                new Viewing("mov2", 1, LocalDateTime.now())
        ));

        List<Viewing> list = service.getByMovieId("mov1");

        assertNotNull("Returned list", list);
        assertEquals("All viewings for requested movie id", list.size(), 2);
    }

    @Test
    public void returnsById() {
        Viewing viewing = new Viewing("mov1", 1, LocalDateTime.now());
        viewing.setId("id");
        when(mockStorage.getById(eq("id"))).thenReturn(viewing);

        Viewing result = service.getById("id");

        assertEquals("Returned viewing", result, viewing);
    }

    @Test
    public void correctlyValidatesViewing() {

        Viewing viewing = new Viewing(null, 1, LocalDateTime.now());

        try {
            service.save(viewing);
            fail("missed movie id null error");
        } catch (IllegalArgumentException e) {
            assertEquals("Movie id null error", e.getMessage(), "Movie id is null");
        }

        viewing = new Viewing("mov1", 1, LocalDateTime.now());

        when(mockMovieService.getById(eq("mov1"))).thenReturn(null);

        try {
            service.save(viewing);
            fail("missed invalid movie id  error");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid movie id error", e.getMessage(), "Movie id is invalid");
        }

        viewing = new Viewing("mov1", 1, LocalDateTime.now());

        Movie movie = new Movie();
        movie.setId("mov1");

        when(mockMovieService.getById(eq("mov1"))).thenReturn(movie);
        when(mockHallService.getByNumber(eq(1))).thenReturn(null);

        try {
            service.save(viewing);
            fail("missed invalid hall number error");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid hall number error", e.getMessage(), "Hall number is invalid");
        }
    }

    @Test
    public void savesSuccessfully() {
        Viewing viewing = new Viewing("mov1", 1, LocalDateTime.now());

        Movie movie = new Movie();
        movie.setId("mov1");
        Hall hall = new Hall(1, null);
        when(mockMovieService.getById(eq("mov1"))).thenReturn(movie);
        when(mockHallService.getByNumber(eq(1))).thenReturn(hall);

        doAnswer(invocation -> {
            Viewing arg = invocation.getArgumentAt(0, Viewing.class);
            assertEquals("Served storage the viewing", arg, viewing);
            return null;
        }).when(mockStorage).save(any(Viewing.class));

        service.save(viewing);
    }
}
