package com.lizhi.method.queue;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class ArrayQueue<E> implements Queue<E>, Iterable<E> {

    private int head =  0;
    private int tail = 0;
    private E[] array;
    private int length;
    private int size;

    @SuppressWarnings("all")
    public ArrayQueue(int capacity) {
        length = capacity;
        array = (E[]) new Object[length];
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull(){
        return size == length;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int capacity = 0;
            int p = head;
            @Override
            public boolean hasNext() {
                return capacity != size;
            }

            @Override
            public E next() {
                E e = array[p];
                p = (p + 1) % length;
                capacity++;
                return e;
            }
        };
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E e) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean offer(E e) {
        if (isFull()){
            return false;
        }
        array[tail] = e;
        tail = (tail + 1) % length;
        size++;
        return true;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public E poll() {
        if (isEmpty()){
            return null;
        }
        E e = array[head];
        head = (head + 1) % length;
        size--;
        return e;
    }

    @Override
    public E element() {
        return null;
    }

    @Override
    public E peek() {
        if (isEmpty()){
            return null;
        }
        return array[head];
    }
}
