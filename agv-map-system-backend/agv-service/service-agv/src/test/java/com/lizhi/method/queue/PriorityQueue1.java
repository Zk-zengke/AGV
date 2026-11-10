package com.lizhi.method.queue;

public class PriorityQueue1 <E extends Priority>{

    Priority[] array;
    int size;

    public PriorityQueue1(int capacity) {
        this.array = new Priority[capacity];
    }

    private void insert(E e){
        int m = 0;
        for (int i = size - 1; i >= 0;i--){
            if (array[i].priority() > e.priority()){
                array[i + 1] = array[i];
            }else {
                m = i + 1;
                break;
            }
        }
        array[m] = e;
        size++;
    }

    public boolean offer(E e) {
        if (isFull()){
            return false;
        }
        insert(e);
        return true;
    }
    @SuppressWarnings("unchecked")
    public E poll() {
        if (isEmpty()){
            return null;
        }
        E e = (E) array[size - 1];
        size--;
        array[size] = null;
        return e;
    }
    @SuppressWarnings("unchecked")
    public E peek() {
        if (isEmpty()){
            return null;
        }
        return (E) array[size - 1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull(){
        return size == array.length;
    }
}
