package org.example.part2;

public class HashTable {
    private Integer[] table;
    private int size;

    public HashTable(int capacity) {
        table = new Integer[capacity];
        size = 0;
    }

    private int hash(int key) {
        return key % table.length;
    }

    public boolean insert(int key) {
        if (size == table.length) return false;

        int index = hash(key);
        while (table[index] != null) {
            index = (index + 1) % table.length;
        }
        table[index] = key;
        size++;
        return true;
    }

    public boolean search(int key) {
        int index = hash(key);
        int startIndex = index;

        while (table[index] != null) {
            if (table[index].equals(key)) return true;
            index = (index + 1) % table.length;
            if (index == startIndex) break;
        }
        return false;
    }

    public boolean delete(int key) {
        int index = hash(key);
        int startIndex = index;

        while (table[index] != null) {
            if (table[index].equals(key)) {
                table[index] = null;
                size--;
                return true;
            }
            index = (index + 1) % table.length;
            if (index == startIndex) break;
        }
        return false;
    }

    public int getSize() {
        return size;
    }

    public void setTable(Integer[] table) {
        this.table = table;
        size = 0;
    }

    public Integer[] getTable() {
        return table;
    }
}
