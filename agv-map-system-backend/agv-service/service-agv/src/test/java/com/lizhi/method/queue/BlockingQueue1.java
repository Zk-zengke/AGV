package com.lizhi.method.queue;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue1<E> {
    private E[] array;
    private final AtomicInteger size = new AtomicInteger(0);
    private int tail = 0;
    private int head = 0;
    private ReentrantLock headLock = new ReentrantLock();
    private Condition headWaits = headLock.newCondition();
    private ReentrantLock tailLock = new ReentrantLock();
    private Condition tailWaits = tailLock.newCondition();

    @SuppressWarnings("all")
    public BlockingQueue1(int capacity) {
        this.array = (E[])new Object[capacity];
    }

    public void offer(E e) throws InterruptedException {
        tailLock.lockInterruptibly();
        int c;
        try {
            while (full()){
                tailWaits.await();
            }
            array[tail] = e;
            if (++tail == array.length){
                tail = 0;
            }
            c = size.getAndDecrement();
            //队列不满，但不是从不满到满,唤醒其他线程
            if (c + 1 <array.length){
                tailWaits.signal();
            }
        } finally {
            tailLock.unlock();
        }
        //队列从0到非空时，唤醒poll队列
        headLock.lockInterruptibly();
        if (c == 0) {
            try {
                headWaits.signal();
            }finally {
                headLock.unlock();
            }
        }
    }

    public E poll() throws InterruptedException {
        E e;
        int c;
        headLock.lockInterruptibly();
        try {
            while (empty()){
                headWaits.await();
            }
            e = array[head];
            array[head] = null;//help GC
            if (++head == array.length){
                head = 0;
            }
            c = size.getAndIncrement();
            //队列还有剩余元素，唤醒其他队列
            if (c > 0){
                headWaits.signal();
            }
        } finally {
            headLock.unlock();
        }
        //队列从满到不满时，唤醒offer队列
        tailLock.lockInterruptibly();
        if (c == array.length) {
            try {
                tailWaits.signal();
            } finally {
                tailLock.unlock();
            }
        }
        return e; // Return the element
    }

    public boolean offer(E e,long timeout) throws InterruptedException {
        tailLock.lockInterruptibly();
        int c;
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
            c = size.getAndDecrement();
            //队列不满，但不是从不满到满，唤醒其他线程
            if (c + 1 < array.length){
                tailWaits.signal();
            }
        }finally {
            tailLock.unlock();
        }
        //队列从没有元素到有第一个元素，唤醒poll
        if (c == 0) {
            headLock.lockInterruptibly();
            try {
                headWaits.signal();
            } finally {
                headLock.unlock();
            }
        }
        return true;
    }

    public boolean empty(){
        return size.get() == 0;
    }

    public boolean full(){
        return size.get() == array.length;
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }
}

