package com.lizhi.method.queue;

import java.util.Iterator;

public class LinkedListDeque<E> implements Iterable<E> {

    public boolean offerFirst(E e) {
        if (isFull()){
            return false;
        }
        Node<E> added = new Node<>(sentinel, e, sentinel.next);
        sentinel.next.prev = added;
        sentinel.next = added;
        size++;
        return true;
    }

    public boolean offerLast(E e) {
        if (isFull()){
            return false;
        }
        Node<E> added = new Node<>(sentinel.prev, e, sentinel);
        sentinel.prev.next = added;
        sentinel.prev = added;
        size++;
        return true;
    }

    public E pollFirst() {
        Node<E> current = sentinel.next;
        sentinel.next = current.next;
        current.next.prev = sentinel;
        size--;
        return current.value;
    }

    public E pollLast() {
        Node<E> current = sentinel.prev;
        sentinel.prev = current.prev;
        current.prev.next = sentinel;
        size--;
        return current.value;
    }

    public E peekFirst() {
        Node<E> current = sentinel.next;
        sentinel.next = current.next;
        current.next.prev = sentinel;
        return current.value;
    }

    public E peekLast() {
        Node<E> current = sentinel.prev;
        sentinel.prev = current.prev;
        current.prev.next = sentinel;
        return current.value;
    }
    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull(){
        return size == capacity;
    }

    static class Node<E>{
        private Node<E> prev;
        private E value;
        private Node<E> next;

        public Node() {
        }

        public Node(Node<E> prev, E value, Node<E> next) {
            this.prev = prev;
            this.value = value;
            this.next = next;
        }
    }

    Node<E> sentinel = new Node<>();
    int capacity;
    int size;

    public LinkedListDeque(int capacity) {
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        this.capacity = capacity;
    }

    public Iterator<E> iterator(){
        return new Iterator<E>() {
            Node<E> current = sentinel.next;
            @Override
            public boolean hasNext() {
                return current != sentinel;
            }

            @Override
            public E next() {
                E value = current.value;
                current = current.next;
                return value;
            }
        };
    }
}
