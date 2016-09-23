package it.unifi.ing.pc.image.publisher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import it.unifi.ing.pc.image.publisher.message.file.FileUtils;
import it.unifi.ing.pc.image.publisher.message.producer.RandomMessageProducer;
import it.unifi.ing.pc.image.publisher.message.producer.RandomMessageProducerTsv;
import it.unifi.ing.pc.image.publisher.message.properties.KafkaProducerProperties;


public class ImagePublisher {

	private static final String KAFKA_TOPIC = "images";
	private static final String[] SERVICES = {"GOOGLE", "BING", "FLICKR"};
	private static final String INPUT_FILE_LOCATION = "/path/to/input/file";
	private static final String OUTPUT_FILE_LOCATION = "/Users/stark/Desktop/hadoop/input/rawdata";
	
	public static void main(String[] args) {
		
			
		if(args.length == 0) {
			fromFile();
		} else {
			if("demo".equals( args[0] )){
				demoMode(Integer.valueOf(args[1]), Integer.valueOf(args[2]), Arrays.copyOfRange(args, 3, args.length));
			}
			if("online".equals( args[0] )) {
				//TODO implement
			}
		}
		
	}
	
	private static void fromFile() {
		System.out.println("reading from file");
		try(KafkaProducer<String, String> producer = new KafkaProducer<>(KafkaProducerProperties.PROPERTIES)){
			for(String s : new FileUtils(INPUT_FILE_LOCATION).read()) {
				producer.send(new ProducerRecord<String,String>(KAFKA_TOPIC, s));
			}
		}
	}
	
	private static void demoMode(Integer iterations, Integer sleep, String[] tags) {
		System.out.println("demo mode");
		FileUtils fileUtils = new FileUtils(OUTPUT_FILE_LOCATION);
		try(KafkaProducer<String, String> producer = new KafkaProducer<>(KafkaProducerProperties.PROPERTIES)){
			while(true) {
				List<String> messages = new ArrayList<>(iterations);
				RandomMessageProducer randomMessage = new RandomMessageProducerTsv(SERVICES, tags);
				for(int i=0; i<iterations; i++) {
					String message = randomMessage.buildRandomMessage();
					messages.add(message);
					producer.send( new ProducerRecord<String,String>(KAFKA_TOPIC, message) );
				}
				fileUtils.append(messages);
				
				try {
					System.out.println("published "+iterations+" messages");
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
}
	