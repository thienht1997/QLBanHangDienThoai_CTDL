package com.ctdl.btl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Simple generic singly linked list implementation used as the primary storage
 * structure for the application. Only the operations required by the
 * assignment are implemented.
 */
public class SinglyLinkedList<T> implements Iterable<T> {

    private static final class Node<T> {
        private T data;
        private Node<T> next;

        private Node(T data) {
            this.data = data;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public void addLast(T data) {
        Node<T> node = new Node<>(data);
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    public void bulkAdd(Collection<T> items) {
        items.forEach(this::addLast);
    }

    public boolean removeIf(Predicate<T> predicate) {
        boolean removed = false;
        Node<T> prev = null;
        Node<T> current = head;
        while (current != null) {
            if (predicate.test(current.data)) {
                removed = true;
                size--;
                if (prev == null) {
                    head = current.next;
                } else {
                    prev.next = current.next;
                }
                if (current == tail) {
                    tail = prev;
                }
            } else {
                prev = current;
            }
            current = current.next;
        }
        return removed;
    }

    public Optional<T> findFirst(Predicate<T> predicate) {
        for (T item : this) {
            if (predicate.test(item)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public boolean update(Predicate<T> predicate, Function<T, T> updater) {
        Node<T> current = head;
        boolean updated = false;
        while (current != null) {
            if (predicate.test(current.data)) {
                current.data = updater.apply(current.data);
                updated = true;
            }
            current = current.next;
        }
        return updated;
    }

    public void sort(Comparator<T> comparator) {
        List<T> temp = toList();
        temp.sort(comparator);
        clear();
        bulkAdd(temp);
    }

    public List<T> toList() {
        List<T> list = new ArrayList<>(size);
        for (T item : this) {
            list.add(item);
        }
        return list;
    }

    public void clear() {
        head = tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> cursor = head;

            @Override
            public boolean hasNext() {
                return cursor != null;
            }

            @Override
            public T next() {
                T data = cursor.data;
                cursor = cursor.next;
                return data;
            }
        };
    }
}
