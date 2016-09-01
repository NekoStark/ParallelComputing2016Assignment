package it.unifi.ing.pc.kafka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import it.unifi.ing.pc.searcher.Result;

public class Searcher {

	public static void main(String[] args) {
		
		// args[0]: key search
		Set<Result> images = new HashSet<>();
		
//		images.addAll( new GoogleImageSearcher().search(args[0], 1) );
//		images.addAll( new BingImageSearcher().search(args[0], 1) );
//		images.addAll( new FlickrSearcher().search(args[0], 1) );
		
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("retries", 1);
		props.put("batch.size", 16384);
		props.put("linger.ms", 0);
		props.put("block.on.buffer.full", true);
//		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
			
//		ZkClient zkClient = new ZkClient("localhost:2181", 10000, 10000, ZKStringSerializer$.MODULE$);
//		AdminUtils.createTopic(zkClient, "my-topic", 10, 1, new Properties());
		
		try(KafkaProducer<String, String> producer = new KafkaProducer<>(props)){
//			for(Result r : images) {
//				String formatted = format(args[0], r);
//				producer.send(new ProducerRecord<String,String>("test2", formatted));
//			}
			for(String s : getLines()) {
				producer.send(new ProducerRecord<String,String>("test2", s));
			}
		}
		
	}
	
	private static String format(String term, Result r) {
		return r.getImage()+"\t"+term+"\t"+r.getService()+"\t"+r.getTimeStamp();
	}
	
	private static List<String> getLines(){
		BufferedReader br = null;
		List<String> lines = new ArrayList<String>();
		
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader("/Users/stark/Desktop/ciao/image.out"));
			while ((sCurrentLine = br.readLine()) != null) {
				lines.add(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return lines;
	}
}
	