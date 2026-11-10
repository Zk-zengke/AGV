package com.lizhi.method.link;

import java.util.Stack;

public class LinkedListStack<E> extends Stack<E> implements Iterable<E> {



    class Node{

        private E value;
        private Node next;

        public Node() {
        }

        public Node(E value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    private int capacity;
    private int size;
    private Node head = new Node();

    public LinkedListStack(int capacity) {
        this.capacity = capacity;
    }

    /**
     * 在栈顶添加元素
     * @param item   the item to be pushed onto this stack.
     * @return
     */
    @Override
    public E push(E item) {
        if (full()){
            return null;
        }
        head.next = new Node(item,head.next);
        size++;
        return super.push(item);
    }

    /**
     * 查看栈顶元素并删除
     * @return
     */
    @Override
    public synchronized E pop() {
        if (empty()){
            return null;
        }
        Node current = head.next;
        head.next = current.next;
        size--;
        return current.value;
    }

    /**
     * 查看栈顶元素
     * @return
     */
    @Override
    public synchronized E peek() {
        if (empty()){
            return null;
        }
        return head.next.value;
    }

    /**
     * 判断栈是否为空
     * @return
     */
    @Override
    public boolean empty() {
        return size == 0;
    }

    /**
     * 判断是否满栈
     * @return
     */
    private boolean full(){
        return capacity == size;
    }

    /**
     * 打印元素
     */
    public void print(){
        Node p = head.next;
        println(p);
    }

    private void println(Node p){
        if (p == null){
            return;
        }
        System.out.println(p.value);
        println(p.next);
    }

    public int size(){
        return size;
    }
}
