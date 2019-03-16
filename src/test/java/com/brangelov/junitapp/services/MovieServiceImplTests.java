package com.brangelov.junitapp.services;

import com.brangelov.junitapp.entities.Movie;
import com.brangelov.junitapp.storage.Storage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MovieServiceImplTests {

    private MovieServiceImpl service;
    private Storage<Movie> mockStorage;

    @Before
    public void setUp() throws Exception {
        mockStorage = mock(Storage.class);
        service = new MovieServiceImpl(mockStorage);
    }

    @Test
    public void doesntReturnNull() {
        when(mockStorage.getAll()).thenReturn(new ArrayList<>());

        List<Movie> list = service.getAll();

        assertNotNull("Doesn't return null", list);
    }

    @Test
    public void returnsAllOrdered() {
        LocalDateTime now = LocalDateTime.now();
        when(mockStorage.getAll()).thenReturn(Stream.of(
                new Movie("Title 1", null, now),
                new Movie("Title 2", null, now.plusDays(1))
        ).collect(Collectors.toList()));

        List<Movie> all = service.getAll();

        assertNotNull("Doesn't return null", all);
        assertEquals("Returns 2 elements", all.size(), 2);
        assertEquals("First movie is the latter one", all.get(0).getTitle(), "Title 2");
    }

    @Test
    public void returnsById() {
        Movie movie = new Movie("Title", null, LocalDateTime.now());
        movie.generateId();
        String id = movie.getId();

        when(mockStorage.getById(eq(id))).thenReturn(movie);

        Movie result = service.getById(id);

        assertEquals("Returned correct result", result, movie);
    }

    @Test
    public void correctlyStoresMovie() {

        Movie movie = new Movie("Title", null, LocalDateTime.now());
        Mockito.doAnswer(invocation -> {
            Movie arg = invocation.getArgumentAt(0, Movie.class);
            assertEquals("Movie correctly served to storage", arg, movie);
            return null;
        }).when(mockStorage).save(any(Movie.class));

        service.save(movie);
    }

    @Test
    public void correctlyValidatesMovie() {
        Movie movie = new Movie(null, null, LocalDateTime.now());

        try {
            service.save(movie);
            Assert.fail("Did not throw on null title");
        } catch (IllegalArgumentException e) {
            assertEquals("Movie title error", e.getMessage(), "Movie title is null");
        }

        movie = new Movie("Title", null, null);

        try {
            service.save(movie);
            Assert.fail("Did not throw on null air date");
        } catch (IllegalArgumentException e) {
            assertEquals("Movie air date error", e.getMessage(), "Movie requires an air date");
        }
    }
}
