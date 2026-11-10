package com.lizhi.method.link;

import java.util.Iterator;

public class DoublyLinkedListSentinel implements Iterable<Integer>{

    static class Node{
        Node prev;
        int value;
        Node next;

        public Node(Node prev, int value, Node next) {
            this.prev = prev;
            this.value = value;
            this.next = next;
        }
    }

    private final Node sentinel = new Node(null,-1,null);

    public DoublyLinkedListSentinel() {
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new nodeIterator();
    }

    class nodeIterator implements Iterator<Integer>{
        Node current = sentinel.next;
        @Override
        public boolean hasNext() {
            return current != sentinel;
        }

        @Override
        public Integer next() {
            int value = current.value;
            current = current.next;
            return value;
        }
    }

    /**
     * 添加第一个元素
     * @param value
     */
    public void addFirst(int value){
        Node a = sentinel;
        Node b = sentinel.next;
        Node node = new Node(null, value, null);
        node.prev = a;
        node.next = b;
        a.next = node;
        b.prev = node;
    }

    /**
     * 添加最后一个元素
     * @param value
     */
    public void addLast(int value){
        Node last = sentinel.prev;
        Node node = new Node(null, value, null);
        node.prev = last;
        node.next = sentinel;
        last.next = node;
        sentinel.prev = node;
    }

    /**
     * 移除第一个元素
     */
    public void removeFirst(){
        Node node = sentinel.next;
        Node next = node.next;
        sentinel.next = next;
        next.prev = sentinel;
    }

    /**
     * 移除最后一个元素
     */
    public void removeLast(){
        Node node = sentinel.prev;
        Node prev = node.prev;
        sentinel.prev = prev;
        prev.next = sentinel;
    }

    /**
     * 根据索引搜寻值
     * @param value
     * @return
     */
    private Node searchByIndex(int value){
        int i = 0;
        for (Node current = sentinel.next;current != sentinel;current = current.next,i++){
            if (i == value){
                return current;
            }
        }
        return null;
    }

    /**
     * 索引不合法报错
     * @param index
     * @return
     */
    private IllegalArgumentException illegalArgument(int index){
        return new IllegalArgumentException(String.format("index [%d] 不合法%n",index));
    }
    /**
     * 根据值删除节点
     * @param index 待删除值索引
     */
    public void removeByValue(int index){
        Node current = searchByIndex(index);
        if (current == null || current == sentinel){
            throw illegalArgument(index);
        }
        Node prev = current.prev;
        Node next = current.next;
        prev.next = next;
        next.prev = prev;
    }

}
