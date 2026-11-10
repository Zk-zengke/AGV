package com.lizhi.method.link;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DoublyLinkedList implements Iterable<Integer>{

    private Node head;//头
    private Node tail;//尾

    public static class Node{
        Node prev;
        int value;
        Node next;

        public Node(Node prev, int value, Node next) {
            this.prev = prev;
            this.value = value;
            this.next = next;
        }

        public Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    public DoublyLinkedList() {
        head = new Node(null,666,null);
        tail = new Node(null,888,null);
        head.next = tail;
        tail.prev = head;
    }

    /**
     * 往头部添加元素
     * @param value
     */
    public void addFirst(int value){
        Node next = head.next;
        Node node = new Node(head, value, next);
        head.next = node;
        next.prev = node;
    }

    public Iterator<Integer> iterator(){
        return new nodeIterator();
    }

    private class nodeIterator implements Iterator<Integer>{
        Node current = head.next;
        @Override
        public boolean hasNext() {
            return current != tail;
        }

        @Override
        public Integer next() {
            Integer value = current.value;
            current = current.next;
            return value;
        }
    }

    /**
     * 递归遍历
     */
    public void loop(){
        recursion(head.next);
    }

    private void recursion(Node current){
        if (current == tail){
            return;
        }
        System.out.println(current.value);
        recursion(current.next);
    }

    /**
     * 寻找最后一个元素
     * @return
     */
    /*private Node findLast(){
        Node current = head;
        while (current.next != tail){
            current = current.next;
        }
        return current;
    }*/

    /**
     * 向最后一个元素添加元素
     */
    public void addLast(int value){
        Node last = tail.prev;
        Node node = new Node(last, value, tail);
        last.next = node;
        tail.prev = node;
    }

    /**
     * 根据索引查找元素
     * @param index
     * @return
     */
    private Node findNode(int index){
        int i = -1;
        for (Node current = head; current != tail; current = current.next,i++){
            if (i == index){
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
     * 获取对应序号的元素
     * @param index
     * @return
     */
    public int get(Integer index){
        Node node = findNode(index);
        if (node != null){
            return node.value;
        }else {
            throw illegalArgument(index);
        }
    }

    /**
     * 向指定序列号插入元素
     * @param index
     * @param value
     */
    public void insert(int index,int value){
        Node beforNode = findNode(index - 1);
        if (beforNode == null){
            throw illegalArgument(index);
        }
        Node current = beforNode.next;
        if (current == null){
            throw illegalArgument(index);
        }
        Node node = new Node(beforNode, value, current);
        beforNode.next = node;
        current.prev = node;
    }

    /**
     * 移除指定序列的元素
     * @param index
     */
    public void remove(int index){
        Node beforNode = findNode(index - 1);
        if (beforNode == null){
            throw illegalArgument(index);
        }
        Node current = beforNode.next;
        Node nextNode = current.next;
        if (nextNode == null) {
            throw illegalArgument(index);
        }
        beforNode.next = nextNode;
        nextNode.prev = beforNode;
    }

    /**
     * 反转单向链表
     * @param node
     * @return
     */
    public Node reversal(Node node){
        Node head = new Node(null, 777, null);
        return null;
    }

    /**
     * 根据值删除节点
     *
     * @param value
     * @return
     */
    public Object[] deleteByValue(int[] list, int value){
        if (list == null){
            return null;
        }
        for (Integer element : list) {
            Node prev = tail.prev;
            Node node = new Node(null, element, null);
            node.prev = prev;
            node.next = tail;
            prev.next = node;
            tail.prev = node;
        }
        Node current = head.next;
        while (current != tail){
            if (current.value == value){
                Node prev = current.prev;
                Node next = current.next;
                prev.next = next;
                next.prev = prev;
            }
            current = current.next;
        }
        ArrayList<Integer> newList = new ArrayList<>();
        int i = 0;
        for (Node recent = head.next;recent != tail;recent = recent.next,i++){
            newList.add(recent.value);
        }
        return new ArrayList[]{newList};
    }

    /**
     * 删除倒数节点
     * @param list
     * @param index
     * @return
     */
    public Object[] deleteReciprocalValue(int[] list,int index){
        for (int element : list) {
            Node prev = tail.prev;
            Node node = new Node(prev, element, tail);
            prev.next = node;
            tail.prev = node;
        }
        int newIndex = list.length - index;
        int i = 0;
        for (Node current = head.next;current != tail; current = current.next,i++){
            if (i == newIndex){
                Node prev = current.prev;
                Node next = current.next;
                prev.next = next;
                next.prev = prev;
            }
        }
        Node recent = head.next;
        ArrayList<Integer> newList = new ArrayList<>();
        while (recent != tail){
            newList.add(recent.value);
            recent = recent.next;
        }
        return new ArrayList[]{newList};
    }

    /**
     * 有序列表去重
     * @return
     */
    public Object[] orderLinkRemoveWeight(int[] list){
        for (int element : list) {
            Node prev = tail.prev;
            Node node = new Node(prev, element, tail);
            prev.next = node;
            tail.prev = node;
        }
        for (Node current = head.next;current != tail;current = current.next){
            int value = current.value;
            int nextValue = current.next.value;
            if (value == nextValue){
                Node prev = current.prev;
                Node next = current.next;
                prev.next = next;
                next.prev = prev;
            }
        }
        Node recent = head.next;
        ArrayList<Integer> newList = new ArrayList<>();
        while (recent != tail){
            newList.add(recent.value);
            recent = recent.next;
        }
        return new ArrayList[]{newList};
    }
    /**
     * 有序列表去重(一个不留)
     * @return
     */
    public Object[] orderLinkRemove(int[] list){
        for (int element : list) {
            Node prev = tail.prev;
            Node node = new Node(prev, element, tail);
            prev.next = node;
            tail.prev = node;
        }
        Node current = head.next;
        int value = current.value;
        int nextValue = current.next.value;
        while (current.next != tail){
            if (value == nextValue){
                deleteByValue(value);
            }
            current = current.next;
            value = current.value;
            nextValue = current.next.value;
        }
        Node recent = head.next;
        ArrayList<Integer> newList = new ArrayList<>();
        while (recent != tail){
            newList.add(recent.value);
            recent = recent.next;
        }
        return new ArrayList[]{newList};
    }

    private void deleteByValue(int value){
        for (Node current = head.next;current != tail;current=current.next){
            if (current.value == value){
                Node prev = current.prev;
                Node next = current.next;
                prev.next = next;
                next.prev = prev;
            }
        }
    }

    /**
     * 合并有序链表
     * @param list1
     * @param list2
     * @return
     */
    public Object[] mergeLink(int[] list1, int[] list2){
        for (int element : list1) {
            Node prev = tail.prev;
            Node node = new Node(prev, element, tail);
            prev.next = node;
            tail.prev = node;
        }
        for (int num : list2) {
            insertValue(num);
        }
        ArrayList<Integer> list = new ArrayList<>();
        for (Node recent = head.next;recent != tail;recent = recent.next){
            list.add(recent.value);
        }
        return new ArrayList[]{list};
    }

    private void insertValue(int value){
        Node current = head.next;
        if (current == tail){
            Node prev = current.prev;
            Node node = new Node(prev, value, current);
            prev.next = node;
            current.prev = node;
        }
        while (current != tail){
            if (value <= current.value){
                Node prev = current.prev;
                Node node = new Node(prev, value, current);
                prev.next = node;
                current.prev = node;
                return;
            }
            current = current.next;
        }
    }

    /**
     * 合并多个有序列表
     * @param lists
     * @return
     */
    public Node merge(List<Node> lists){
        return null;
    }

}
