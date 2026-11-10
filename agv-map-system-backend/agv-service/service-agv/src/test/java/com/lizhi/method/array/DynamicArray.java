package com.lizhi.method.array;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class DynamicArray implements Iterable<Integer>{

    private int size; //逻辑大小
    private int capacity; //容量
    private int [] array;

    /**
     * 向动态数组最后的位置添加元素
     * @param element
     */
    public void addFinality(int element){
        add(size,element);
    }

    /**
     * 向动态数组对应的位置添加元素
     * @param index
     * @param element
     */
    public void add(int index,int element){
        checkAndGrow();
        if (index >= 0 && index < size){
            System.arraycopy(array,index,array,index+1,size-index);
        }
        array[index] = element;
        size++;
    }

    /**
     * 容量检查和扩容
     */
    private void checkAndGrow(){
        if (size == 0){
            capacity = 8;
            array = new int[capacity];
        }else if (size == capacity){
            capacity += capacity >> 1;
            int[] newArray = new int[capacity];
            System.arraycopy(array,0,newArray,0,size);
            array = newArray;
        }
    }

    /**
     * 从动态数组中删除元素
     * @param index
     */
    public int remove(int index){
        int removeNum = array[index];
        System.arraycopy(array,index,array,index-1,size-index);
        size--;
        return removeNum;
    }

    /**
     * 查询元素
     * @param index
     * @return
     */
    public int get(int index){
        return array[index];
    }

    /**
     * 遍历函数
     * @param consumer
     */
    public void foreach(Consumer<Integer> consumer){
        for (int i = 0;i < size;i++){
            consumer.accept(array[i]);
        }
    }


    /**
     * 遍历数组
     * @return
     */
    public Iterator<Integer> iterator(){
        return new Iterator<Integer>() {
            int i = 0;
            @Override
            public boolean hasNext() {
                return i < size;
            }

            @Override
            public Integer next() {
                return array[i++];
            }
        };
    }

    /**
     * 遍历数组
     * @return
     */
    public IntStream stream(){
        return IntStream.of(Arrays.copyOfRange(array,0,size));
    }

}
