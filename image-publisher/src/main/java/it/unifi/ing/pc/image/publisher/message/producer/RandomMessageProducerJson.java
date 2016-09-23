package it.unifi.ing.pc.image.publisher.message.producer;

import java.time.Instant;
import java.util.UUID;

public class RandomMessageProducerJson extends RandomMessageProducer {

	public RandomMessageProducerJson(String[] services, String[] tags) {
		super(services, tags);
	}

	@Override
	public String buildRandomMessage() {
		StringBuffer sb = new StringBuffer();
		return sb.append("{\"url\":\"http://")
				.append(UUID.randomUUID().toString())
				.append(".com\"")
				.append(",\"tag\": \"")
				.append( random(tags) )
				.append("\",\"service\":\"")
				.append( random(services) )
				.append("\",\"timestamp\" : \"")
				.append(Instant.now().toString())
				.append("\"}")
				.toString();
	}

}
