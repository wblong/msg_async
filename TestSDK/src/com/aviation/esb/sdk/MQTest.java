package com.aviation.esb.sdk;


import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class MQTest  {
	
    private static final String url = "tcp://127.0.0.1:61616";
    private static final String topicName = "GIS_AIRCRAFT_LBS";

    public static void main(String[] args) throws JMSException {
    	
        //1.创建ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        // 添加信任自定义消息类所在包
       // connectionFactory.setTrustedPackages(new ArrayList<>(Arrays.asList(new String[]{AppProducer.class.getPackage().getName()})));
        //2.创建Connection
        Connection connection = connectionFactory.createConnection();
        //3.启动连接
        connection.start();
        //4.创建会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.创建一个目标
        Destination destination = session.createTopic(topicName);
        //6.创建一个生产者
        MessageProducer producer = session.createProducer(destination);
        for (int i = 0; i < 10; i++) {
            //7.创建消息
            TextMessage textMessage = session.createTextMessage("test" + i);
            //8.发布消息
            producer.send(textMessage);

            System.out.println("发送消息"+textMessage.getText());
        }

        //9.关闭连接
        connection.close();

    }
}