package org.my.collections;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyHashMap<K, V> extends AbstractMap<K, V> {
    private final static int DEFAULT_TABLE_CAPACITY = 16;
    private final static float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size = 0;
    private int limit;

    MyHashMap() {
        table = new Node[DEFAULT_TABLE_CAPACITY];
        limit = (int) (DEFAULT_TABLE_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public V get(Object key) {
        Node<K, V> node = getNode(key);

        if (node != null) {
            return node.value;
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        size++;

        return ;
    }

    public V delete(Object key) {
        size--;

        return ;
    }

    @Override
    public Collection<V> values() {
        return ;
    }

    @Override
    public Set<K> keySet() {
        return ;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return ;
    }

    private int hash(Object key) {
        if (key == null) {
            return 0;
        }
        int code = key.hashCode();
        code ^= (code >>> 20) ^ (code >>> 12);

        return code ^ (code >>> 7) ^ (code >>> 4);
    }

    private Node<K, V> getNode(Object key) {
        int keyHash = hash(key);
        if (table != null) {
            for (int i = 0; i < size; i++) {
                if (table[i].hash == keyHash && table[i].key.equals(key)) {
                    return table[i];
                } else if (table[i].next != null) {
                    Node<K, V> node = table[i];

                    while (node.next != null) {
                        if (node.hash == keyHash && node.key.equals(key)) {
                            return node;
                        } else {
                            node = node.next;
                        }
                    }
                }
            }
        }

        return null;
    }

    private static class Node<K, V> implements Map.Entry<K, V> {
        int hash;
        K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V prevValue = this.value;
            this.value = value;

            return prevValue;
        }
    }
}