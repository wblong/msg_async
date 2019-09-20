package com.aviation.esb.sdk;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsystems.aviation.esb.client.EsbClient;
import com.tsystems.aviation.esb.esbInterface.IBussReceiver;
import com.tsystems.aviation.esb.resultModel.EsbMessage;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;



/**
 * 
 * @ClassName: SDKClientTest 
 * @Description: SDK通用服务接口Demo
 * @author rmeng 
 * @date 2018年11月13日 下午2:44:18 
 *
 */
public class ADSBClient implements IBussReceiver{
	
	private static Log logger = LogFactory.getLog(SDKClientTest.class);
	
	private static EsbClient client;
	
	// send message to activemq
	private static String mqUrl = "tcp://127.0.0.1:61616";
    private static String mqTopicName = "GIS_AIRCRAFT_LBS";
    private static String mqTopic="GIS_VEHICLE_LBS";

    private static ActiveMQConnectionFactory connectionFactory;
    private static Connection connection;
    private static Connection connection1;
    private static MessageProducer producer;
    private static MessageProducer producer1;
	private static Session session;
	private static Session session1;
	
	/*
	 * 主题
	 * GIS_VEHICLE_LBS_EXTEND:车辆
	 * 服务编码
	 */
//	private static final String topic ="HKQWZ0306";
	
	public static void main(String[] args){
		
		init();
	}
	
	public static void init(){
		
		client = new EsbClient(new ADSBClient());
		connectionFactory=new ActiveMQConnectionFactory(mqUrl);
		
		try {
			//mq
			connection = connectionFactory.createConnection();
			connection.start();
			connection1 = connectionFactory.createConnection();
			connection1.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			session1 = connection1.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createTopic(mqTopicName);
			producer = session.createProducer(destination);
			destination = session1.createTopic(mqTopic);
			producer1 = session1.createProducer(destination);
			//鉴权、主题连接
			client.login();
			//client.connTopic(topic, EsbDataEnum.SEQDATA);
			
//			ImfMessageListener listener = new ImfMessageListener() {
//
//
//				@Override
//				public void handleMessage(String message) {
//					logger.info("send message>>>>>\n"+message);
//					try {
//						client.getSenders().get(topic).sendMessage(message);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//
//			};
//			
//			ImfSsAutoClient ssAutoClient = ImfSsAutoClient.getImfSsAutoClient(ImfServiceType.FSS1, listener);
//			//取消订阅
////			ssAutoClient.unSubscribe();
//			
//			//订阅
//			ssAutoClient.subscribe();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void receiveData(EsbMessage esbMessage) throws Exception {
		
	   logger.info("reciever message>>>>\n" + esbMessage.getMessageBody());
	   
	   String topic=esbMessage.getHeadersMap().get("Svc_TopicName");
	   TextMessage textMessage=null;
	   if(topic.equals("AirportToptic/HKQWZBCSJFW0688") ||  topic.equals("AirportToptic/HKQWZ0306")){
	   //7.创建消息
		   textMessage= session.createTextMessage(esbMessage.getMessageBody());
       //8.发布消息  
		   producer.send(textMessage);
		   }
	   else if(topic.equals("AirportToptic/CLWZBCSJFW0687")){
		   //    Svc_TopicName: AirportToptic/CLWZBCSJFW0687
		   textMessage= session1.createTextMessage(esbMessage.getMessageBody());
	       //8.发布消息  
		   producer1.send(textMessage);
	   }
		   
	   
	}
}
