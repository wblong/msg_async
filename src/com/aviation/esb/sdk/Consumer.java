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
	private long 				_start;
	private long				_end;
	private long				_count;
	
    public Consumer(ProcessQueue workDesk,Session session,MessageProducer producer,
    		Connection connection,String topicName ,Log logger) {
    	
        this._workDesk = workDesk;
        this._session=session;
        this._producer=producer;
        this._connection=connection;
        this._topicName=topicName;
        this._logger=logger;
        _start=_end=System.currentTimeMillis( );
        _count=0;
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long diff=_end-_start;
		try {
            for (;;) {
              
              EsbMessage msg=  _workDesk.useDish();
              try{
            	  TextMessage textMessage = _session.createTextMessage(msg.getMessageBody());
            	  _producer.send(textMessage);
            	  _session.commit();
            	  _count++;
            	  _end=System.currentTimeMillis( );
            	  diff=_end-_start;
            	  if(diff>=1000*60){
            		  _logger.info("process data per minute "+ _count +"; " + 
            				  "the current length of queue is " + _workDesk.Count() );
            		  _start=_end;
            		  _count=0;
            	  }
            	  _workDesk.Clear();
            	  
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
