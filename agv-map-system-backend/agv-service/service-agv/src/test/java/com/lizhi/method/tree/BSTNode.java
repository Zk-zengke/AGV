package com.lizhi.method.tree;

public class BSTNode {
    int key;
    Object value;
    BSTNode left;
    BSTNode right;

    public BSTNode(BSTNode left,int key, Object value,BSTNode right) {
        this.key = key;
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public BSTNode(int key, Object value) {
        this.key = key;
        this.value = value;
    }

    public BSTNode() {
    }
}
