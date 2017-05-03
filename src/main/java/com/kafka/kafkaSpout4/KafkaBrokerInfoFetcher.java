package com.kafka.kafkaSpout4;

import java.util.List;
import java.util.Map;

import org.apache.zookeeper.ZooKeeper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class KafkaBrokerInfoFetcher {
	public static void main(String[] args){
		try{
			ZooKeeper zk = new ZooKeeper("localhost:2181", 10000, null);
			List<String> topics = zk.getChildren("/brokers/topics", false);
			List<String> ids = zk.getChildren("/brokers/ids", false);
			
			for (String topic : topics) {
	            System.out.println("topic " + topic);
	        }
			for (String id : ids) {
				if( zk.getData("/brokers/ids/" + id, false, null) != null){
		            String brokerInfo = new String(zk.getData("/brokers/ids/" + id, false, null));
		            System.out.println(id + ": " + brokerInfo);
				}
	        }
			/*
	        ZooKeeper zk = new ZooKeeper("localhost:2181", 10000, null);
	        List<String> topics = zk.getChildren("/brokers/topics", false);
	        List<String> ids = zk.getChildren("/brokers/ids", false);
	        JSONObject fieldsJson = null;
	        JSONParser parser = new JSONParser();
	        
	        for (String id : ids) {
	            String brokerInfo = new String(zk.getData("/brokers/ids/" + id, false, null));
	            fieldsJson = (JSONObject) parser.parse(brokerInfo);
	            System.out.println(id + ": " + brokerInfo);
	            System.out.println(fieldsJson.get("host"));
	        }
	        
	        for (String topic : topics) {
	            System.out.println("topic " + topic);
	        }
			 */
		}catch(Exception e){
			System.out.println(e);
		}
    }
}
