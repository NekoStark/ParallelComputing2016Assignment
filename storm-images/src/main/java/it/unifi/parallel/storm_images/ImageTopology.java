package it.unifi.parallel.storm_images;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

public class ImageTopology {

	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("image_spout", new RandomImageSpout(), 4);
		builder.setBolt("image_parser", new ImageParser(), 4)
				.shuffleGrouping("image_spout");
		builder.setBolt("image_recorder", new ImageRecord(), 4)
			.fieldsGrouping("image_parser", new Fields("searchKey", "link")); 

		
		LocalCluster cluster = new LocalCluster();
		Config conf = new Config();
		cluster.submitTopology("image-search", conf, builder.createTopology());
		
		Utils.sleep(5000);
		
		cluster.killTopology("image-search");
		cluster.shutdown();

		write();
	}
	
	public static void write(){
		try(FileWriter fw = new FileWriter("/home/tommi/Scrivania/Pluto/result.out", true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw)){
			Map<String, String> images = ImageRecord.getImages();
			for (String key : images.keySet()){
				out.println(key + "\t" + images.get(key));
			}
			} catch (IOException e) {
			    System.out.println("Impossibile scrivere sul documento");
			}
	}

}
