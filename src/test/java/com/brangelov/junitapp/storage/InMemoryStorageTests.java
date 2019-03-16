package com.brangelov.junitapp.storage;

import com.brangelov.junitapp.entities.Entity;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InMemoryStorageTests {

    private InMemoryStorage<TestEntity> storage;

    @Before
    public void setUp() {
        storage = new InMemoryStorage<>();
    }

    @Test
    public void getAllDoesntReturnNullWhenEmpty() {

        List<TestEntity> list = storage.getAll();

        assertNotNull("Not null when empty", list);
    }

    @Test
    public void savesSuccessfullyWithId() {

        final String id = "id";
        final int value = 123;
        storage.save(new TestEntity(id, value));

        List<TestEntity> list = storage.getAll();

        assertNotNull("Not null", list);
        assertEquals("Exactly 1 element", list.size(), 1);

        TestEntity entity = list.get(0);

        assertEquals("Same id", entity.getId(), id);
        assertEquals("Same value", entity.getValue(), value);
    }

    @Test
    public void savesSuccessfullyAndGeneratesId() {

        final int value = 123;
        storage.save(new TestEntity(null, value));

        List<TestEntity> all = storage.getAll();

        assertNotNull("Not null", all);
        assertEquals("Exactly 1 element", all.size(), 1);

        TestEntity entity = all.get(0);

        assertNotNull("Id not null", entity.getId());
        assertEquals("Value is the same", entity.getValue(), value);
    }

    @Test
    public void savesAndGetsById() {

        final String id = "123";
        storage.save(new TestEntity(id, 0));

        TestEntity entity = storage.getById(id);

        assertNotNull("Found entity", entity);
        assertEquals("Id is same", entity.getId(), id);
    }

    @Test
    public void savesThenReplacesWithSameId() {

        final String id = "123";
        final int val1 = 0;
        final int val2 = 1;
        storage.save(new TestEntity(id, val1));
        storage.save(new TestEntity(id, val2));

        List<TestEntity> list = storage.getAll();

        assertNotNull("List not null", list);
        assertEquals("List has only one entity", list.size(), 1);

        TestEntity entity = list.get(0);
        assertEquals("Entity has same id", entity.getId(), id);
        assertEquals("Entity has second value", entity.getValue(), val2);
    }

    private static class TestEntity extends Entity {

        TestEntity(String id, int value) {
            setId(id);
            this.value = value;
        }

        private int value;

        int getValue() {
            return value;
        }
    }
}
