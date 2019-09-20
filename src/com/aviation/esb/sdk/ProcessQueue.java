package com.aviation.esb.sdk;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.tsystems.aviation.esb.resultModel.EsbMessage;
/*
 * 消息处理队列
 */
public class ProcessQueue {
	
	 BlockingQueue<EsbMessage> desk = new LinkedBlockingQueue<EsbMessage>(8192);
	 
	 public void washDish(EsbMessage msg) throws InterruptedException {
         desk.put(msg);
     }
	 
	 public EsbMessage useDish() throws InterruptedException {
         return desk.take();
     }
	 
	 public int Count(){
		 return desk.size() ;
	 }
	 
	 public void Clear(){
		 if(desk.size()>5000)
			 desk.clear();
	 }
}
