package com.lizhi.method.hash;

import cn.hutool.core.convert.Convert;

public class HashTable {

    static class Entry{
        int hash;
        Object key;
        Object value;
        Entry next;

        public Entry(int hash, Object key, Object value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }

        public Entry() {
        }


    }

    Entry[] table = new Entry[8];
    int size = 0;
    float loadFactor = 0.75f;
    int threshold = Convert.toInt(table.length*loadFactor,0);

    /**
     * 根据key查找value
     * @param hash
     * @param key
     * @return
     */
    Object get(int hash,Object key){
        int index = hash & (table.length - 1);
        Entry p = table[index];
        while(p != null){
            if(p.key.equals(key)){
                return p.value;
            }
            p = p.next;
        }
        return null;
    }

    /**
     * 添加新的值
     * @param hash
     * @param key
     * @param value
     */
    void put(int hash, Object key,Object value){
        int index = hash & (table.length - 1);
        Entry p = table[index];
        Entry parent = null;
        while (p != null){
            if(p.key.equals(key)){
                p.value = value;
                return;
            }
            parent = p;
            p = p.next;
        }
        if (parent!= null){
            parent.next = new Entry(hash,key,value);
        }else {
            table[index] = new Entry(hash,key,value);
        }
        size++;
        if(size > threshold){
            resize();
        }
    }

    private void resize(){
        Entry[] newTable = new Entry[table.length << 1];
        int length = table.length;
        for (int i = 0; i < length; i++){
            Entry p = table[i];
            Entry a = null;
            Entry b = null;
            Entry aHead = null;
            Entry bHead = null;
            while (p != null){
                if(p.hash < length){
                    if(a != null){
                        a.next = p;
                    }else {
                        aHead = p;
                    }
                    a = p;
                }else {
                    if(b != null){
                        b.next = p;
                    }else {
                        bHead = p;
                    }
                    b = p;
                }
                p = p.next;
            }
            if(a != null){
                a.next = null;
                newTable[i] = aHead;
            }
            if(b != null){
                b.next = null;
                newTable[i + table.length] = bHead;
            }
        }
        table = newTable;
        threshold = Convert.toInt(newTable.length*loadFactor,0);
    }



}
