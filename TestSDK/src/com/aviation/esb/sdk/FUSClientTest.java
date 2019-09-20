package com.aviation.esb.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tsystems.aviation.esb.client.commons.ImfServiceType;
import com.tsystems.aviation.esb.client.subsystem.ImfUsClient;

/**
 * 
 * @ClassName: FUSClientTest 
 * @Description: 航班数据更新服务Demo
 * @author rmeng 
 * @date 2018年11月13日 下午2:34:36 
 *
 */
public  class FUSClientTest {
	
	private static Logger logger = LoggerFactory.getLogger(FUSClientTest.class);
	
	public static void main(String[] args){
		
		//message为需要更新的消息
		//String message = "<IMFRoot xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'> 	<SysInfo> 		<MessageSequenceID>2</MessageSequenceID> 		<MessageType>FlightData</MessageType> 		<ServiceType>FUS</ServiceType> 		<OperationMode>MOD</OperationMode> 		<SendDateTime>2018-11-12T15:06:39.041</SendDateTime> 		<CreateDateTime>2018-11-12T09:52:27.715</CreateDateTime> 		<OriginalDateTime>2018-11-12T09:24:35</OriginalDateTime> 		<Receiver>IMF</Receiver> 		<Sender>BHS</Sender> 		<Owner>BHS</Owner> 		<Station>PEK</Station> 	</SysInfo> 	<Data> 		<PrimaryKey> 			<FlightKey> 				<FlightScheduledDate>2018-11-12</FlightScheduledDate> 				<FlightIdentity>ZH9157</FlightIdentity> 				<FlightDirection>D</FlightDirection> 			</FlightKey> 		</PrimaryKey> 		<FlightData> 			<General> 				<FlightScheduledDateTime>2018-11-12T23:25:00</FlightScheduledDateTime> 			</General> 			<Airport> 				<BaggageMakeup> 					<BaggageBeltID>D21</BaggageBeltID> 					<ScheduledMakeupStartDateTime>2018-11-12T18:23:23</ScheduledMakeupStartDateTime> 					<ScheduledMakeupEndDateTime>2018-11-12T18:35:11</ScheduledMakeupEndDateTime> 				</BaggageMakeup> 				<BaggageMakeup> 					<BaggageBeltID>D22</BaggageBeltID> 					<ScheduledMakeupStartDateTime>2018-11-12T17:23:23</ScheduledMakeupStartDateTime> 					<ScheduledMakeupEndDateTime>2018-11-12T17:35:11</ScheduledMakeupEndDateTime> 				</BaggageMakeup> 			</Airport> 		</FlightData> 	</Data> </IMFRoot>"; 		
		String message = "<IMFRoot xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'> 	<SysInfo> 		<MessageSequenceID>2</MessageSequenceID> 		<MessageType>FlightData</MessageType> 		<ServiceType>FUS</ServiceType> 		<OperationMode>MOD</OperationMode> 		<SendDateTime>2018-11-12T20:16:39.041</SendDateTime> 		<CreateDateTime>2018-11-12T20:16:27.715</CreateDateTime> 		<OriginalDateTime>2018-11-12T20:14:35</OriginalDateTime> 		<Receiver>IMF</Receiver> 		<Sender>DCS</Sender> 		<Owner>DCS</Owner> 		<Station>PEK</Station> 	</SysInfo> 	<Data> 		<PrimaryKey> 			<FlightKey> 				<FlightScheduledDate>2018-11-12</FlightScheduledDate> 				<FlightIdentity>ZH9157</FlightIdentity> 				<FlightDirection>D</FlightDirection> 			</FlightKey> 		</PrimaryKey> 		<FlightData> 			<General> 				<FlightScheduledDateTime>2018-11-12T23:25:00</FlightScheduledDateTime> 			</General> 			<Airport> 				<Gate> 					<GateID>01</GateID> 					<ActualGateStartDateTime>2018-11-12T22:25:00</ActualGateStartDateTime> 				</Gate> 			</Airport> 		</FlightData> 	</Data> </IMFRoot>"; 
		fusClient(message);
	}
	
	public static void fusClient(String message){
		try {
			ImfUsClient imfUsClient = ImfUsClient.getImfUsClient(ImfServiceType.FUS);
			
			//更新航班数据操作
			imfUsClient.update(message);
			
			//获取心跳状态
			//imfUsClient.connectionStatus();
			
			
		} catch (Exception e) {
			logger.error("##########update flight data failed!!!##########",e);
			e.printStackTrace();
		}
		
	}
}
