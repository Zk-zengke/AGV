package com.lizhi.method.queue;

public class PriorityQueue2 <E extends Priority>{
    //大顶堆,出最大值
    Priority[] array;
    int size = 0;

    public PriorityQueue2(int capacity) {
        this.array = new Priority[capacity];
    }

    public boolean offer(E e) {
        if (isFull()){
            return false;
        }
        int child = size++;
        int parent = (child - 1) / 2;
        while (child > 0 && array[parent].priority() < e.priority()){
            array[child] = array[parent];
            child = parent;
            parent = (child - 1) / 2;
        }
        array[child] = e;
        return true;
    }
    @SuppressWarnings("unchecked")
    public E poll() {
        if (isEmpty()){
            return null;
        }
        swap(0,size - 1);
        E e = (E) array[--size];
        array[size] = null;
        down(0);
        return e;
    }

    /**
     * 交换索引i和j的值
     * @param i
     * @param j
     */
    private void swap(int i,int j){
        Priority priority = array[i];
        array[i] = array[j];
        array[j] = priority;
    }

    /**
     * 将索引下沉到对应的位置
     * @param parent
     */
    private void down(int parent){
        int left = 2 * parent + 1;
        int right = 2 * parent + 2;
        int min = parent;
        if (left < size && array[left].priority() > array[min].priority()){
            min = left;
        }
        if (right < size && array[right].priority() > array[min].priority()){
            min = right;
        }
        if (min != parent){
            swap(parent,min);
            down(min);
        }
    }
    @SuppressWarnings("unchecked")
    public E peek() {
        if (isEmpty()){
            return null;
        }
        return (E) array[0];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull(){
        return size == array.length;
    }
}
