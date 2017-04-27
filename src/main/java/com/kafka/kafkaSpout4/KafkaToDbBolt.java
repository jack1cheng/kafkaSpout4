package com.kafka.kafkaSpout4;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

import au.com.bytecode.opencsv.CSVWriter;

public class KafkaToDbBolt implements IRichBolt{
   private OutputCollector collector;
   private OutputStreamWriter out = null; 
   private CSVWriter writer = null;  
   private String filePath = null;
   private String s[] = null;
   private static final Logger logger = LogManager.getLogger(KafkaToDbBolt.class);
   private FileWriter fw = null;
   private int i = 1;
   
   public KafkaToDbBolt(String args){
	   filePath = args;
   }
   
   @Override
   public void prepare(Map stormConf, TopologyContext context,
   OutputCollector collector) {
	   try {
		   this.collector = collector;
		   if(filePath != null && filePath.length() > 0){
			   out = new OutputStreamWriter(new FileOutputStream(filePath), Charset.forName("BIG5"));
			   writer = new CSVWriter(out, ',');  
			   
		   } 
		   fw = new FileWriter("f:\\2.txt");

	} catch (Exception e) {
		// TODO Auto-generated catch block
		logger.error(e);
	}  
       
   }

   @Override
   public void execute(Tuple input) {

      try {  
          String str = input.getString(0);
          collector.ack(input);
         // s = str.split(",");
          //writer.writeNext(s);
          fw.write(str + "\r\n");
          fw.flush();
          //Thread.sleep(1 * 1000);
          logger.info(i + " : " + str);
          i += 1;
      } catch (Exception e) {  
          collector.fail(input);  
      } 
   }

   @Override
   public void cleanup() {
	   try {
		writer.close();
		fw.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
		logger.error(e);
	}
   }

   @Override
   public void declareOutputFields(OutputFieldsDeclarer declarer) {
   
   }

   @Override
   public Map<String, Object> getComponentConfiguration() {
      return null;
   }
}