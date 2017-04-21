package com.kafka.kafkaSpout4;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Map;

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
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
       
   }

   @Override
   public void execute(Tuple input) {

      try {  
          String str = input.getString(0);
          collector.ack(input);
          s = str.split(",");
          writer.writeNext(s);
      } catch (Exception e) {  
          collector.fail(input);  
      } 
   }

   @Override
   public void cleanup() {
	   try {
		writer.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
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