package com.lizhi.method.array;

import java.util.Iterator;
import java.util.Stack;

public class ArrayStack<E> extends Stack<E> implements Iterable<E> {

    private int top = 0;
    private final E[] array;

    @SuppressWarnings("all")
    public ArrayStack(int capacity) {
        this.array = (E[])new Object[capacity];
    }

    @Override
    public E push(E item) {
        if (full()){
            return null;
        }
        array[top++] = item;
        return super.push(item);
    }

    @Override
    public synchronized E pop() {
        if (empty()){
            return null;
        }
        return array[--top];
    }

    @Override
    public synchronized E peek() {
        if (empty()){
            return null;
        }
        return array[top - 1];
    }

    @Override
    public boolean empty() {
        return top == 0;
    }

    public boolean full(){
        return top == array.length;
    }

    public Iterator<E> iterator(){
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return top > 0;
            }

            @Override
            public E next() {
                return array[--top];
            }
        };
    }


}
