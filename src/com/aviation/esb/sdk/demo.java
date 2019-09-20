package com.aviation.esb.sdk;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.ibm.disthub2.impl.formats.OldEnvelop.payload.normal.body.jms.JMSbody.MapMessage;

public class demo {
	
	private String USER = ActiveMQConnection.DEFAULT_USER;
	private String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
	private String URL = "tcp://127.0.0.1:61616?jms.alwaysSyncSend=false&jms.useAsyncSend=true";
	private String SUBJECT = "ActiveMQ.Demo";

	private Destination destination = null;
	private Connection conn = null;
	private Session session = null;
	private MessageProducer producer = null;

	// 初始化
	private void initialize() throws JMSException, Exception {
		// 连接工厂
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USER, PASSWORD, URL);
		conn = connectionFactory.createConnection();
		// 事务性会话，自动确认消息
		session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// 消息的目的地（Queue/Topic）
		destination = session.createQueue(SUBJECT);
		// destination = session.createTopic(SUBJECT);
		// 消息的提供者（生产者）
		producer = session.createProducer(destination);
		// 不持久化消息
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		conn.start();
	}

	public void sendMessage(String msgType) throws JMSException, Exception {
		
		// 连接到JMS提供者（服务器）
		
		// 发送文本消息
		if ("text".equals(msgType)) {
			String textMsg = "ActiveMQ Text Message!";
			TextMessage msg = session.createTextMessage();
			// TextMessage msg = session.createTextMessage(textMsg);
			msg.setText(textMsg);
			producer.send(msg);
		}
	}

	// 关闭连接
	public void close() throws JMSException {
	if (producer != null)
	producer.close();
	if (session != null)
	session.close();
	if (conn != null)
	conn.close();
	}
	public static void main(String[] args) throws JMSException, Exception {
		// TODO Auto-generated method stub
		demo d=new demo();
		d.initialize();
		for(;;){
			d.sendMessage("text");
			Thread.sleep(1);
		}
		}
		

}
