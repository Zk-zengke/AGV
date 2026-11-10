package com.lizhi.method.heap;

import java.util.Arrays;

public class MaxHeap {

    int[] array;
    int size;

    public MaxHeap(int capacity) {
        this.array = new int[capacity];
        this.size = 0;
    }

    public MaxHeap(int[] array) {
        this.array = array;
        this.size = array.length;
        heapify();
    }

    /**
     * 查看堆顶元素
     * @return
     */
    public int peek(){
        return array[0];
    }

    /**
     * 查看堆顶元素并删除
     * @return
     */
    public int poll(){
        if (empty()){
            return -1;
        }
        int element = array[0];
        swap(0,size-1);
        array[size-1] = 0;
        size--;
        down(0);
        return element;
    }

    /**
     * 查看指定索引处元素并删除
     * @param index
     * @return
     */
    public int poll(int index){
        int element = array[index];
        swap(index,size - 1);
        array[size-1] = 0;
        size--;
        down(index);
        return element;
    }

    /**
     * 替换堆顶元素
     * @param element
     */
    public void replace(int element){
        array[0] = element;
        down(0);
    }

    /**
     * 在尾部添加元素
     * @param element
     */
    public void offer(int element){
        size++;
        array[size] = element;
        up(size - 1);
    }

    public boolean full(){
        return size == array.length;
    }

    public boolean empty(){
        return size == 0;
    }
    private void heapify(){
        for (int i = size / 2 - 1;i >=0;i--){
            down(i);
        }
    }
    private void down(int index){
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        int max = index;
        if (left < size && array[left] > array[max]){
            max = left;
        }
        if (right < size && array[right] > array[max]){
            max = right;
        }
        if (max != index){
            swap(max,index);
            down(max);
        }
    }

    /**
     * 将索引处元素上浮
     * @param index
     */
    private void up(int index){
        int child = index;
        int parent = (child - 1) / 2;
        while (child > 0 && array[parent] < array[child]){
            swap(parent,child);
            child = parent;
            parent = (child - 1) / 2;
        }
    }

    private void swap(int i,int j){
        int q = array[i];
        array[i] = array[j];
        array[j] = q;
    }

    private void heapSort(){

        while (size > 0) {
            swap(0,size - 1);
            size--;
            down(0);
        }

    }


    public static void main(String[] args) {
        int[] array = {1,2,3,4,5,6,7};
        MaxHeap maxHeap = new MaxHeap(array);
        maxHeap.heapSort();
        System.out.println(Arrays.toString(maxHeap.array));
        /*MaxHeap maxHeap = new MaxHeap(10);
        maxHeap.offer(1);
        maxHeap.offer(9);
        maxHeap.offer(8);
        maxHeap.offer(10);
        maxHeap.offer(6);
        maxHeap.offer(4);
        maxHeap.replace(1);
        System.out.println(maxHeap.poll(1));
        System.out.println(Arrays.toString(maxHeap.array));*/
    }
}
