package com.lizhi.method.queue;


public class PriorityQueue<E extends Priority>{

    Priority[] array;
    int size;

    public PriorityQueue(int capacity) {
        this.array = new Priority[capacity];
    }
    public boolean offer(E e) {
        if (isFull()){
            return false;
        }
        array[size++] = e;
        return true;
    }

    /**
     * 返回优先级最高的索引
     * @return
     */
    private int findPriority(){
        int m = 0;
        for (int i = 1; i < size; i++){
            if (array[i].priority() > array[m].priority()){
                m = i;
            }
        }
        return m;
    }

    /**
     * 移除指定索引的元素
     * @param index
     */
    private void removeE(int index){
        if (index < size - 1){
            System.arraycopy(array,index + 1,array,index,size - 1 - index);
        }
        size--;
    }
    @SuppressWarnings("unchecked")
    public E poll() {
        if (isEmpty()){
            return null;
        }
        int priority = findPriority();
        E e = (E) array[priority];
        removeE(priority);
        return e;
    }
    @SuppressWarnings("unchecked")
    public E peek() {
        if (isEmpty()){
            return null;
        }
        int priority = findPriority();
        return (E) array[priority];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull(){
        return size == array.length;
    }
}
