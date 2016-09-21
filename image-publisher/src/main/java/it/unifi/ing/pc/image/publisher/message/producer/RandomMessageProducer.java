package it.unifi.ing.pc.image.publisher.message.producer;

import java.util.Random;

public abstract class RandomMessageProducer {

	String[] services;
	String[] tags;
	
	public RandomMessageProducer(String[] services, String[] tags) {
		this.services = services;
		this.tags = tags;
	}
	
	public abstract String buildRandomMessage();
	
	String random(String[] pool) {
		return pool[ new Random().nextInt(1000) % pool.length ];
	}
	
}