package com.aviation.esb.sdk;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsystems.aviation.esb.client.EsbClient;
import com.tsystems.aviation.esb.client.commons.ImfServiceType;
import com.tsystems.aviation.esb.client.message.ImfMessageListener;
import com.tsystems.aviation.esb.client.subsystem.ImfSsAutoClient;
import com.tsystems.aviation.esb.esbEnum.EsbDataEnum;
import com.tsystems.aviation.esb.esbInterface.IBussReceiver;
import com.tsystems.aviation.esb.resultModel.EsbMessage;

/**
 * 
 * @ClassName: SDKClientTest 
 * @Description: SDK通用服务接口Demo
 * @author rmeng 
 * @date 2018年11月13日 下午2:44:18 
 *
 */
public class SDKClientTest implements IBussReceiver{
	
	private static Log logger = LogFactory.getLog(SDKClientTest.class);
	
	private static EsbClient client;
	
	/*
	 * 主题
	 */
	private static final String topic ="AirportToptic/IN.FSS.TOPIC";
	
	public static void main(String[] args){
		init();
	}
	
	@SuppressWarnings("serial")
	public static void init(){
		
		client = new EsbClient(new SDKClientTest());
		
		try {
			//鉴权、主题连接
			client.login();
			client.connTopic(topic, EsbDataEnum.SEQDATA);
			
			ImfMessageListener listener = new ImfMessageListener() {


				@Override
				public void handleMessage(String message) {
					logger.info("send message>>>>>\n"+message);
					try {
						client.getSenders().get(topic).sendMessage(message);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			};
			
			ImfSsAutoClient ssAutoClient = ImfSsAutoClient.getImfSsAutoClient(ImfServiceType.FSS1, listener);
			//取消订阅
//			ssAutoClient.unSubscribe();
			
			//订阅
			ssAutoClient.subscribe();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void receiveData(EsbMessage esbMessage) throws Exception {
	   logger.info ("消息属性Svc_AppID"+esbMessage.getHeadersMap().get("Svc_AppID"));
       logger.info ("消息属性Svc_AppName"+esbMessage.getHeadersMap().get("Svc_AppName"));
       logger.info ("消息属性Svc_MsgSeq"+esbMessage.getHeadersMap().get("Svc_MsgSeq"));
       logger.info ("消息属性Svc_TimeStamp"+esbMessage.getHeadersMap().get("Svc_TimeStamp"));
       logger.info ("消息属性Svc_ServiceCode"+esbMessage.getHeadersMap().get("Svc_ServiceCode"));
       logger.info ("消息属性Svc_ServiceType"+esbMessage.getHeadersMap().get("Svc_ServiceType"));
       logger.info ("消息属性Svc_MsgType"+esbMessage.getHeadersMap().get("Svc_MsgType"));
       logger.info ("消息属性Svc_TopicName"+esbMessage.getHeadersMap().get("Svc_TopicName"));
       logger.info ("消息属性Svc_QName"+esbMessage.getHeadersMap().get("Svc_QName"));
	   logger.info("reciever message>>>>\n" + esbMessage.getMessageBody());
	   
		
	}
}
