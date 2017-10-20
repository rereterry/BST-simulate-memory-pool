package edu.neu.info6205;

public class BlockBST {

	public int size;  //2的次方大小
	public int start; //在這一行的位置大小
	public BlockBST parent;  //父節點
	public BlockBST right;  //子節點 右
	public BlockBST left;	//子節點 左
	public boolean isUsed;
	
	public BlockBST (int size,int start,BlockBST parent) {
		this.size = size;
		this.start = start;
		this.parent = parent;
		this.left = null;
		this.right = null;
		this.isUsed = false;
	}
	
	@Override
	public String toString() {
		if (isUsed) {
			return "|"+"("+this.start+")"+this.size+"+"+"|";
		} else {
			return "|"+"("+this.start+")"+this.size+"-"+"|";
		}
	}
}
