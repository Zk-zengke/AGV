package com.lizhi.method.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RedBlackTree {

    enum Color{
        RED,BLACK;
    }

    Node root;

    static class Node{
        int key;
        Object value;
        Node left;
        Node right;
        Node parent;
        Color color = Color.RED;

        public Node(int key) {
            this.key = key;
        }

        public Node(int key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Node(int key, Color color) {
            this.key = key;
            this.color = color;
        }

        public Node(int key, Color color, Node left, Node right) {
            this.key = key;
            this.color = color;
            this.left = left;
            this.right = right;
        }

        boolean isLeftChild(){
            return parent != null && parent.left == this;
        }
        //节点的叔叔
        Node uncle(){
            if (parent == null || parent.parent == null){
                return null;
            }
            if(parent.isLeftChild()){
                return parent.parent.right;
            }else {
                return parent.parent.left;
            }
        }
        //节点的兄弟
        Node sibling(){
            if(parent == null){
                return null;
            }
            if(this.isLeftChild()){
                return parent.right;
            }else {
                return parent.left;
            }
        }
    }

    boolean isRed(Node node){
        return node != null && node.color == Color.RED;
    }

    boolean isBlack(Node node){
        return node == null || node.color == Color.BLACK;
    }

    private void rightRotate(Node node){
        Node yellow = node.left;
        Node green = yellow.right;
        Node parent = node.parent;
        yellow.right = node;
        node.left = green;
        if(parent == null){
            root = yellow;
        }else if(node.isLeftChild()){
            parent.left = yellow;
        }else {
            parent.right = yellow;
        }
        if (green != null) {
            green.parent = node;
        }
        yellow.parent = parent;
        node.parent = yellow;


    }

    private void leftRotate(Node node){
        Node yellow = node.right;
        Node green = yellow.left;
        Node parent = node.parent;
        yellow.left = node;
        node.right = green;
        if(parent == null){
            root = yellow;
        }else if(node.isLeftChild()){
            parent.left = yellow;
        }else {
            parent.right = yellow;
        }
        if(green != null){
            green.parent = node;
        }
        yellow.parent = parent;
        node.parent = yellow;
    }

    /**
     * 新增或替换
     * @param key
     * @param value
     */
    public void put(int key,Object value){
        Node p = root;
        Node parent = null;
        while(p != null){
            parent = p;
            if (key < p.key){
                p = p.left;
            }else if(key > p.key){
                p = p.right;
            }else {
                p.value = value;
                return;
            }
        }
        Node insert = new Node(key, value);
        if (parent == null){
            root = insert;
        }else if(key < parent.key){
            parent.left = insert;
            insert.parent = parent;
        }else {
            parent.right = insert;
            insert.parent = parent;
        }
        disColor(insert);
    }

    private void disColor(Node insert) {
        //当前节点为根节点
        if (insert == root){
            insert.color = Color.BLACK;
            return;
        }
        //当前节点的父亲为黑色
        Node parent = insert.parent;
        if (isBlack(parent)){
            return;
        }
        //当前节点的父亲和叔叔都是红色
        Node uncle = insert.uncle();
        Node grandParent = parent.parent;
        if (isRed(uncle)){
            parent.color = Color.BLACK;
            uncle.color = Color.BLACK;
            grandParent.color = Color.RED;
            disColor(grandParent);
            return;
        }
        if (parent.isLeftChild() && insert.isLeftChild()){
            parent.color = Color.BLACK;
            grandParent.color = Color.RED;
            rightRotate(grandParent);
        }else if(parent.isLeftChild()){
            leftRotate(parent);
            insert.color = Color.BLACK;
            grandParent.color = Color.RED;
            rightRotate(grandParent);
        }else if(!insert.isLeftChild()){
            parent.color = Color.BLACK;
            grandParent.color = Color.RED;
            leftRotate(grandParent);
        }else {
            rightRotate(parent);
            insert.color = Color.BLACK;
            grandParent.color = Color.RED;
            leftRotate(grandParent);
        }
    }

    /**
     * 删除
     * @param key
     */
    public void remove(int key){
        Node deleteNode = findDeleteNode(key);
        if (deleteNode == null){
            return;
        }
        doDelete(deleteNode);
    }

    private void doDelete(Node deleteNode) {
        Node remainNode = remainNode(deleteNode);
        Node parent = deleteNode.parent;
        //没有孩子
        if (remainNode == null){
            if (deleteNode == root){
                root = null;
            }else {
                if(isBlack(deleteNode)){
                    fixDoubleBlack(deleteNode);
                    //复杂处理
                }else {
                    //无需处理
                }
                if (deleteNode.isLeftChild()){
                    parent.left = null;
                }else{
                    parent.right = null;
                }
                deleteNode.parent = null;
            }
            return;
        }
        //只有一个孩子
        if (deleteNode.left == null || deleteNode.right == null){
            if (deleteNode == root){
                root.key = remainNode.key;
                root.value = remainNode.value;
                root.left = null;
                root.right = null;
            }else {
                if (deleteNode.isLeftChild()){
                    parent.left = remainNode;
                }else{
                    parent.right = remainNode;
                }
                remainNode.parent = parent;
                deleteNode.left = deleteNode.right = deleteNode.parent = null;
                if (isBlack(deleteNode) && isBlack(remainNode)){
                    //复杂处理
                    fixDoubleBlack(deleteNode);
                }else if (isBlack(deleteNode)){
                    remainNode.color = Color.BLACK;
                }else {
                    //无需处理
                }
            }
            return;
        }
        //有两个孩子 ->只有一个孩子或无孩子
        int key = deleteNode.key;
        deleteNode.key = remainNode.key;
        remainNode.key = key;

        Object value = deleteNode.value;
        deleteNode.value = remainNode.value;
        remainNode.value = value;
        doDelete(remainNode);
    }

    private void fixDoubleBlack(Node deleteNode) {
        Node parent = deleteNode.parent;
        Node sibling = deleteNode.sibling();
        if (isRed(sibling)){
            if (sibling.isLeftChild()){
                rightRotate(sibling);
            }else {
                leftRotate(sibling);
            }
            parent.color = Color.RED;
            sibling.color = Color.BLACK;
            fixDoubleBlack(deleteNode);
        }else if (isRed(sibling.right) || isRed(sibling.left)){

        }else {
            sibling.color = Color.RED;
            if (isRed(parent)){
                parent.color = Color.BLACK;
            }else {
                fixDoubleBlack(parent);
            }
        }
    }

    private Node findDeleteNode(int key){
        Node p = root;
        while (p != null){
            if(key < p.key){
                p = p.left;
            }else if(key > p.key){
                p = p.right;
            }else {
                return p;
            }
        }
        return null;
    }

    private Node remainNode(Node node){
        if (node.left == null && node.right == null){
            return null;
        }
        if (node.left == null){
            return node.right;
        }
        if (node.right == null){
            return node.left;
        }
        Node p = node.right;
        while (p.left != null){
            p = p.left;
        }
        return p;
    }

    /**
     * 打印
     * @return
     */
    public List<Object> print(){
        Node p = root;
        LinkedList<Node> list = new LinkedList<>();
        ArrayList<Object> result = new ArrayList<>();
        while (p != null || !list.isEmpty()){
            if (p != null){
                list.push(p);
                p = p.left;
            }else {
                Node pop = list.pop();
                result.add(pop.value);
                p = pop.right;
            }
        }
        return result;
    }
}
