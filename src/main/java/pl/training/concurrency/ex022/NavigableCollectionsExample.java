package pl.training.concurrency.ex022;

import java.util.Collections;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class NavigableCollectionsExample {

    public static void main(String[] args) {
        NavigableMap<String, String> navigableMap = Collections.unmodifiableNavigableMap(new ConcurrentSkipListMap<>());
        NavigableSet<String> navigableSet = new ConcurrentSkipListSet<>();
    }

}
