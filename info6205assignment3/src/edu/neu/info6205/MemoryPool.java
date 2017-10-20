package edu.neu.info6205;

import java.util.LinkedList;
import java.util.Queue;

public class MemoryPool {

	private final static int N = 5; //the size of root will be 2^N 或者說是幾個階層
	private BlockBST memoryPoolRoot = null;
	
	public MemoryPool() {
		memoryPoolRoot = createMemoryPool(N,0,memoryPoolRoot,null);//(N次方，起始位置，子節點的產生，來源的父節點）
	}
	
	private BlockBST createMemoryPool(int index,int start,BlockBST root,BlockBST parent) {
		if (index < 0) {
			return null;
		}
		if (parent != null) {
			root = new BlockBST((int)Math.pow(2, index),start,parent);
		} else {
			root = new BlockBST((int)Math.pow(2, index),start,null);
		}
		//左右子節點，所以事實上是要少一次方，起始位置，以左邊為起點，故右邊需要加次方數
		root.left = createMemoryPool(index-1,start,root.left,root);
		root.right = createMemoryPool(index-1,start+(int)Math.pow(2, index-1),root.right,root);
		return root; 
	}
	
	public BlockBST getBlock(int index) throws NoBlockAvailableException{
		BlockBST result = getBlock(index,memoryPoolRoot);
		if (result == null) {
			defragment();
			throw new NoBlockAvailableException();
		}
		markBlock(result,true);
		markParent(result,true); //not sure if needed
		return result;
	}
	
	private BlockBST getBlock(int index,BlockBST root){
		//要求沒有超過我們設定的大小，而且有可以用的
		if ((!isUsed(root)) && (root.size == (int)Math.pow(2, index))) {
			return root;
		} else {
			//本身要求的memory子節點有存在的話   這塊要問
			if (root.left != null) {
				BlockBST result = getBlock(index,root.left);
				if (result != null) {
					return result;
				} else {
					if (root.right != null) {
						result = getBlock(index,root.right);
						if (result != null) {
							return result;
						}
					}
				}
			}
		}
		return null;
	}
	//以下設定是為了確認父節點或子節點是否被使用，如果本身沒被找到，就找左右邊的子節點，因為只有左右邊存在才可使用
	private boolean isUsed(BlockBST root) {
		if ((root.left == null) && (root.right == null)) {
			return root.isUsed;
		} else {
			return (isUsed(root.left) || isUsed(root.right));
		}
	}
	//確認前一個節點是否存在
	private void markParent(BlockBST root,boolean status) {
		while (root.parent != null) {
			root.parent.isUsed = status;
			root = root.parent;
		}
	}
	//確認自己本身，並傳回左右邊的情況，因為如果其中一邊被使用了，這個節點就不可使用
	private void markBlock(BlockBST root,boolean status) {
		root.isUsed = status;
		if (root.left != null) {
			markBlock(root.left,status);
		}
		if (root.right != null) {
			markBlock(root.right,status);
		}
	}
	//回傳值時，把位置轉成不可使用
	public void returnBlock(BlockBST b) {
		markBlock(b,false);
	}
	//這裡是合併的區域
	//no need to defragment if markParent is commented
	private void defragment() {		
		defragment(memoryPoolRoot);
	}
	
	private void defragment(BlockBST root) {
		if ((root.left == null) && (root.right == null)) {
			return;
		}
		//數入的memory的子節點有孫子節點可用，直接使用為data的左右
		if ((root.left.left != null) && (root.left.right != null) && (root.right.left != null) && (root.right.right != null)){
			defragment(root.left);
			defragment(root.right);
		}
		if (root.isUsed && !isUsed(root.left) && !isUsed(root.right)) {
			root.isUsed = false;
			System.out.println(root+" is defraged!!!");
		}
	}
	
	public void print() {
		Queue<BlockBST> q = new LinkedList<BlockBST>();
		int count = 0;
		int lastCount = 0;
		int index = 0;
		q.offer(memoryPoolRoot);
		while (!q.isEmpty()) {
			BlockBST temp = (BlockBST) q.poll();
			System.out.print(temp);
			if (temp.left != null) {
				q.offer(temp.left);
			}
			if (temp.right != null) {
				q.offer(temp.right);
			}
			count++;
			if ((count-lastCount) == (int)Math.pow(2, index)) {
				System.out.println("");
				lastCount = count;
				index++;
			}
		}
	}
	
}
