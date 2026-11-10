package com.lizhi.method.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AVLTree {

    AVLNode root;

    static class AVLNode{
        int key;
        Object value;
        AVLNode left;
        AVLNode right;
        int height = 1;

        public AVLNode() {
        }

        public AVLNode(int key, Object value) {
            this.key = key;
            this.value = value;
        }

        public AVLNode(int key, Object value, AVLNode left, AVLNode right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }


    private int height(AVLNode node){
        return node == null ? 0:node.height;
    }

    private void updateHeight(AVLNode node){
        if (node == null){
            return;
        }
        node.height = Integer.max(height(node.left),height(node.right)) + 1;
    }

    private int bf(AVLNode node){
        return height(node.left) - height(node.right);
    }

    private AVLNode leftRotate(AVLNode node){
        AVLNode yellow = node.left;
        AVLNode green = yellow.right;
        yellow.right = node;
        node.left = green;
        return yellow;
    }

    private AVLNode leftRightRotate(AVLNode node){
        node.left = rightRotate(node.left);
        return leftRotate(node);
    }

    private AVLNode rightLeftRotate(AVLNode node){
        node.right = leftRotate(node.right);
        return rightRotate(node);
    }
    private AVLNode rightRotate(AVLNode node){
        AVLNode yellow = node.right;
        AVLNode green = yellow.left;
        yellow.left = node;
        node.right = green;
        return yellow;
    }

    private AVLNode balance(AVLNode node){
        if(node == null){
            return null;
        }
        int bf = bf(node);
        if(bf > 1 && bf(node.left) > 0){
            return leftRotate(node);
        }else if(bf > 1 && bf(node.left) <= 0){
            return leftRightRotate(node);
        }else if(bf < -1 && bf(node.right) < 0){
            return rightRotate(node);
        }else if(bf < -1 && bf(node.right) >= 0){
            return rightLeftRotate(node);
        }
        return node;
    }

    /**
     * 添加数值
     * @param key
     * @param value
     */
    public void put(int key,Object value){
        root = doPut(root,key,value);
    }

    private AVLNode doPut(AVLNode node, int key, Object value) {
        if (node == null){
            return new AVLNode(key,value);
        }
        if (key < node.key){
            node.left = doPut(node.left,key,value);
        }else if(key > node.key){
            node.right = doPut(node.right,key,value);
        }else {
            node.value = value;
        }
        updateHeight(node);
        return balance(node);
    }

    /**
     * 删除数值
     * @param key
     */
    public void remove(int key){
        root = doRemove(root,key);
    }

    private AVLNode doRemove(AVLNode node,int key){
        if (node == null){
            return null;
        }
        if (key < node.key){
            node.left = doRemove(node.left,key);
        }else if(key > node.key){
            node.right = doRemove(node.right,key);
        }else{
            if (node.left == null){
                node = node.right;
            }else if(node.right == null){
                node = node.left;
            }else {
                AVLNode p = node.right;
                while (p.left != null){
                    p = p.left;
                }
                p.right = doRemove(node.right,p.key);
                p.left = node.left;
                node = p;
            }
        }
        updateHeight(node);
        return balance(node);
    }

    /**
     * 打印
     * @return
     */
    public List<Object> print(){
        AVLNode p = root;
        LinkedList<AVLNode> list = new LinkedList<>();
        ArrayList<Object> result = new ArrayList<>();
        while (p != null || !list.isEmpty()){
            if (p != null){
                list.push(p);
                p = p.left;
            }else {
                AVLNode pop = list.pop();
                result.add(pop.value);
                p = pop.right;
            }
        }
        return result;
    }

}
