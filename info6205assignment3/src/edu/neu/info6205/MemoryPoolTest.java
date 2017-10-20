package edu.neu.info6205;

import java.util.ArrayList;

public class MemoryPoolTest {
	
	private static MemoryPool myMemoryPool = null;
	private static ArrayList<BlockBST> usedBlocks = new ArrayList<BlockBST>();
	private final static int SLEEPTIME = 0; //milliseconds, 0 for no sleep
	private final static int N = 5; //the size of root will be 2^N
	private final static int TESTTIME = 1;
	private final static double REQUEST_VS_RETURN_RATE = 0.5; //from 0 to 1, the larger the less request time
	private static int failedRequestNumber = 0;
	
	public static void main(String[] args) {
		run();
	}
	
	private static void run() {
		int requestCount = 0;
		int returnCount = 0;
		long startTime = System.currentTimeMillis();
		myMemoryPool = new MemoryPool();
		long endTime   = System.currentTimeMillis();
		long createTime = endTime - startTime;
		myMemoryPool.print();
		//System.out.println("Create Memory Pool Time: "+createTime);
		
		System.out.println(">>>>>>>>START<<<<<<<<");
		startTime = System.currentTimeMillis();
		for (int i = 0; i < TESTTIME; i++) {
			if (Math.random() > REQUEST_VS_RETURN_RATE) {
				requestBlock();
				requestCount++;
			} else {
				returnBlock();
				returnCount++;
			}
		}
		endTime   = System.currentTimeMillis();
		myMemoryPool.print();
		System.out.println("Total Failed Request Number: "+failedRequestNumber);
		//System.out.println("Fragment: "+myMemoryPool.getFragment());
		long totalRunTime = endTime - startTime;
		System.out.println("Create Memory Pool Time: "+createTime);
		System.out.println("Total Runtime: "+totalRunTime);
		System.out.println("Total Request Count: "+requestCount);
		System.out.println("Total Return Count: "+returnCount);
	}

	
	private static void requestBlock() {
		int index = (int) (Math.random() * N);
		System.out.print("We want a " + (int) Math.pow(2, index) + " size block. ");
		try {
			BlockBST b = myMemoryPool.getBlock(index);
			usedBlocks.add(b);
			System.out.println("We get a block:" + b + ". Remaining Memory Pool:");
			myMemoryPool.print();
			System.out.println("==============================");
		} catch (NoBlockAvailableException e) {
			failedRequestNumber++;
			System.out.println("We failed to get a block. After defragment, Memory Pool:");
			myMemoryPool.print();
			System.out.println("==============================");
		}
		sleep(SLEEPTIME);
	}
	
	private static void returnBlock() {
		if (usedBlocks.size() == 0) {
			return;
		}
		int index = (int) (usedBlocks.size()*Math.random());
		BlockBST b = usedBlocks.remove(index);
		myMemoryPool.returnBlock(b);
		sleep(SLEEPTIME);
		System.out.println("We returned a block:" + b + ". Remaining Memory Pool:");
		myMemoryPool.print();
		System.out.println("==============================");
	}
	
	
	private static void sleep(int i) {
		if (i>0) {
			try {
				Thread.sleep(i);
			} catch (InterruptedException e) {
				return;
			}
		} else {
			return;
		}
	}
}
