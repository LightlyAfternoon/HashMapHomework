package org.example;

import java.util.*;

/**
 * {@code MyHashMap} is an {@code AbstractMap} implementation.
 *  Its structure consists from hash table - a key-value Node and Index of bucket,
 *  where node is storing.
 *  Node is a nested class implements a {@code Map.Entry} interface and contains
 *  fields hash for calculating an index, key, value, and next - a field for
 *  storing Nodes that were put in this bucket previously.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class MyHashMap<K, V> extends AbstractMap<K, V> {
    private final static int DEFAULT_TABLE_CAPACITY = 16;
    private final static float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size = 0;
    private int limit;
    /**
     * loadFactor is an indicator of at what load MyHashMap should recreate
     * an array with increased capacity and transfer all Nodes.
     */
    private final float loadFactor;

    public MyHashMap(int capacity, float loadFactor) {
        int MAX_CAPACITY = Integer.MAX_VALUE >> 1;
        this.loadFactor = loadFactor;

        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }
        if (capacity > MAX_CAPACITY) {
            table = new Node[MAX_CAPACITY];
            limit = (int) (MAX_CAPACITY * loadFactor);
        } else if (capacity < 1) {
            table = new Node[DEFAULT_TABLE_CAPACITY];
            limit = (int) (DEFAULT_TABLE_CAPACITY * loadFactor);
        } else {
            table = new Node[capacity];
            limit = (int) (capacity * loadFactor);
        }
    }

    public MyHashMap(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap() {
        this(DEFAULT_TABLE_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Returns the number of key-value Nodes int this map
     * @return the number of key-value Nodes int this map
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns the value connected with the key, or null
     * if this map contains no mapping for this key
     * @return Returns the value connected with the key, or null
     * if this map contains no mapping for the key
     */
    @Override
    public V get(Object key) {
        Node<K, V> node = getNode(key);

        if (node != null) {
            return node.value;
        }
        return null;
    }

    /**
     * Returns the previous value associated with the key in this map,
     * or null if there was no mapping for this key.
     * This method adds a new Node in this map, or override a value for
     * existing Node with the key.
     * If a bucket already has a Node, then this Node placed in a field {@code next}
     * of new Node, and new Node places in this bucket
     * @return Returns the previous value associated with the key in this map,
     * or null if there was no mapping for this key
     */
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

    /**
     * Returns the value that was associated with the key in this map,
     * or null if there was no mapping for this key.
     * This method delete Node in this map with the key, if this Node exists.
     * If this Node have another Node in its {@code next} field, than Node
     * will replace deleting Node in a bucket or in a {@code next} field of parent Node.
     * @return Returns the value that was associated with the key in this map,
     *      * or null if there was no mapping for this key
     */
    public V delete(Object key) {
        Node<K, V> node = getNode(key);

        if (node != null) {
            Node<K, V> parentNode = getParentNode(node);

            if (parentNode != null && node.next != null) {
                parentNode.next = node.next;
            } else if (parentNode != null){
                parentNode.next = null;
            } else if (node.next != null) {
                int index = indexFor(node.hash, table.length);

                table[index] = node.next;
            } else {
                int index = indexFor(node.hash, table.length);

                table[index] = null;
            }

            size--;

            return node.value;
        }

        return null;
    }

    /**
     * Returns a collection of all values that containing in this map,
     * or empty collection.
     * @return Returns a collection of all values that containing in this map,
     *      * or empty collection
     */
    @Override
    public Collection<V> values() {
        Collection<V> collection = new ArrayList<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                collection.add(table[i].value);

                Node<K, V> next = table[i].next;
                if (next != null) {
                    do {
                        collection.add(next.value);
                        next = next.next;
                    } while (next != null);
                }
            }
        }

        return collection;
    }

    /**
     * Returns a Set of all keys that containing in this map,
     * or empty Set.
     * @return Returns a Set of all keys that containing in this map,
     *      * or empty Set
     */
    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                set.add(table[i].key);

                Node<K, V> next = table[i].next;
                if (next != null) {
                    do {
                        set.add(next.key);
                        next = next.next;
                    } while (next != null);
                }
            }
        }

        return set;
    }

    /**
     * Returns a Set of all key-value Entries that containing in this map,
     * or empty Set.
     * @return Returns a Set of all key-value Entries that containing in this map,
     *      * or empty Set
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = new HashSet<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                set.add(table[i]);

                Node<K, V> next = table[i].next;
                if (next != null) {
                    do {
                        set.add(next);
                        next = next.next;
                    } while (next != null);
                }
            }
        }

        return set;
    }

    private Node<K, V> getNode(Object key) {
        int keyHash = hash(key);
        if (size != 0) {
            int i = indexFor(keyHash, table.length);
            if (table[i] != null && table[i].hash == keyHash && (table[i].key == key || table[i].key.equals(key))) {
                return table[i];
            } else if (table[i] != null && table[i].next != null) {
                Node<K, V> node = table[i].next;

                do {
                    if (node.hash == keyHash && (node.key == key || node.key.equals(key))) {
                        return node;
                    }
                } while ((node = node.next) != null);
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

                 do {
                     if (thisNode.next == node) {
                         return thisNode;
                     }
                     thisNode = thisNode.next;
                 } while (thisNode != null);
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

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                Node<K, V> node = table[i];
                int newIndex;

                do {
                    Node<K, V> next = node.next;
                    newIndex = indexFor(node.hash, newLength);

                    node.next = newTable[newIndex];
                    newTable[newIndex] = node;
                    node = next;
                } while (node != null);
            }
        }

        limit = (int) (newLength * loadFactor);
        table = newTable;
    }

    private void putNode(int index, Node<K, V> node, Node<K, V>[] table) {
        if (table[index] != null) {
            node.next = table[index];
        }
        table[index] = node;
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