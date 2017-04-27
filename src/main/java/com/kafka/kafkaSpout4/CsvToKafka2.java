package com.kafka.kafkaSpout4;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

public class CsvToKafka2  extends Thread{
	private static Producer<Integer, String> producer;
    private final Properties properties = new Properties();
    private String topic; 
    private String filePath;
    private static final Logger logger = LogManager.getLogger(CsvToKafka2.class);
    private int i = 1;
    
    public CsvToKafka2(String topic,String filePath) {  
        super();  
        this.topic = topic;  
        this.filePath = filePath;
    } 
    private Producer<Integer, String> createProducer() {  
        properties.put("metadata.broker.list", "localhost:9093");
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("request.required.acks", "1");
        properties.put("zookeeper.connect", "127.0.0.1:2181");// 
        return new Producer<Integer, String>(new ProducerConfig(properties));  
    } 
    @Override  
    public void run() {  
    	CSVReader reader = null;
    	DataInputStream in = null;
    	
    	try {
    		producer = createProducer();  
			String[] nextLine;
	        KeyedMessage<Integer, String> data = null;
			
			//reader = new CSVReader(new FileReader(args[1])); //f://1.csv
			in = new DataInputStream(new FileInputStream(new File(filePath)));  
		
			reader = new CSVReader(new InputStreamReader(in,"BIG5"));

			if(reader.readNext() != null){
				while ((nextLine = reader.readNext()) != null) { 
					logger.info(i + " : " +nextLine[0]);
			        data = new KeyedMessage<>(topic, nextLine[0]);
			        producer.send(data);
			        Thread.sleep(1 * 1000);
			        data = null;
			        i += 1;
				}
				 producer.close();
			}	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e);
			}
			reader = null;
			in = null;
		}
    }  
	public static void main(String[] args) throws Exception {//test,f://1.csv
		// TODO Auto-generated method stub
		if(args != null && args.length > 1){
			new CsvToKafka2(args[0],args[1]).start();
			
			
		} 
	}

}
