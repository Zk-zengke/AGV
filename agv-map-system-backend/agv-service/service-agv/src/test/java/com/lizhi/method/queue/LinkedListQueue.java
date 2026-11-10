package com.lizhi.method.queue;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class LinkedListQueue<E> implements Queue<E>,Iterable<E>{

    private class Node<E>{
        E value;
        Node<E> next;

        public Node(E value, Node<E> next) {
            this.value = value;
            this.next = next;
        }

        public Node() {
        }
    }

    private Node<E> head;
    private Node<E> tail = new Node<>();
    private Integer size = 0;
    private Integer capacity = Integer.MAX_VALUE;

    {
        head = tail;
    }

    public LinkedListQueue(Integer capacity) {
        this.capacity = capacity;
    }

    public LinkedListQueue() {
    }

    @Override
    public int size() {
        Node<E> current = head.next;
        int i = 0;
        while (current != tail){
            i++;
            current = current.next;
        }
        return i;
    }

    @Override
    public boolean isEmpty() {
        return head == tail;
    }

    public boolean isFull(){
        return size > capacity;
    }

    @Override
    public boolean contains(Object o) {
        Node<E> current = head.next;
        while (current != tail){
            if (current.value == o){
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Node<E> current = head;
            @Override
            public boolean hasNext() {
                return current != tail;
            }

            @Override
            public E next() {
                current = current.next;
                return current.value;
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
        Node<E> addNode = new Node<>(e,null);
        tail.next = addNode;
        tail = addNode;
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
        Node<E> current = head.next;
        head.next = current.next;
        return current.value;
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
        return head.next.value;
    }

}
