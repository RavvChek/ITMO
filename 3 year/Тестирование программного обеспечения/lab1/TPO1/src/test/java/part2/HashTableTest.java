package part2;

import org.example.part2.HashTable;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HashTableTest {
    HashTable hashTable;

    @BeforeAll
    static void setUpAll() {
        System.out.println("Running tests of the Hash Table function...");
    }

    @BeforeEach
    void setUp() {
        hashTable = new HashTable(5);
    }

    //  Чёрный ящик
    @Test
    @DisplayName("Check insert function")
    @Order(1)
    void checkInsert() {
        System.out.println("Test1\n");
        assertTrue(hashTable.insert(5));
        assertTrue(hashTable.insert(6));
        assertTrue(hashTable.search(5));
        assertTrue(hashTable.search(6));
    }

    @Test
    @DisplayName("Check insert with collision")
    @Order(2)
    void checkInsertCollision() {
        assertTrue(hashTable.insert(7));
        assertTrue(hashTable.insert(12));
        assertTrue(hashTable.search(7));
        assertTrue(hashTable.delete(7));
    }

    @Test
    @DisplayName("Check delete function")
    @Order(3)
    void checkDelete() {
        System.out.println("Test2\n");
        assertTrue(hashTable.insert(6));
        assertTrue(hashTable.delete(6));
        assertFalse(hashTable.search(6));
    }

    @Test
    @DisplayName("Check search function")
    @Order(4)
    void checkSearch() {
        System.out.println("Test3\n");
        assertTrue(hashTable.insert(1));
        assertTrue(hashTable.search(1));
    }

    @Test
    @DisplayName("Check full hash table function")
    @Order(5)
    void checkFullTable() {
        System.out.println("Test4\n");
        hashTable.insert(1);
        hashTable.insert(2);
        hashTable.insert(3);
        hashTable.insert(4);
        hashTable.insert(5);
        System.out.println("Size: " + hashTable.getSize() + "\nCapacity: " + hashTable.getTable().length);
        assertFalse(hashTable.insert(6));
    }

    // Серый ящик
    @Test
    @DisplayName("Check seq of elements")
    @Order(6)
    void checkSeqHashTable() {
        hashTable.insert(5);
        hashTable.insert(10);
        Integer[] arr = hashTable.getTable();
        assertEquals(arr[0], 5);
        assertEquals(arr[1], 10);
    }

    @Test
    @DisplayName("Check cpacity hash table")
    @Order(7)
    void checkCapacityHashTable() {
        assertEquals(0, hashTable.getSize());
        hashTable.insert(0);
        hashTable.insert(5);
        assertEquals(2, hashTable.getSize());
        hashTable.delete(0);
        assertEquals(1, hashTable.getSize());
    }

    // Белый ящик
    @Test
    @DisplayName("Check collision on insertion")
    @Order(8)
    void checkCollisionOnInsertion() {
        hashTable.insert(0);
        hashTable.insert(5);
        hashTable.insert(10);
        assertTrue(hashTable.search(10));
    }

    @Test
    @DisplayName("Check empty table")
    @Order(9)
    void checkEmptyTable() {
        assertFalse(hashTable.search(1));
    }

    @Test
    @Order(10)
    void testFullCycleSearch() {
        hashTable.insert(0);
        hashTable.insert(5);
        hashTable.insert(10);
        hashTable.insert(15);
        hashTable.insert(20);
        assertTrue(hashTable.search(15));
        assertFalse(hashTable.search(26));
        assertFalse(hashTable.delete(25));
    }

    @Test
    @Order(11)
    void testInsertAfterDeletion() {
        hashTable.insert(1);
        hashTable.insert(6);
        hashTable.delete(1);
        assertTrue(hashTable.insert(11));
        assertTrue(hashTable.search(11));
        Integer[] arr = hashTable.getTable();
        assertEquals(arr[1], 11);
        assertEquals(arr[2], 6);
    }

    @AfterEach
    void end() {
        hashTable.setTable(new Integer[]{});
    }

    @AfterAll
    static void endAll() {
        System.out.println("Ending tests of the Hash Table function...");
    }
}
