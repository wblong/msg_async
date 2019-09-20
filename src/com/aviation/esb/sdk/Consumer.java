package com.aviation.esb.sdk;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;

import com.tsystems.aviation.esb.resultModel.EsbMessage;

public class Consumer implements Runnable{
	
	private ProcessQueue 		_workDesk;
	private Session  			_session;
	private MessageProducer 	_producer;
	private Connection 			_connection;
	private String 				_topicName;
	private Log  				_logger;
	
    public Consumer(ProcessQueue workDesk,Session session,MessageProducer producer,
    		Connection connection,String topicName ,Log logger) {
    	
        this._workDesk = workDesk;
        this._session=session;
        this._producer=producer;
        this._connection=connection;
        this._topicName=topicName;
        this._logger=logger;
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
            for (;;) {
              EsbMessage msg=  _workDesk.useDish();
              try{
            	  TextMessage textMessage = _session.createTextMessage(msg.getMessageBody());
            	  _producer.send(textMessage);
            	  _session.commit();
            	  if(_workDesk.Count()>5000)
            		  _workDesk.Clear();
            	  _logger.info("the current length of workqueue is "+ _workDesk.Count());
              }catch(javax.jms.IllegalStateException ex){
            	  
            	  _session = _connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            	  Destination destination = _session.createTopic(_topicName);
            	  _producer = _session.createProducer(destination);
              }
			  
              Thread.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
