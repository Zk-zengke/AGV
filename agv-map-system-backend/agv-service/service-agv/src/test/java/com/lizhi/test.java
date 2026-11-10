package com.lizhi;

import com.lizhi.domain.po.AgvShape;
import com.lizhi.method.array.ArrayStack;
import com.lizhi.method.array.BinarySearch;
import com.lizhi.method.array.DynamicArray;
import com.lizhi.method.heap.MinHeap1;
import com.lizhi.method.link.*;
import com.lizhi.method.queue.*;
import com.lizhi.method.queue.ArrayDeque;
import com.lizhi.method.tree.*;
import com.lizhi.utils.AgvCollisionAvoidance;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.PriorityQueue;

import static com.alibaba.fastjson.JSONValidator.Type.Array;


public class test {

    private final BinarySearch binarySearch = new BinarySearch();
    private final DynamicArray dynamicArray = new DynamicArray();
    private final SinglyLinkedList singlyLinkedList = new SinglyLinkedList();
    private final DoublyLinkedList doublyLinkedList = new DoublyLinkedList();
    private final LinkedListStack<Object> stack = new LinkedListStack<>(8);


    @Test
    public void towfind(){
        int[] a = {5,7,7,8,8,10};
        int b = 8;
        ArrayList<Integer> integers = binarySearch.mostArray(a, b);
        System.out.println(integers);
    }
    @Test
    public void addFinality(){
        dynamicArray.addFinality(1);
        dynamicArray.addFinality(2);
        dynamicArray.addFinality(3);
        dynamicArray.addFinality(4);
        dynamicArray.addFinality(5);
        dynamicArray.addFinality(6);
        dynamicArray.add(0,9);
        dynamicArray.remove(5);
        //dynamicArray.foreach(System.out::println);
        /*for (Integer element : dynamicArray) {
            System.out.println(element);
        }*/
        dynamicArray.stream().forEach(System.out::println);
    }

    @Test
    public void singlyLink(){
        singlyLinkedList.addFirst(1);
        singlyLinkedList.addLast(2);
        singlyLinkedList.addLast(3);
        singlyLinkedList.addLast(4);
        singlyLinkedList.addLast(5);
        /*singlyLinkedList.loop1(element->{
            System.out.println(element.intValue());
        });*/
        /*for (Integer element : singlyLinkedList) {
            System.out.println(element);
        }*/
        singlyLinkedList.insert(0,9);
        singlyLinkedList.insert(3,33);
        singlyLinkedList.remove(3);
        singlyLinkedList.remove(0);
        singlyLinkedList.remove(2);
        for (Integer i : singlyLinkedList) {
            System.out.println(i);
        }

    }

    @Test
    public void doublyLink(){
        doublyLinkedList.addLast(1);
        doublyLinkedList.addLast(2);
        doublyLinkedList.addLast(3);
        doublyLinkedList.addLast(4);
        doublyLinkedList.addLast(5);
        doublyLinkedList.insert(0,9);
        doublyLinkedList.insert(6,9);
        doublyLinkedList.remove(4);
        doublyLinkedList.loop();
    }

    @Test
    public void doublySentinel(){
        DoublyLinkedListSentinel sentinel = new DoublyLinkedListSentinel();
        sentinel.addLast(1);
        sentinel.addLast(2);
        sentinel.addLast(3);
        sentinel.addLast(4);
        sentinel.addLast(5);
        sentinel.removeByValue(4);
        for (Integer element : sentinel) {
            System.out.println(element);
        }
    }
    @Test
    public void LeetCode(){
        SinglyLinkedList linked = new SinglyLinkedList();
        int[] array1 = {1,2,3};
        linked.recursion(array1);
    }

    @Test
    public void linkedQueue(){
        LinkedListQueue<String> queue = new LinkedListQueue<>(4);
        queue.offer("a");
        queue.offer("b");
        queue.offer("c");
        queue.offer("d");
        queue.poll();
        for (String s : queue) {
            System.out.println(s);
        }
    }

    @Test
    public void arrayQueue(){
        ArrayQueue<String> queue = new ArrayQueue<>(4);
        queue.offer("a");
        queue.offer("b");
        queue.offer("c");
        queue.offer("d");
        queue.poll();
        queue.offer("e");
        for (String s : queue) {
            System.out.println(s);
        }
    }

    @Test
    public void treeNode(){
        /*TreeNode root = new TreeNode(
                new TreeNode(new TreeNode(4),2,new TreeNode(5)),
                1,
                new TreeNode(new TreeNode(6),3,new TreeNode(7))
        );*/
        Solution solution = new Solution();
        //solution.postOrder(root);
    }

    @Test
    public void linkedStack(){
        LinkedListStack<String> stack = new LinkedListStack<>(4);
        System.out.println(stack.empty());
        stack.push("a");
        stack.push("b");
        stack.push("c");
        stack.push("d");
        System.out.println(stack.peek());
        stack.pop();
        stack.pop();
        stack.print();
    }

    @Test
    public void arrayStack(){
        ArrayStack<String> stack = new ArrayStack<>(4);
        stack.push("a");
        stack.push("b");
        stack.push("c");
        stack.push("d");
        System.out.println(stack.peek());
        System.out.println(stack.pop());
    }

    public boolean judge(char c){
        int judge = 0;
        switch(c){
            case ')':
                if (stack.peek().equals('(')) {
                    judge = 1;
                }
                break;
            case '}':
                if (stack.peek().equals('{')){
                    judge = 1;
                }
                break;
            case ']':
                if (stack.peek().equals('[')){
                    judge = 1;
                }
        }
        stack.pop();
        return judge == 1;
    }

    /**
     * 判断是否为有效括号{}()[]
     * @param s
     * @return
     */
    public boolean valid(String s){
        if (s.length() % 2 == 1){
            return false;
        }
        boolean judge = false;
        for (int i = 0; i < s.length();i++){
            char c = s.charAt(i);
            if (c == '[' || c == '{' || c == '('){
                stack.push(c);
            }else {
                judge = judge(c);
            }
        }
        return judge;
    }

    @Test
    public void leetCode20(){
        String s ="([]";
        System.out.println(valid(s));
    }

    private void calculateNum(String o){
        int sum = 0;
        int o1 = (int) stack.pop();
        int o2 = (int) stack.pop();
        sum = switch (o) {
            case "+" -> o2 + o1;
            case "-" -> o2 - o1;
            case "*" -> o2 * o1;
            case "/" -> o2 / o1;
            default -> sum;
        };
        stack.push(sum);
    }

    /**
     * 后缀表达式求值
     * @param arrayList
     * @return
     */
    public int calculate(List<Object> arrayList){
        for (Object o : arrayList) {
            if (o instanceof Number) {
                stack.push(o);
            } else {
                calculateNum((String) o);
            }
        }
       return (int) stack.pop();
    }

    @Test
    public void leetCode120(){
        ArrayList<Object> list = new ArrayList<>();
        list.add(4);
        list.add(13);
        list.add(5);
        list.add("/");
        list.add("+");
        System.out.println(calculate(list));
    }

    /**
     * 中缀转后缀表达式
     * @param list
     * @return
     */
    public Object postfix(List<Object> list){
        ArrayList<Object> list1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            Object o = list.get(i);
            if ( o instanceof Number){
                list1.add(o);
            }else if (o == ")"){
                while (stack.peek() != "("){
                  list1.add(stack.pop());
                }
                stack.pop();
            }else if(o == "("){
                stack.push(o);
            }else {
                if(judgeOperator(o)){
                    while (!stack.empty() && stack.peek() != "("){
                        list1.add(stack.pop());
                    }
                }
                stack.push(o);
            }
        }
        list1.add(stack.pop());
        return list1;
    }

    private boolean judgeOperator(Object o){
        return setpriority(o) <= setpriority(stack.peek());
    }

    private int setpriority(Object o){
        if (o == "+" || o == "-"){
            return 1;
        }else if(o == "*" || o == "/"){
            return 2;
        }else {
            return 0;
        }
        /*return switch(o){
            case '+','-' -> 1;
            case '*','/' -> 2;
            default -> throw new IllegalArgumentException("不合法的运算符:"+o);
        };*/
    }
    @Test
    public void suffix(){
        ArrayList<Object> list = new ArrayList<>();
        list.add(1);
        list.add("*");
        list.add("(");
        list.add(2);
        list.add("+");
        list.add(3);
        list.add(")");
        System.out.println(postfix(list));
    }

    ArrayQueue<Integer> queue = new ArrayQueue<>(100);

    public void push(int x){
        queue.offer(x);
        for (int i = 0; i < queue.size();i++){
            queue.offer(queue.poll());
        }
    }

    public int pop(){
        return queue.poll();
    }

    public int top(){
        return queue.peek();
    }

    public boolean empty(){
        return queue.isEmpty();
    }

    @Test
    public void s(){
        push(1);
        push(2);
        System.out.println(top());
        System.out.println(top());
    }

    @Test
    public void linkedListDeque(){
        //LinkedListDeque<Object> deque = new LinkedListDeque<>(4);
        ArrayDeque<Object> deque = new ArrayDeque<>(4);
        System.out.println(deque.isEmpty());
        deque.offerFirst("2");
        deque.offerFirst("1");
        deque.offerLast("3");
        deque.offerLast("4");
        System.out.println(deque.isFull());
        System.out.println(deque.pollFirst());
        System.out.println(deque.pollLast());
        System.out.println(deque.peekFirst());
        System.out.println(deque.peekLast());
        for (Object o : deque) {
            System.out.println(o);
        }
    }

    @Test
    public void obb(){
        AgvCollisionAvoidance avoidance = new AgvCollisionAvoidance();
        AgvShape agvShape1 = new AgvShape();
        AgvShape agvShape2 = new AgvShape();
        agvShape1.setX(0);
        agvShape1.setY(0);
        agvShape1.setWidth(10);
        agvShape1.setHeight(10);
        agvShape1.setAngle(150);
        agvShape2.setX(9);
        agvShape2.setY(9);
        agvShape2.setWidth(10);
        agvShape2.setHeight(10);
        agvShape2.setAngle(30);
        System.out.println(avoidance.collidesWith(agvShape1, agvShape2));
    }

    @Test
    public void priorityQueue(){
        PriorityQueue2<Priority> queue1 = new PriorityQueue2<>(4);
        System.out.println(queue1.isEmpty());
        queue1.offer(new Entry("a",1));
        queue1.offer(new Entry("b",4));
        queue1.offer(new Entry("c",3));
        queue1.offer(new Entry("d",2));
        System.out.println(queue1.isFull());
        System.out.println(queue1.peek());
    }

    @Test
    public void leetcode(){
        SinglyLinkedList linkedList = new SinglyLinkedList();
        int[][] arrays = {{1,4,5},{1,3,4},{2,6}};
        List<ListNode> nodes = linkedList.buildLists(arrays);
        ListNode listNode = mergeKLists(nodes);
        linkedList.printNode(listNode);

    }

    public ListNode mergeKLists(List<ListNode> lists) {
        MinHeap1 heap = new MinHeap1(100);
        for(int i = 0; i < 3;i++){
            ListNode nodes = lists.get(i);
            while(nodes != null){
                heap.offer(nodes);
                nodes = nodes.next;
            }
        }
        ListNode head = new ListNode();
        ListNode p = head;
        while(!heap.isEmpty()){
            ListNode s = heap.poll();
            p.next = s;
            p = s;
        }
        return head.next;
    }

    private ListNode findLast(ListNode head){
        ListNode current = head;
        if (current.next != null){
            current = current.next;
        }
        return current;
    }

    @Test
    public void blockingQueue() throws InterruptedException {
        BlockingQueue1<String> queue = new BlockingQueue1<>(3);
        Thread t1 = new Thread(()->{
            try{
                System.out.println(System.currentTimeMillis()+":begin");
                queue.offer("任务1");
                System.out.println(queue);
                queue.offer("任务2");
                System.out.println(queue);
                queue.offer("任务3");
                System.out.println(queue);
                queue.offer("任务4",5000);
                System.out.println(queue);
                System.out.println(System.currentTimeMillis() + ":end");
            } catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        },"生产者");
        t1.start();

        Thread.sleep(2000);
        queue.poll();
        try {
            t1.join(); // 等待子线程完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void BSTTree(){
        BSTNode n1 = new BSTNode(2, 2);
        BSTNode n2 = new BSTNode(null,4, 4,new BSTNode(5,5));
        BSTNode n3 = new BSTNode(n1, 3, 3, n2);

        BSTNode n6 = new BSTNode(7, 7);

        BSTTree bstTree = new BSTTree(new BSTNode(n3, 6,6, n6));
        bstTree.put(1,1);
        System.out.println(bstTree.isValidBST());
    }

    @Test
    public void AVLTree(){
        AVLTree avlTree = new AVLTree();
        avlTree.put(1,1);
        avlTree.put(2,2);
        avlTree.put(3,3);
        avlTree.put(4,4);
        avlTree.put(5,5);
        avlTree.remove(3);
        avlTree.put(6,6);
        avlTree.put(7,7);
        System.out.println(avlTree.print());
    }

    @Test
    public void RedBlackTree(){
        RedBlackTree redBlackTree = new RedBlackTree();
        redBlackTree.put(1,1);
        redBlackTree.put(2,2);
        redBlackTree.put(3,3);
        redBlackTree.put(4,4);
        redBlackTree.put(5,5);
        redBlackTree.put(6,6);
        redBlackTree.put(7,7);
        redBlackTree.put(8,8);
        System.out.println(redBlackTree.print());
    }

    @Test
    public void test02(){
        String c = "abcanacascw";
        HashMap<Character,Integer> map = new HashMap<>();
        int begin = 0;
        for (int end = 0; end < c.length(); end++){
            char ch = c.charAt(end);
            if(map.containsKey(ch)){
                begin = Math.max(map.get(ch) + 1,begin);
                map.put(ch,end);
            }else {
                map.put(ch,end);
            }
            System.out.println(c.substring(begin,end+1));
        }

    }

    @Test
    public void test01(){
        String a = "1,2,3";
        List<String> b = new ArrayList<>(Arrays.asList(a.split(",")));
        b.removeIf(code -> code.equals("201010502"));
        b.removeIf(code -> code.equals("201010503"));
        if (b.isEmpty()){
            System.out.println("-----");
        }
    }

    @Test
    public void test(){
        int[] nums = {1,11,2,2,3,3,3};
        Map<Integer,Integer> cnt = new HashMap<>();
        for(int i =0; i < nums.length; i++){
            int x = nums[i];
            if(cnt.containsKey(x)){
                cnt.put(x,cnt.get(x) + 1);
            }else{
                cnt.put(x,1);
            }
        }
        for(int k : cnt.keySet()){
            System.out.println(k + "出现" + cnt.get(k) + "次");
        }
    }

    static class Job implements Comparable<Job>{
        final long time;
        final long deadtime;
        Job(long time,long deadtime){
            this.time = time;
            this.deadtime = deadtime;
        }
        public int compareTo(Job job){
            return Long.compare(deadtime, job.deadtime);
        }
    }

    @Test
    public void test1(){
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Job[] jobs = new Job[n];
        for (int i = 0; i < n; i++) {
            long t = scanner.nextLong();
            long d = scanner.nextLong();
            jobs[i] = new Job(t, d);
        }
        scanner.close();
        Arrays.sort(jobs);
        PriorityQueue<Long> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        long total = 0;
        for (Job job : jobs) {
            total += job.time;
            maxHeap.offer(job.time);
            if (total > job.deadtime) {
                total -= maxHeap.poll();
            }
        }
        System.out.println(maxHeap.size());
    }

    @Test
    public void test2(){
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Job[] jobs = new Job[n];
        for (int i = 0; i < n; i++){
            long time = scanner.nextLong();
            long deadTime = scanner.nextLong();
            jobs[i] = new Job(time,deadTime);
        }
        scanner.close();
        PriorityQueue<Long> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        Arrays.sort(jobs);
        long total = 0;
        for (Job job : jobs) {
            total = total + job.time;
            maxHeap.offer(job.time);
            if(job.deadtime < total){
                total = total - maxHeap.poll();
            }
        }
        System.out.println(maxHeap.size());
        new PriorityQueue<>(Collections.reverseOrder());
    }








}
