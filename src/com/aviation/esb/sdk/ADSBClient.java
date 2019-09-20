package com.aviation.esb.sdk;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tsystems.aviation.esb.client.EsbClient;
import com.tsystems.aviation.esb.esbInterface.IBussReceiver;
import com.tsystems.aviation.esb.resultModel.EsbMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 
 * @ClassName: SDKClientTest
 * @Description: SDK通用服务接口Demo
 * @author rmeng
 * @date 2018年11月13日 下午2:44:18
 *
 */
public class ADSBClient implements IBussReceiver {

	private static Log logger = LogFactory.getLog(ADSBClient.class);
	
	private static EsbClient client;
	/*
	 * activemq
	 */
	private static String mqUrl = "tcp://127.0.0.1:61616?jms.alwaysSyncSend=false&jms.useAsyncSend=true";
	private static String mqAircraftName = "GIS_AIRCRAFT_LBS";
	private static String mqVehicleName = "GIS_VEHICLE_LBS";

	private static ActiveMQConnectionFactory connectionFactory;
	private static Connection connection_flight;
	private static Connection connection_vehicle;
	private static MessageProducer producer_flight;
	private static MessageProducer producer_vehicle;
	private static Session session_flight;
	private static Session session_vehicle;
	
	private static ProcessQueue  flightqueue; //航班队列
	private static ProcessQueue  vehiclequeue;//车辆队列
	/*
	 * 主题 GIS_VEHICLE_LBS_EXTEND:车辆 服务编码
	 */
	public static void main(String[] args) {
		init();
		ExecutorService service = Executors.newCachedThreadPool();
		for(int i=0;i<5;i++){
			
			service.submit(new Consumer(flightqueue,session_flight,producer_flight,
					connection_flight,mqAircraftName,logger));
			
		    service.submit(new Consumer(vehiclequeue,session_vehicle,producer_vehicle,
		    		connection_vehicle,mqVehicleName,logger));
		}
	}

	public static void init() {
		
		flightqueue=new ProcessQueue();
		vehiclequeue=new ProcessQueue();
		
		client = EsbClient.getEsbClient(new ADSBClient());
		
		connectionFactory = new ActiveMQConnectionFactory(mqUrl);
		
		try {
			// mq
			connection_flight = connectionFactory.createConnection();
			connection_flight.start();
			connection_vehicle = connectionFactory.createConnection();
			connection_vehicle.start();
			session_flight = connection_flight.createSession(true, Session.AUTO_ACKNOWLEDGE);
			session_vehicle = connection_vehicle.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session_flight.createTopic(mqAircraftName);
			producer_flight = session_flight.createProducer(destination);
			producer_flight.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			destination = session_vehicle.createTopic(mqVehicleName);
			producer_vehicle = session_vehicle.createProducer(destination);
			producer_vehicle.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			// 鉴权、主题连接
			client.login();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.toString());
		}
	}

	@Override
	public void receiveData(EsbMessage esbMessage) {
		//pool.execute(
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// TODO Auto-generated method stub
					String topic = esbMessage.getHeadersMap().get("Svc_TopicName");
					
					if (topic.equals("AirportToptic/IHPS_HKQWZBCSJFW")) {
						flightqueue.washDish(esbMessage);
					}else if (topic.equals("AirportToptic/IHPS_CLWZBCSJFW")) {
						vehiclequeue.washDish(esbMessage);
					}
				}catch(InterruptedException e){
					logger.error(e.toString());
				}
			}
		}).start();
	}
}
