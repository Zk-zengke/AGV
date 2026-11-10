package com.lizhi.method.link;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class SinglyLinkedList implements Iterable<Integer>{

    private final ListNode head = new ListNode(666,null);


    /**
     * 往头部添加元素
     * @param value
     */
    public void addFirst(int value){
        insert(0,value);
    }

    /**
     * while遍历
     */
    public void loop(Consumer<Integer> consumer){
        ListNode current = head.next;
        while (current != null){
            consumer.accept(current.val);
            current = current.next;
        }
    }

    /**
     * for遍历
     */
    public void loop1(Consumer<Integer> consumer){
        for (ListNode current = head.next; current != null; current = current.next){
            consumer.accept(current.val);
        }
    }

    /**
     * 迭代器
     */
    private class IteratorNode implements Iterator<Integer>{
        ListNode current = head.next;
        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Integer next() {
            int value = current.val;
            current = current.next;
            return value;
        }
    }
    public Iterator<Integer> iterator(){
        return new IteratorNode();
    }

    /**
     * 寻找最后一个元素
     * @return
     */
    private ListNode findLast(){
        ListNode current = head;
        while (current.next != null){
            current = current.next;
        }
        return current;
    }
    /**
     * 向最后一个元素添加元素
     */
    public void addLast(int value){
        ListNode last = findLast();
        last.next = new ListNode(value,null);
    }

    private ListNode findNode(int index){
        int i = -1;
        for (ListNode current = head;current != null;current = current.next,i++){
            if (i == index){
                return current;
            }
        }
        return null;
    }

    private IllegalArgumentException illegalArgument(int index){
        return new IllegalArgumentException(String.format("index [%d] 不合法%n",index));
    }

    /**
     * 获取对应序号的元素
     * @param index
     * @return
     */
    public int get(Integer index){
        ListNode node = findNode(index);
        if (node != null){
            return node.val;
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
        ListNode beforNode = findNode(index - 1);
        if (beforNode == null){
            throw illegalArgument(index);
        }else {
            beforNode.next = new ListNode(value,beforNode.next);
        }
    }

    /**
     * 移除指定序列的元素
     * @param index
     */
    public void remove(int index){
        ListNode beforNode = findNode(index - 1);
        if (beforNode == null){
            throw illegalArgument(index);
        }
        ListNode current = beforNode.next;
        if (current != null) {
            beforNode.next = current.next;
        }else {
            throw illegalArgument(index);
        }
    }

    /**
     * 打印链表
     * @param node
     */
    public void printNode(ListNode node){
        ArrayList<Object> list = new ArrayList<>();
        while (node != null){
            list.add(node.val);
            node = node.next;
        }
        System.out.println(list);
    }

    /**
     * 将数组转换为链表
     * @param list
     * @return
     */
    public ListNode buildList(int[] list){
        ListNode head = new ListNode(666,null);
        for (int element : list) {
            ListNode last = findLast(head);
            last.next = new ListNode(element, null);
        }
        return head.next;
    }
    /**
     * 查找链表最后一个元素
     * @param node
     * @return
     */
    private ListNode findLast(ListNode node){
        while (node.next != null){
            node = node.next;
        }
        return node;
    }

    /**
     * 将多个数组转换为链表
     * @param lists
     * @return
     */
    public List<ListNode> buildLists(int[][] lists){
        List<ListNode> nodes = new ArrayList<>();
        for (int[] list : lists) {
            ListNode node = buildList(list);
            nodes.add(node);
        }
        return nodes;
    }

    /**
     * 反转单向链表
     * @param list
     * @return
     */
    public ListNode reversal(int[] list){
        if (list == null){
            return null;
        }
        ListNode node = buildList(list);
        ListNode head = new ListNode(777,null);
        while (node != null){
            head.next = new ListNode(node.val,head.next);
            node = node.next;
        }
        return head.next;
    }

    /**
     * 根据值删除节点
     * @param list
     * @return
     */
    public ListNode deleteByValue(int[] list,int value){
        if (list == null){
            return null;
        }
        ListNode node = buildList(list);
        ListNode head = new ListNode(777,null);
        while (node != null){
            if (node.val != value){
                ListNode last = findLast(head);
                last.next = new ListNode(node.val,null);
            }
            node = node.next;
        }
        return head.next;
    }

    /**
     * 计算链表长度
     * @param node
     * @return
     */
    private int calculation(ListNode node){
        int i = 0;
        while (node != null){
            node = node.next;
            i++;
        }
        return i;
    }

    /**
     * 删除倒数节点
     * @param list
     * @param index
     * @return
     */
    public ListNode deleteReciprocalValue(int[] list,int index){
        ListNode node = buildList(list);
        ListNode newNode = node;
        int length = calculation(node);
        int newIndex = length - index;
        if (newIndex == 0){
            return null;
        }
        int i = 0;
        while (node != null){
            if (i == newIndex - 1){
                ListNode current = node.next;
                node.next = current.next;
            }
            node = node.next;
            i++;
        }
        return newNode;
    }

    /**
     * 有序列表去重
     * @return
     */
    public ListNode linkRemoveWeight(int[] list){
        ListNode head = buildList(list);
        ListNode p1 = head;
        ListNode p2 = head.next;
        while (p2 != null){
            if (p1.val == p2.val){
                p1.next = p2.next;
            }else {
                p1 = p1.next;
            }
            p2 = p1.next;
        }
        return head;
    }

    /**
     * 删除链表的相应元素
     * @param node
     * @return
     */
    private ListNode deleteValue(ListNode node,Integer value){
        ListNode p1 = node;
        while (p1.next != null){
            if (p1.next.val == value){
                ListNode current = p1.next;
                p1.next = current.next;
            }else {
                p1 = p1.next;
            }
        }
        return node;
    }

    /**
     * 有序链表去重
     * @param list
     * @return
     */
    public ListNode linkRemove(int[] list){
        ListNode node = buildList(list);
        ListNode p1 = node;
        ListNode p2 = node.next;
        Integer start = 0;
        if (p1.val == p2.val){
            start = 1;
        }
        while (p2 != null){
            if (p1.val == p2.val){
                ListNode newNode = deleteValue(node, p1.val);
                p1 = newNode;
                p2 = newNode.next;
            }else {
                p1 = p1.next;
                p2 = p1.next;
            }
        }
        if (start == 1){
            return node.next;
        }else {
            return node;
        }
    }

    /**
     * 插入元素
     * @param node
     * @param value
     * @return
     */
    private ListNode insertValue(ListNode node,Integer value){
        ListNode p1 = node;
        while (p1.next != null){
            if (p1.next.val >= value){
                p1.next = new ListNode(value, p1.next);
                return node;
            }
            p1 = p1.next;
        }
        p1.next = new ListNode(value,null);
        return node;
    }

    /**
     * 合并有序链表
     * @param list1
     * @param list2
     * @return
     */
    public ListNode mergeLink(int[] list1,int[] list2){
        if (list1.length == 0){
            return buildList(list2);
        }
        ListNode node1 = buildList(list1);
        ListNode node2 = buildList(list2);
        ListNode p1 = node1;
        ListNode p2 = node2;
        while (p2 != null){
            p1 = insertValue(p1, p2.val);
            p2 = p2.next;
        }
        return p1;
    }

    /**
     * 合并多个有序链表
     * @param lists
     * @return
     */
    public ListNode mergeLinks(int[][] lists){
        List<ListNode> nodes = buildLists(lists);
        ListNode sumNode = new ListNode();
        for (ListNode node : nodes) {
            while (node != null){
                sumNode = insertValue(sumNode, node.val);
                node = node.next;
            }
        }
        return sumNode.next;
    }

    /**
     * 寻找链表中间节点
     * @param list
     * @return
     */
    public Integer findMidLink(int[] list){
        ListNode node = buildList(list);
        ListNode p1 = node;
        ListNode p2 = node;
        int i = 1;
        while (p2 != null && p2.next != null){
            p1 = p1.next;
            p2 = p2.next;
            p2 = p2.next;
            i++;
        }
        return i;
    }

    /**
     * 计算链表长度
     * @param node
     * @return
     */
    private Integer calculationLinkLength(ListNode node){
        int i = 0;
        while (node != null){
            node = node.next;
            i++;
        }
        return i;
    }

    private ListNode rowLink(ListNode node,Integer index){
        ListNode p1 = new ListNode();
        int i = 1;
        while (node != null){
            if (i >= index){
                p1.next = new ListNode(node.val,p1.next);
            }
            i++;
            node = node.next;
        }
        return p1.next;
    }

    /**
     * 判断链表是否为回文链表
     * @param list
     * @return
     */
    public boolean palindrome(int[] list){
        ListNode node = buildList(list);
        Integer midLink = findMidLink(list);
        Integer length = calculationLinkLength(node);
        ListNode p1;
        if (length % 2 == 0){
            p1 = rowLink(node, midLink);
        }else {
            p1 = rowLink(node,midLink + 1);
        }
        int i = 1;
        while (i != midLink){
            if (node.val != p1.val){
                return false;
            }
            node = node.next;
            p1 = p1.next;
            i++;
        }
        return true;
    }

    private ListNode findNode(ListNode node,int index){
        int i = 1;
        while (node != null){
            if (i == index){
                return node;
            }
            node = node.next;
            i++;
        }
        return null;
    }

    /**
     * 判断是否有环
     */
    private ListNode haveCycle(ListNode node){
        ListNode p1 = node;//兔子
        ListNode p2 = node;//乌龟
        while (p1 != null && p1.next != null){
            p1 = p1.next;
            p1 = p1.next;
            p2 = p2.next;
            if (p1 == p2){
                return p1;
            }
        }
        return null;
    }

    /**
     * 判断是否为环形链表
     * @param list
     * @return
     */
    public Integer floyd(int[] list){
        ListNode node = buildList(list);
        ListNode last = findLast(node);
        last.next = findNode(node,6);
        ListNode p1 = haveCycle(node);//兔子
        ListNode p2 = node;//乌龟
        while (p1 != null){
            p1 = p1.next;
            p2 = p2.next;
            if (p1 == p2){
                return p1.val;
            }
        }
        return null;
    }

    /**
     * 判断是否共尾
     * @return
     */
    public Integer commontail(int[] list1,int[] list2){
        ListNode node1 = buildList(list1);
        ListNode node2 = buildList(list2);
        ListNode p1 = node1;
        ListNode p2 = node2;
        Integer length1 = calculationLinkLength(node1);
        Integer length2 = calculationLinkLength(node2);
        int i = 1;
        int num1 = -1;
        int num2 = -2;
        while (i <= length1 + length2 + 1){
            if (p1 != null){
                num1 = p1.val;
            }else {
                num1 = -1;
            }
            if (p2 != null){
                num2 = p2.val;
            }else {
                num2 = -2;
            }
            if (num1 == num2){
                return num1;
            }
            if (p1 == null){
                p1 = node2;
            }else {
                p1 = p1.next;
            }
            if (p2 == null){
                p2 = node1;
            }else {
                p2 = p2.next;
            }
            i++;
        }
        return null;
    }

    /**
     * 递归
     * @param node
     */
    private void f(ListNode node){
        if (node == null){
            return;
        }
        System.out.println("befor:" + node.val);
        f(node.next);
        System.out.println("after:" + node.val);
    }

    public void recursion(int[] list){
        ListNode node = buildList(list);
        f(node);
    }
}
