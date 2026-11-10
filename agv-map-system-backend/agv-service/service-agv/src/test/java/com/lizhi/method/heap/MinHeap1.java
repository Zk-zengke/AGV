package com.lizhi.method.heap;

import com.lizhi.method.link.ListNode;

public class MinHeap1 {

    ListNode[] array;
    int size = 0;

    public MinHeap1(int capacity) {
        this.array = new ListNode[capacity];
    }

    public void offer(ListNode element) {
        int child = size++;
        int parent = (child - 1) / 2;
        while(child > 0 && array[parent].val > element.val){
            array[child] = array[parent];
            child = parent;
            parent = (child - 1) / 2;
        }
        array[child] = element;
    }

    public ListNode poll(){
        if(isEmpty()){
            return null;
        }
        swap(0,size - 1);
        ListNode e = array[size - 1];
        array[--size] = null;
        down(0);
        return e;
    }

    private void swap(int i,int j){
        ListNode listNode = array[i];
        array[i] = array[j];
        array[j] = listNode;
    }

    private void down(int parent){
        int left = 2 * parent + 1;
        int right = 2 * parent + 2;
        int max = parent;
        if (left < size && array[left].val < array[max].val){
            max = left;
        }
        if (right < size && array[right].val < array[max].val){
            max =right;
        }
        if (max != parent){
            swap(max,parent);
            down(max);
        }
    }

    public ListNode peek(){
        if (isEmpty()){
            return null;
        }
        return array[0];
    }

    public boolean isEmpty(){
        return size == 0;
    }

}