package com.lizhi.method.queue;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue<E>{

    private final E[] array;
    private int tail = 0;
    private int head = 0;
    private int size = 0;

    private ReentrantLock lock = new ReentrantLock();
    private Condition headWaits = lock.newCondition();
    private Condition tailWaits = lock.newCondition();



    @Override
    public String toString() {
        return Arrays.toString(array);
    }

    @SuppressWarnings("all")
    public BlockingQueue(int capacity) {
        this.array = (E[]) new Object[capacity];
    }

    public void offer(E e) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (full()){
                tailWaits.await();
            }
            array[tail] = e;
            if (++tail == array.length){
                tail = 0;
            }
            size++;
            headWaits.signal();
        } finally {
            lock.unlock();
        }
    }

    public boolean offer(E e,long timeout) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            long t = TimeUnit.MILLISECONDS.toNanos(timeout);
            while (full()){
                if (t <= 0){
                    return false;
                }
                t = tailWaits.awaitNanos(t);
            }
            array[tail] = e;
            if (++tail == array.length){
                tail = 0;
            }
            size++;
            headWaits.signal();
            return true;
        }finally {
            lock.unlock();
        }
    }

    public E poll() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (empty()){
                headWaits.await();
            }
            E e = array[head];
            array[head] = null;//help GC
            if (++head == array.length){
                head = 0;
            }
            size--;
            tailWaits.signal();
            return e;
        } finally {
            lock.unlock();
        }
    }

    public boolean empty(){
        return size == 0;
    }

    public boolean full(){
        return size == array.length;
    }
}
