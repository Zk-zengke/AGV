package com.lizhi.method.queue;

import java.util.Iterator;

public class ArrayDeque<E> implements Iterable<E> {

    public boolean offerFirst(E e) {
        if (isFull()){
            return false;
        }
        head = inc(head,array.length);
        array[head] = e;
        return true;
    }

    public boolean offerLast(E e) {
        if (isFull()){
            return false;
        }
        array[tail] = e;
        tail = dec(tail,array.length);
        return true;
    }

    public E pollFirst() {
        if (isEmpty()){
            return null;

        }
        E e = array[head];
        array[head] = null;
        head = dec(head,array.length);
        return e;
    }

    public E pollLast() {
        if (isEmpty()){
            return null;
        }
        tail = inc(tail,array.length);
        E e = array[tail];
        array[tail] = null;
        return e;
    }
    public E peekFirst() {
        if (isEmpty()){
            return null;
        }
        return array[head];
    }

    public E peekLast() {
        if (isEmpty()){
            return null;
        }
        return array[inc(tail,array.length)];
    }

    public boolean isEmpty() {
        return head == tail;
    }

    public boolean isFull(){
        if (tail > head){
            return tail - head == array.length - 1;
        }else if(tail < head){
            return head - tail == 1;
        }
        return false;
    }

    public Iterator<E> iterator() {
        return new Iterator<E>() {

            int p = head;
            @Override
            public boolean hasNext() {
                return p != tail;
            }

            @Override
            public E next() {
                E e = array[p];
                p = dec(p,array.length);
                return e;
            }
        };
    }

    private int inc(int index,int length){
        if (index - 1 < 0){
            return length - 1;
        }
        return index - 1;
    }

    private int dec(int index,int length){
        if (index + 1 == length){
            return 0;
        }
        return index + 1;
    }

    private int head = 0;
    private int tail = 0;
    private final E[] array;

    @SuppressWarnings("all")
    public ArrayDeque(int capacity) {
        this.array = (E[])new Object[capacity + 1];
    }
}
