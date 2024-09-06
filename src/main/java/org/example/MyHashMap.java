package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class MyHashMap<K, V> extends AbstractMap<K, V> {
    private final static int DEFAULT_TABLE_CAPACITY = 16;
    private final static float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size = 0;
    private int limit;
    private float loadFactor;

    public MyHashMap() {
        table = new Node[DEFAULT_TABLE_CAPACITY];
        loadFactor = DEFAULT_LOAD_FACTOR;
        limit = (int) (DEFAULT_TABLE_CAPACITY * loadFactor);
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
        Node<K, V> existedNode = getNode(key);

        if (existedNode != null) {
            V oldValue = existedNode.value;
            existedNode.value = value;

            return oldValue;
        } else if (key == null) {
            Node<K, V> node = new Node<K, V>(0, null, value, null);

            putNode(0, node, table);
        } else {
            int keyHash = hash(key);
            int index = indexFor(keyHash, table.length);
            Node<K, V> node = new Node<>(keyHash, key, value, null);

            putNode(index, node, table);
        }

        if (++size > limit) {
            resize();
        }

        return null;
    }

    public V delete(Object key) {
        Node<K, V> node = getNode(key);

        if (node != null) {
            Node<K, V> parentNode = getParentNode(node);

            if (parentNode != null && node.next != null) {
                parentNode.next = node.next;
            } else if (parentNode != null){
                parentNode.next = null;
            } else {
                int index = indexFor(node.hash, table.length);

                table[index] = null;
            }

            size--;

            return node.value;
        }

        return null;
    }

    @Override
    public Collection<V> values() {
        return Arrays.stream(table).map(t -> t.value).collect(Collectors.toList());
    }

    @Override
    public Set<K> keySet() {
        return Arrays.stream(table).map(t -> t.key).collect(Collectors.toSet());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Arrays.stream(table).collect(Collectors.toSet());
    }

    private Node<K, V> getNode(Object key) {
        int keyHash = hash(key);
        if (table != null && size != 0) {
            for (int i = 0; i < table.length; i++) {
                if (table[i] != null && table[i].hash == keyHash && table[i].key.equals(key)) {
                    return table[i];
                } else if (table[i] != null && table[i].next != null) {
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

    private Node<K, V> getParentNode(Node<K, V> node) {
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && table[i].next == node) {
                return table[i];
            } else if (table[i] != null && table[i].next != null) {
                Node<K, V> thisNode = table[i];

                while (thisNode.next != null) {
                    if (thisNode.next == node) {
                        return thisNode;
                    }
                    thisNode = thisNode.next;
                }
            }
        }

        return null;
    }

    private int hash(Object key) {
        if (key == null) {
            return 0;
        }
        int hash = key.hashCode();
        hash ^= (hash >>> 20) ^ (hash >>> 12);

        return hash ^ (hash >>> 7) ^ (hash >>> 4);
    }

    private int indexFor(int hash, int tableLength) {
        return hash & (tableLength - 1);
    }

    private void resize() {
        int newLength = size << 1;
        Node<K, V>[] newTable = new Node[newLength];

        newTable[0] = table[0];
        for (int i = 1; i < table.length; i++) {
            if (table[i] != null) {
                Node<K, V> node = table[i];
                int newHash = hash(node.key);
                int newIndex = indexFor(newHash, newLength);
                newTable[newIndex] = node;

                while (node.next != null) {
                    node = node.next;
                    newHash = hash(node.key);
                    newIndex = indexFor(newHash, newLength);

                    putNode(newIndex, node, newTable);
                }
            }
        }

        limit = (int) (newLength * loadFactor);
    }

    private void putNode(int index, Node<K, V> node, Node<K, V>[] table) {
        if (table[index] == null) {
            table[index] = node;
        } else {
            Node<K, V> thisNode = table[index];

            while (thisNode.next != null) {
                thisNode = thisNode.next;
            }
            thisNode.next = node;
        }
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