package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

class MyHashMapTest {
    MyHashMap<String, String> myHashMap;

    @BeforeEach
    void setupMyHashMap() {
        System.out.println("@BeforeAll executed");
        myHashMap = new MyHashMap<>();
    }

    @Test
    void testAddElementsWithSimilarKey() {
        System.out.println("==TEST ONE EXECUTED==");

        myHashMap.put("1", "One");
        Assertions.assertEquals("One", myHashMap.put("1", "Two"));
        Assertions.assertEquals(1, myHashMap.size());
    }

    @Test
    void testAddElementsWithSimilarHash() {
        System.out.println("==TEST TWO EXECUTED==");

        myHashMap.put("Aa", "One");
        Assertions.assertNull(myHashMap.put("BB", "Two"));
        Assertions.assertEquals(2, myHashMap.size());

    }

    @Test
    void testAddManyElements() {
        System.out.println("==TEST THREE EXECUTED==");

        for (int i = 0; i < 10000; i++) {
            myHashMap.put(String.valueOf(i), "element " + i);
        }
        Assertions.assertEquals(10000, myHashMap.size());
    }

    @Test
    void testGetManyElements() {
        System.out.println("==TEST FOUR EXECUTED==");

        for (int i = 0; i < 1001; i++) {
            myHashMap.put(String.valueOf(i), "element " + i);
        }

        for (int i = 0; i < 1001; i++) {
            Assertions.assertNotEquals(null, myHashMap.get(String.valueOf(i)));
        }
        Assertions.assertNull(myHashMap.get("2000"));
        Assertions.assertEquals(1001, myHashMap.size());
    }

    @Test
    void testDeleteManyElements() {
        System.out.println("==TEST FIVE EXECUTED==");

        for (int i = 0; i < 1001; i++) {
            myHashMap.put(String.valueOf(i), "element " + i);
        }

        for (int i = 0; i < 1001; i++) {
            Assertions.assertNotEquals(null, myHashMap.delete(String.valueOf(i)));
        }
        Assertions.assertNull(myHashMap.delete(1));
        Assertions.assertNull(myHashMap.delete(28));
        Assertions.assertNull(myHashMap.delete(174));
        Assertions.assertNull(myHashMap.delete(1000));
        Assertions.assertNull(myHashMap.delete(1050));

        Assertions.assertNull(myHashMap.get("1"));
        Assertions.assertEquals(0, myHashMap.size());
    }

    @Test
    void testAddAndGetNullKeyElements() {
        System.out.println("==TEST SIX EXECUTED==");

        for (int i = 0; i < 20; i++) {
            myHashMap.put(null, "element " + i);
        }
        Assertions.assertEquals("element 19", myHashMap.get(null));
        myHashMap.put(null, null);
        Assertions.assertNull(myHashMap.get(null));
        Assertions.assertEquals(1, myHashMap.size());
    }

    @Test
    void testGetAllValues() {
        System.out.println("==TEST SEVEN EXECUTED==");

        for (int i = 0; i < 1000; i++) {
            myHashMap.put(String.valueOf(i), "element " + i);
        }

        List<String> list = (List<String>) myHashMap.values();

        for (int i = 0; i < 1000; i++) {
            Assertions.assertTrue(list.contains("element " + i));
        }
    }

    @Test
    void testGetKeySet() {
        System.out.println("==TEST EIGHT EXECUTED==");

        for (int i = 0; i < 1000; i++) {
            myHashMap.put(String.valueOf(i), "element " + i);
        }

        Set<String> set = myHashMap.keySet();

        for (int i = 0; i < 1000; i++) {
            Assertions.assertTrue(set.contains(String.valueOf(i)));
        }
    }

    @Test
    void testGetEntrySet() {
        System.out.println("==TEST NINE EXECUTED==");

        for (int i = 0; i < 1000; i++) {
            myHashMap.put(String.valueOf(i), "element " + i);
        }

        Set<Map.Entry<String, String>> set = myHashMap.entrySet();

        for (Map.Entry<String, String> entry : set) {
            Assertions.assertNotEquals(null, entry.getKey());
            Assertions.assertNotEquals(null, entry.getValue());
        }

        Assertions.assertEquals(1000, set.size());
    }

    @Test
    void testChangeValues() {
        System.out.println("==TEST TEN EXECUTED==");

        for (int i = 0; i < 1000; i++) {
            myHashMap.put(String.valueOf(i), "element " + i);
        }

        Set<Map.Entry<String, String>> set = myHashMap.entrySet();
        String otherValue = "none";

        for (Map.Entry<String, String> entry : set) {
            entry.setValue(otherValue);
        }

        Assertions.assertEquals(1000, set.size());
        for (String value : myHashMap.values()) {
            Assertions.assertEquals(otherValue, value);
        }
    }

    @Test
    void testConstructors() {
        System.out.println("==TEST ELEVEN EXECUTED==");
        myHashMap = new MyHashMap<>(55, 0.4f);

        for (int i = 0; i < 100; i++) {
            myHashMap.put(String.valueOf(i), "element " + i);
        }

        List<String> list = (List<String>) myHashMap.values();

        for (int i = 0; i < 100; i++) {
            Assertions.assertTrue(list.contains("element " + i));
        }

        myHashMap = new MyHashMap<>(9);

        for (int i = 0; i < 100; i++) {
            myHashMap.put(String.valueOf(i), "element " + i);
        }

        list = (List<String>) myHashMap.values();

        for (int i = 0; i < 100; i++) {
            Assertions.assertTrue(list.contains("element " + i));
        }
    }

    @AfterEach
    void tearHashMap() {
        System.out.println("@AfterEach executed");
        myHashMap = null;
    }
}