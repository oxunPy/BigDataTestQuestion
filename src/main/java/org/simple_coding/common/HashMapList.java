package org.simple_coding.common;

import java.util.*;

public class HashMapList<T, E> {
    private HashMap<T, ArrayList<E>> map = new HashMap<T, ArrayList<E>>();

    // Insert item into list at key
    public void put(T key, E item) {
        if(!map.containsKey(key)) {
            map.put(key, new ArrayList<E>());
        }

        map.get(key).add(item);
    }

    // Insert list of items at key
    public void put(T key, ArrayList<E> items) {
        map.put(key, items);
    }

    // Check if hashmaplist contains key
    public boolean containsKey(T key) {
        return map.containsKey(key);
    }

    public boolean containsKeyValue(T key, E value) {
        ArrayList<E> list = map.get(key);
        if(list == null) return false;
        return list.contains(value);
    }

    public Collection<ArrayList<E>> values() {
        return map.values();
    }

    public Set<T> keySet() {
        return map.keySet();
    }

    @Override
    public String toString() {
        return map.toString();
    }

    public ArrayList<E> get(T key) {
        return map.get(key);
    }
}
