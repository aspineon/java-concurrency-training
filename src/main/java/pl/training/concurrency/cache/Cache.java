package pl.training.concurrency.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;

public class Cache<Key, Value> {

    private final Map<Key, Value> store = new HashMap<>();
    private final TreeSet<Index<Key>> indices = new TreeSet<>();
    private long maxCapacity;

    public Cache(long maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public synchronized void put(Key key, Value value) {
       Optional<Index<Key>> index = getIndexByKey(key);
       if (index.isEmpty()) {
           indices.add(new Index<>(key));
       }
       store.put(key, value);
       ensureCapacity();
    }

    public synchronized Optional<Value> get(Key key) {
        getIndexByKey(key).ifPresent(Index::increaseAccessCounter);
        return Optional.ofNullable(store.get(key));
    }

    public synchronized Map<Key, Value> getAll() {
        return new HashMap<>(store);
    }

    private void ensureCapacity() {
        if (indices.size() > maxCapacity) {
            Index<Key> index = indices.pollLast();
            if (index != null) {
                store.remove(index.getKey());
            }
        }
    }

    private Optional<Index<Key>> getIndexByKey(Key key) {
        return indices.stream()
                .filter(index -> index.hasKey(key))
                .findFirst();
    }

}
