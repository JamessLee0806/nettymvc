package com.baocloud.mul;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class MultiThread {
	public static void main(String[] args) {
		ThreadMXBean bean=ManagementFactory.getThreadMXBean();
		ThreadInfo[] threads=bean.dumpAllThreads(false, false);
		for(ThreadInfo info:threads){
			System.out.println("["+info.getThreadId()+"]  "+info.getThreadName()+"  "+info.getThreadState());
		}
		
	}
}
