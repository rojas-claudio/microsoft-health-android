package com.jayway.jsonpath.internal;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
/* loaded from: classes.dex */
public class Cache {
    private final int limit;
    private final ReentrantLock lock = new ReentrantLock();
    private final Map<String, Path> map = new ConcurrentHashMap();
    private final Deque<String> queue = new LinkedList();

    public Cache(int limit) {
        this.limit = limit;
    }

    public void put(String key, Path value) {
        Path oldValue = this.map.put(key, value);
        if (oldValue != null) {
            removeThenAddKey(key);
        } else {
            addKey(key);
        }
        if (this.map.size() > this.limit) {
            this.map.remove(removeLast());
        }
    }

    public Path get(String key) {
        removeThenAddKey(key);
        return this.map.get(key);
    }

    private void addKey(String key) {
        this.lock.lock();
        try {
            this.queue.addFirst(key);
        } finally {
            this.lock.unlock();
        }
    }

    private String removeLast() {
        this.lock.lock();
        try {
            String removedKey = this.queue.removeLast();
            return removedKey;
        } finally {
            this.lock.unlock();
        }
    }

    private void removeThenAddKey(String key) {
        this.lock.lock();
        try {
            this.queue.removeFirstOccurrence(key);
            this.queue.addFirst(key);
        } finally {
            this.lock.unlock();
        }
    }

    private void removeFirstOccurrence(String key) {
        this.lock.lock();
        try {
            this.queue.removeFirstOccurrence(key);
        } finally {
            this.lock.unlock();
        }
    }

    public Path getSilent(String key) {
        return this.map.get(key);
    }

    public void remove(String key) {
        removeFirstOccurrence(key);
        this.map.remove(key);
    }

    public int size() {
        return this.map.size();
    }

    public String toString() {
        return this.map.toString();
    }
}
