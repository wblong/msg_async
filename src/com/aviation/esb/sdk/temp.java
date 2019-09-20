// TODO Auto-generated method stub
String topic = esbMessage.getHeadersMap().get("Svc_TopicName");
TextMessage textMessage = null;
//logger.info("reciever message>>>>\n" + esbMessage.getMessageBody());
try {
	if (topic.equals("AirportToptic/IHPS_HKQWZBCSJFW")) {
		try {
			//logger.info("reciever message AirportToptic/IHPS_HKQWZBCSJFW 航空器 ...");
			textMessage = session.createTextMessage(esbMessage.getMessageBody());
			producer.send(textMessage);
		} catch (javax.jms.IllegalStateException e) {
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createTopic(mqAircraftName);
			producer = session.createProducer(destination);
			session.commit();
		}

	} else if (topic.equals("AirportToptic/IHPS_CLWZBCSJFW")) {
		try {
			logger.info("reciever message AirportToptic/IHPS_CLWZBCSJFW 车辆 ...");
			textMessage = session1.createTextMessage(esbMessage.getMessageBody());
			producer1.send(textMessage);
		} catch (javax.jms.IllegalStateException e) {
			session1 = connection1.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session1.createTopic(mqVehicleName);
			producer1 = session1.createProducer(destination);
			session1.commit();
		}
	}
} catch (JMSException e) {
	logger.error(e.toString());
}