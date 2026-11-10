package com.lizhi.method.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BSTTree {

    BSTNode root;

    public BSTTree(BSTNode root) {
        this.root = root;
    }

    /**
     * 获取当前key的值
     * @param key
     * @return
     */
    public Object get(int key){
        BSTNode node = root;
        while (node != null){
            if (key < node.key){
                node = node.left;
            }else if(key > node.key){
                node = node.right;
            }else {
                return node.value;
            }
        }
        return null;
    }

    /**
     * 查找最小值
     * @return
     */
    public Object min(){
        return doMin(root);
    }

    private Object doMin(BSTNode root){
        if (root == null){
            return null;
        }
        if (root.left == null){
            return root.value;
        }
        return doMin(root.left);
    }

    /**
     * 寻找最大值
     * @return
     */
    public Object max(){
        return doMax(root);
    }

    private Object doMax(BSTNode root){
        if (root == null) {
            return null;
        }
        if (root.right == null){
            return root.value;
        }
        return doMax(root.right);
    }

    /**
     * 已有key，更新值。未有key，新增值。
     * @param key
     * @param value
     */
    public void put(int key,Object value){
        BSTNode node = root;
        BSTNode parent = null;
        while (node != null){
            parent = node;
            if (key < node.key){
                node = node.left;
            }else if(key > node.key){
                node = node.right;
            }else {
                node.value = value;
                return;
            }
        }
        if (parent != null && key < parent.key){
            parent.left = new BSTNode(key, value);
        }else if(parent != null) {
            parent.right = new BSTNode(key, value);
        }
    }

    /**
     * 查找当前值的前一个值
     * @param key
     * @return
     */
    public Object successor(int key){
        BSTNode node = root;
        BSTNode ancestor = null;
        while (node!= null){
            if (key < node.key){
                node = node.left;
            }else if(key > node.key){
                ancestor = node;
                node = node.right;
            }else {
                break;
            }
        }
        if (node == null){
            return null;
        }
        if (node.left != null){
            return doMax(node.left);
        }
        return ancestor != null ? ancestor.value:null;
    }

    /**
     * 查找当前值的后一个值
     * @param key
     * @return
     */
    public Object predecessor(int key){
        BSTNode node = root;
        BSTNode ancestor = null;
        while (node != null){
            if (key < node.key){
                ancestor = node;
                node = node.left;
            }else if (key > node.key){
                node = node.right;
            }else {
                break;
            }
        }
        if (node == null){
            return null;
        }
        if (node.right != null){
            return doMin(node.right);
        }
        return ancestor != null ? ancestor.value:null;
    }

    /**
     * 删除指定键的值
     * @param key
     */
    public boolean delete(int key){
        BSTNode node = root;
        BSTNode parent = null;
        while (node != null){
            if (key < node.key){
                parent = node;
                node = node.left;
            }else if(key > node.key){
                parent = node;
                node = node.right;
            }else {
                break;
            }
        }
        if (node == null){
            return false;
        }else if (node.left == null){
            shift(parent,node,node.right);
        }else if (node.right == null){
            shift(parent,node,node.left);
        }else{
            BSTNode s = node.right;
            BSTNode sParent = node;
            while (s.left != null){
                sParent = s;
                s = s.left;
            }
            if (sParent != node){
                shift(sParent,s,s.right);
                s.right = node.right;
            }
            shift(parent,node,s);
            s.left = node.left;
        }
        return true;
    }

    private void shift(BSTNode parent, BSTNode node, BSTNode child) {
        if (parent == null){
            root = child;
        }else if (node == parent.left){
            parent.left = child;
        }else{
            parent.right = child;
        }
    }

    /**
     * 查找小于key的值
     * @param key
     * @return
     */
    public List<Object> less(int key){
        ArrayList<Object> result = new ArrayList<>();
        LinkedList<BSTNode> linkedList = new LinkedList<>();
        BSTNode p = root;
        while (p != null || !linkedList.isEmpty()){
            if (p != null){
                linkedList.push(p);
                p = p.left;
            }else {
                BSTNode pop = linkedList.pop();
                if (pop.key < key){
                    result.add(pop.value);
                }else {
                    break;
                }
                p = pop.right;
            }
        }
        return result;
    }

    /**
     * 找所有大于key的值
     * @return
     */
    public List<Object> greater(int key){
        ArrayList<Object> result = new ArrayList<>();
        LinkedList<BSTNode> list = new LinkedList<>();
        BSTNode p = root;
        while (p != null || !list.isEmpty()){
            if (p != null){
                list.push(p);
                p = p.right;
            }else {
                BSTNode pop = list.pop();
                if (pop.key > key){
                    result.add(pop.value);
                }else {
                    break;
                }
                p = pop.left;
            }
        }
        return result;
    }

    /**
     * 查找大于key1且小于key2的值
     * @param key1
     * @param key2
     * @return
     */
    public List<Object> between(int key1,int key2){
        ArrayList<Object> result = new ArrayList<>();
        LinkedList<BSTNode> list = new LinkedList<>();
        BSTNode p = root;
        while(p != null || !list.isEmpty()){
            if (p!= null){
                list.push(p);
                p = p.left;
            }else {
                BSTNode pop = list.pop();
                if (pop.key <= key2 && pop.key >= key1){
                    result.add(pop.value);
                }
                p = pop.right;
            }
        }
        return result;
    }
    public boolean isValidBST(){
        BSTNode p = root;
        Long pre = Long.MIN_VALUE;
        LinkedList<BSTNode> list = new LinkedList<>();
        while(p != null || !list.isEmpty()){
            if (p != null){
                list.push(p);
                p = p.left;
            }else {
                BSTNode pop = list.pop();
                if (pre >= pop.key){
                    return false;
                }
                pre = Long.valueOf(pop.value.toString());
                p = pop.right;
            }
        }
        return true;
    }
}
