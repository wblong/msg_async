package com.aviation.esb.sdk;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsReceiver implements MessageListener {
	private String USER = ActiveMQConnection.DEFAULT_USER;
	private String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
	private String URL = ActiveMQConnection.DEFAULT_BROKER_URL;
	private String SUBJECT = "ActiveMQ.Demo";

	private Destination dest = null;
	private Connection conn = null;
	private Session session = null;
	private MessageConsumer consumer = null;

	private boolean stop = false;
	private static ProcessQueue  flightqueue; //航班队列
	// 初始化
	private void initialize() throws JMSException, Exception {
	  // 连接工厂是用户创建连接的对象.
	  ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USER, PASSWORD, URL);
	  // 连接工厂创建一个jms connection
	  conn = connectionFactory.createConnection();
	  // 是生产和消费的一个单线程上下文。会话用于创建消息的生产者，消费者和消息。会话提供了一个事务性的上下文。
	  session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE); // 不支持事务
	  // 目的地是客户用来指定他生产消息的目标还有他消费消息的来源的对象.
	  dest = session.createQueue(SUBJECT);
	  // dest = session.createTopic(SUBJECT);
	  // 会话创建消息的生产者将消息发送到目的地
	  consumer = session.createConsumer(dest);
	  flightqueue=new ProcessQueue();
	}

	/**
	  * 消费消息
	  * 
	  * @throws JMSException
	  * @throws Exception
	  */
	public void receiveMessage() throws JMSException, Exception {
	  initialize();
	  conn.start();
	  consumer.setMessageListener(this);
	  // 等待接收消息
	  while (!stop) {
	   Thread.sleep(5000);
	  }

	}

	@SuppressWarnings("rawtypes")
	public void onMessage(Message msg) {
	   if (msg instanceof TextMessage){
	    TextMessage message = (TextMessage) msg;
	    System.out.println("------Received TextMessage------");
	    try {
	    	//flightqueue.washDish(esbMessage);
			System.out.println(message.getText());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   } 
	}

	// 关闭连接
	public void close() throws JMSException {
	  System.out.println("Consumer:->Closing connection");
	  if (consumer != null)
	   consumer.close();
	  if (session != null)
	   session.close();
	  if (conn != null)
	   conn.close();
	}
	
	public static void main(String[] args) throws JMSException, Exception {
		
		JmsReceiver rece=new JmsReceiver();
		rece.receiveMessage();
	}
}
