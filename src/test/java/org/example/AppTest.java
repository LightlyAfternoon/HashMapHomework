package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AppTest {
    MyHashMap<String, String> myHashMap;

    @BeforeEach
    public void setupMyHashMap() {
        System.out.println("@BeforeAll executed");
        myHashMap = new MyHashMap<>();
    }

    @Test
    public void testAddElementsWithSimilarKey() {
        System.out.println("==TEST ONE EXECUTED==");

        myHashMap.put("1", "One");
        Assertions.assertEquals("One", myHashMap.put("1", "Two"));
    }

    @Test
    public void testAddElementsWithSimilarHash() {
        System.out.println("==TEST TWO EXECUTED==");

        myHashMap.put("A", "One");
        Assertions.assertNull(myHashMap.put("BB", "Two"));
    }

    @Test
    public void testAddManyElements() {
        System.out.println("==TEST THREE EXECUTED==");

        for (int i = 0; i < 200; i++) {
            myHashMap.put(String.valueOf(i), "element " + i);
        }
    }

    @Test
    public void testDeleteManyElements() {
        System.out.println("==TEST FOUR EXECUTED==");

        for (int i = 0; i < 200; i++) {
            myHashMap.put(String.valueOf(i), "element " + String.valueOf(i));
        }
        for (int i = 0; i < 200; i++) {
            // TODO ПЕРЕПРОВЕРИТЬ!
            Assertions.assertNotEquals(null, String.valueOf(myHashMap.delete(String.valueOf(i))));
        }
    }

    @AfterEach
    public void tearHashMap() {
        System.out.println("@AfterEach executed");
        myHashMap = null;
    }
}