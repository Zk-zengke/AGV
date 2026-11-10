package com.lizhi.method.array;

import java.util.ArrayList;

public class BinarySearch {

    /**
     * 二分查找基础版
     * @param a
     * @param b
     * @return
     */
    public int findtarget(int[] a,int b){
        int i = 0;
        int j = a.length;
        while (i < j){
            int m = (i+j)/2;
            if (b < a[m]){
                j = m;
            }else if (a[m] < b){
                i = m + 1;
            }else {
                return m;
            }
        }
        return -(i+1);
        //return -1;
    }

    /**
     * 插入元素
     * @param a
     * @param b
     * @return
     */
    public int[] binarySearch(int[] a,int b){
        int targetIndex = findtarget(a, b);
        int[] c = new int[a.length + 1];
        if (targetIndex < 0){
            int index = Math.abs(targetIndex);
            System.arraycopy(a,0,c,0,index-1);
            c[index - 1] = b;
            int i = c.length - index;
            if (i != 0){
                System.arraycopy(a,index - 1,c,index,c.length - index);
            }
            return c;
        }
        return null;
    }

    /**
     * 二分查找最左边版
     * @param a
     * @param b
     * @return
     */
    public int leftMost(int[] a,int b){
        int i = 0;
        int j = a.length;
        int candidate = -1;
        while (i < j){
            int m = (i+j)/2;
            if (b < a[m]){
                j = m;
            }else if (a[m] < b){
                i = m + 1;
            }else {
                candidate = m;
                j = m - 1;
            }
        }
        return candidate;
    }

    /**
     * 二分查找最右边版
     * @param a
     * @param b
     * @return
     */
    public int rightMost(int[] a,int b){
        int i = 0;
        int j = a.length;
        int candidate = -1;
        while (i < j){
            int m = (i+j)/2;
            if (b < a[m]){
                j = m;
            }else if (a[m] < b){
                i = m + 1;
            }else if(a[m] == b){
                candidate = m;
                i = m + 1;
            }
        }
        return candidate;
    }

    /**
     * 二分查找开始点位和结束点位
     * @param a
     * @param b
     * @return
     */
    public ArrayList<Integer> mostArray(int[] a, int b){
        ArrayList<Integer> list = new ArrayList<>();
        int i1 = leftMost(a, b);
        int i2 = rightMost(a, b);
        list.add(i1);
        list.add(i2);
        return list;
    }
}
