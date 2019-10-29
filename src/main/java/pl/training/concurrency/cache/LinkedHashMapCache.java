package pl.training.concurrency.cache;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class LinkedHashMapCache<Key, Value> {

    private static final int LOAD_FACTOR = 1;

    private Map<Key, Value> store;

    public LinkedHashMapCache(int maxCapacity) {
        store = Collections.synchronizedMap(new LinkedHashMap<>(maxCapacity, LOAD_FACTOR, true) {

            @Override
            protected boolean removeEldestEntry(Map.Entry<Key, Value> eldest) {
                return size() > maxCapacity;
            }

        });
    }

    public Optional<Value> get(Key key) {
        return Optional.ofNullable(store.get(key));
    }

    public void put(Key key, Value value) {
        store.put(key, value);
    }

}
