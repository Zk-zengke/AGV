package com.lizhi.method.tree;

import com.lizhi.method.link.LinkedListStack;
import com.lizhi.method.queue.ArrayDeque;
import com.lizhi.method.queue.ArrayQueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Solution {

    public ArrayList<ArrayList<Integer>> levelOrder(TreeNode root){
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        if (root == null){
            return result;
        }
        ArrayQueue<TreeNode> queue = new ArrayQueue<>(10);
        queue.offer(root);
        int c1 = 1;//第一次元素
        while (!queue.isEmpty()){
            ArrayList<Integer> level = new ArrayList<>();
            int c2 = 0;//每层元素
            for (int i = 0;i < c1;i++){
                TreeNode peek = queue.peek();
                if (peek.left != null){
                    queue.offer(peek.left);
                    c2++;
                }
                if (peek.right != null){
                    queue.offer(peek.right);
                    c2++;
                }
                TreeNode poll = queue.poll();
                //level.add(poll.value);
            }
            c1 = c2;
            result.add(level);
        }
        return result;
    }

    public ArrayList<ArrayList<Integer>> level1Order(TreeNode root){
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        if (root == null){
            return result;
        }
        ArrayQueue<TreeNode> queue = new ArrayQueue<>(10);
        queue.offer(root);
        int c1 = 1;
        boolean odd = false;
        while (!queue.isEmpty()){
            ArrayDeque<Integer> level = new ArrayDeque<>(10);
            ArrayList<Integer> list = new ArrayList<>();
            int c2 = 0;//每层元素
            for (int i = 0;i < c1;i++){
                TreeNode peek = queue.peek();
                if (peek.left != null){
                    queue.offer(peek.left);
                    c2++;
                }
                if (peek.right != null){
                    queue.offer(peek.right);
                    c2++;
                }
                TreeNode poll = queue.poll();
                //level.offerLast(poll.value);
            }
            c1 = c2;
            while (!level.isEmpty()){
                if (odd){
                    list.add(level.pollFirst());
                }else {
                    list.add(level.pollLast());
                }
            }
            odd = !odd;
            result.add(list);
        }
        return result;
    }

    /**
     * 前序遍历
     * @param treeNode
     */
    public void preOrderRecursion(TreeNode treeNode){
        if (treeNode == null){
            return;
        }
        System.out.print(treeNode.value+"\t");
        preOrderRecursion(treeNode.left);
        preOrderRecursion(treeNode.right);
    }

    /**
     * 中序遍历
     * @param treeNode
     */
    public void inOrderRecursion(TreeNode treeNode){
        if (treeNode == null){
            return;
        }
        inOrderRecursion(treeNode.left);
        System.out.print(treeNode.value + "\t");
        inOrderRecursion(treeNode.right);
    }

    /**
     * 后序遍历
     * @param treeNode
     */
    public void postOrderRecursion(TreeNode treeNode){
        if (treeNode == null){
            return;
        }
        postOrderRecursion(treeNode.left);
        postOrderRecursion(treeNode.right);
        System.out.print(treeNode.value + "\t");
    }

    /**
     * 非递归前序遍历,当前，左，右
     */
    public void preOrder(TreeNode treeNode){
        TreeNode current = treeNode;
        LinkedList<Object> list = new LinkedList<>();
        LinkedListStack<TreeNode> stack = new LinkedListStack<>(10);
        while (current != null || !stack.empty()){
            if (current != null){
                stack.push(current);//记录来时路
                System.out.print(current.value + "\t");
                current = current.left;
            }else {
                TreeNode pop = stack.pop();
                current = pop.right;
            }
        }
    }

    /**
     * 非递归中序遍历,左，当前，右
     * @param treeNode
     */
    public void inOrder(TreeNode treeNode){
        TreeNode current = treeNode;
        LinkedListStack<TreeNode> stack = new LinkedListStack<>(10);
        while (current != null || !stack.empty()){
            if (current != null){
                stack.push(current);
                current = current.left;
            }else {
                TreeNode pop = stack.pop();
                System.out.print(pop.value + "\t");
                current = pop.right;
            }
        }
    }

    /**
     * 后序遍历，左，右，当前
     * @param treeNode
     */
    public void postOrder(TreeNode treeNode){
        TreeNode current = treeNode;
        TreeNode previous = treeNode;//最近一次弹栈的元素
        LinkedListStack<TreeNode> stack = new LinkedListStack<>(10);
        while (current != null || !stack.empty()){
            if (current != null){
                stack.push(current);
                current = current.left;
            }else{
                TreeNode peek = stack.peek();
                if (peek.right == null || previous == peek.right) {
                    TreeNode pop = stack.pop();
                    previous = pop;
                    System.out.print(pop.value + "\t");
                } else {
                    current = peek.right;
                }
            }
        }
    }

    /**
     * 中缀表达式 (2-1)*3 转后缀（逆波兰）表达式 21-3*
     * @return
     */
    /*public TreeNode constructExpressionTree(String[] list){
        LinkedList<TreeNode> stack = new LinkedList<>();
        for (String s : list) {
            switch(s) {
                case "+","-","/","*" ->{
                    TreeNode left = stack.pop();
                    TreeNode right = stack.pop();
                    TreeNode parent = new TreeNode(s);
                    parent.left = left;
                    parent.right = right;
                    stack.push(parent);
                }
                default -> {
                    stack.push(new TreeNode(s));
                }
            }
        }
        return stack.peek();
    }*/

    public TreeNode buildTree1(int[] preorder, int[] inorder) {
        if(preorder.length == 1){
            return new TreeNode(preorder[0]);
        }
        if (preorder.length == 0){
            return null;
        }
        TreeNode root = new TreeNode(preorder[0]);
        for(int i = 0; i < inorder.length; i++){
            if(inorder[i] == preorder[0]){
                int[] inLeft = Arrays.copyOfRange(inorder,0,i);
                int[] inRight = Arrays.copyOfRange(inorder,i+1,inorder.length);
                int[] preLeft = Arrays.copyOfRange(preorder,1,i+1);
                int[] preRight = Arrays.copyOfRange(preorder,i+1,inorder.length);
                TreeNode left = buildTree1(preLeft,inLeft);
                TreeNode right = buildTree1(preRight,inRight);
                root.left = left;
                root.right = right;
                break;
            }
        }
        return root;
    }

    public TreeNode buildTree2(int[] inorder, int[] postorder) {
        if(inorder.length == 0){
            return null;
        }
        TreeNode root = new TreeNode(postorder[postorder.length-1]);
        for(int i = 0; i < inorder.length; i++){
            if(inorder[i] == postorder[postorder.length-1]){
                int[] inLeft = Arrays.copyOfRange(inorder,0,i);
                int[] inRight = Arrays.copyOfRange(inorder,i+1,inorder.length);
                int[] postLeft = Arrays.copyOfRange(postorder,0,i);
                int[] postRight = Arrays.copyOfRange(postorder,i,inorder.length -1);
                TreeNode left = buildTree2(inLeft,postLeft);
                TreeNode right = buildTree2(inRight,postRight);
                root.left = left;
                root.right = right;
                break;
            }
        }
        return root;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        TreeNode treeNode = solution.buildTree2(new int[]{9,3,15,20,7}, new int[]{9,15,7,20,3});

    }

}
