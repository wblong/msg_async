package com.aviation.esb.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tsystems.aviation.esb.client.commons.ImfServiceType;
import com.tsystems.aviation.esb.client.message.ImfMessageListener;
import com.tsystems.aviation.esb.client.subsystem.ImfSsAutoClient;

/**
 * 
 * @ClassName: FSSClientTest 
 * @Description: 航班数据服务Demo
 * @author rmeng 
 * @date 2018年11月13日 下午2:31:11 
 *
 */
public class FSSClientTest{
	
	private static Logger logger = LoggerFactory.getLogger(FSSClientTest.class);
	
	public static void main(String[] args){
		fssClient();
	}
	
	public static void fssClient(){
		
		
		try {
			
			ImfMessageListener listener = new ImfMessageListener() {


			/**
			 * 
			 */
			private static final long serialVersionUID = 3902478670225876225L;

			@Override
			public void handleMessage(String message) {
				//此处放获取的航班数据，后续对该消息的解析、入库操作也从这里开始执行
				logger.info("the flight message is>>>>>\n"+message);
				
			}};
			
			ImfSsAutoClient ssAutoClient = ImfSsAutoClient.getImfSsAutoClient(ImfServiceType.FSS, listener);
			
			
			//取消订阅
			//ssAutoClient.unSubscribe();
			
			//订阅
			ssAutoClient.subscribe();
			
			//获取心跳状态
			//ssAutoClient.connectionStatus();
			
		} catch (Exception e) {
			logger.error("##########get flight data failed!!!##########",e);
			e.printStackTrace();
		}
	}
	
}
