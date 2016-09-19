package it.unifi.ing.pc.image.publisher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;


public class ImagePublisher {

	private static final String KAFKA_TOPIC = "test2";
	private static final String[] SERVICES = {"GOOGLE", "BING", "FLICKR"};

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("retries", 1);
		props.put("batch.size", 16384);
		props.put("linger.ms", 0);
		props.put("block.on.buffer.full", true);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
			
		if(args.length == 0) {
			fromFile(props);
		} else {
			if("demo".equals( args[0] )){
				demoMode(props, Arrays.copyOfRange(args, 1, args.length));
			}
			if("online".equals( args[0] )) {
				
			}
		}
		
	}
	
	private static void fromFile(Properties props) {
		System.out.println("reading from file");
		try(KafkaProducer<String, String> producer = new KafkaProducer<>(props)){
			for(String s : getLines()) {
				producer.send(new ProducerRecord<String,String>(KAFKA_TOPIC, s));
			}
		}
	}
	
	private static void demoMode(Properties props, String[] tags) {
		System.out.println("demo mode");
		try(KafkaProducer<String, String> producer = new KafkaProducer<>(props)){
			while(true) {
				for(int i=0; i<10; i++) {
					producer.send(
						new ProducerRecord<String,String>(KAFKA_TOPIC, 
								buildRandomMessage(random(tags), random(SERVICES) )));
				}
				
				try {
					System.out.println("published 1000 messages");
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static String buildRandomMessage(String tag, String service) {
		StringBuffer sb = new StringBuffer();
		return sb.append("http://")
			.append(UUID.randomUUID().toString())
			.append(".com")
			.append("\t")
			.append(tag)
			.append("\t")
			.append(service)
			.append("\t")
			.append(LocalDateTime.now())
			.toString();
	}
	
//	private static String buildRandomMessage(String tag, String service) {
//		StringBuffer sb = new StringBuffer();
//		return sb.append("{\"url\":\"http://")
//			.append(UUID.randomUUID().toString())
//			.append(".com\"")
//			.append(",\"tag\": \"")
//			.append(tag)
//			.append("\",\"service\":\"")
//			.append(service)
//			.append("\",\"timestamp\" : \"")
//			.append(LocalDateTime.now( ZoneId.of("UTC") ))
//			.append("\"}")
//			.toString();
//	}
	
	private static List<String> getLines(){
		BufferedReader br = null;
		List<String> lines = new ArrayList<String>();
		
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader("/Users/stark/Desktop/imgs/image.out"));
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
	
	private static String random(String[] pool) {
		return pool[ new Random().nextInt(1000) % pool.length ];
	}
}
	