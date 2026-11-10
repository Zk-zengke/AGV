package com.lizhi.method.heap;


class MinHeap{

    int[] array;
    int size = 0;

    public MinHeap(int capacity){
        this.array = new int[capacity];
    }

    private void offer(int element){
        size++;
        array[size - 1] = element;
        up(size - 1);
    }

    private void removeTop(int element){
        array[0] = element;
        down(0);
    }

    private int peek(){
        return array[0];
    }


    private void up(int index){
        int child = index;
        int parent = (child - 1) / 2;
        while(child > 0 && array[parent] > array[child]){
            swap(parent,child);
            child = parent;
            parent = (child - 1) / 2;
        }
    }

    private void down(int index){
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        int min = index;
        if(left < size && array[left] < array[min]){
            min = left;
        }
        if(right < size && array[right] < array[min]){
            min = right;
        }
        if(min != index){
            swap(min,index);
            down(min);
        }
    }

    private void swap(int i,int j){
        int element = array[i];
        array[i] = array[j];
        array[j] = element;
    }

    public int findNumByIndex(int[] nums, int k){
        for(int i = 0; i < k; k++){
            offer(nums[i]);
        }
        for(int j = k; j < nums.length; j++){
            if(peek() < nums[j]){
                removeTop(nums[j]);
            }
        }
        return peek();
    }

    public static void main(String[] args) {
        int[] muns = {3,2,1,5,6,4};
        int k = 2;
        MinHeap minHeap = new MinHeap(10);
        minHeap.findNumByIndex(muns,k);
    }

}
