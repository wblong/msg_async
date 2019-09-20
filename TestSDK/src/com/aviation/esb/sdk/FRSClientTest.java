package com.aviation.esb.sdk;

import org.apache.commons.lang.StringUtils;

import com.tsystems.aviation.esb.client.commons.ImfServiceType;
import com.tsystems.aviation.esb.client.heartbeat.ImfHeartbeatManager;
import com.tsystems.aviation.esb.client.message.ImfMessageListener;
import com.tsystems.aviation.esb.client.subsystem.ImfRsClient;


/**
 * 
 * @ClassName: FRSClientTest 
 * @Description: FRS服务Demo
 * @author rmeng 
 * @date 2018年11月13日 下午2:35:50 
 *
 */
public class FRSClientTest {

	
	public static void main(String[] args) {
		try {
			ImfMessageListener frsImfMessageListener = new ImfMessageListener() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 6668348885136932163L;

				@Override
				public void handleMessage(String message) {
					// 此处获取消费放的请求
					
					if(ImfHeartbeatManager.isHbMessage(message)){
						//此处得到心跳消息
					}else{
						String start = StringUtils.substringBetween(message, "<RequestStartDate>", "</RequestStartDate");
						String end = StringUtils.substringBetween(message, "<RequestEndDate>", "</RequestEndDate");
						//获取消费方的请求范围 根据实际情况来调用  构造xml消息列表调用frsClient.response(messages);
						
					}
					
				}
			};
		
			ImfRsClient frsClient = ImfRsClient.getImfRsClient(ImfServiceType.FRS, frsImfMessageListener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

