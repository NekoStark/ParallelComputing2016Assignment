package it.unifi.ing.pc;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

public class WikiContributorsTopology {

	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("contribution_spout", new RandomContributorSpout(), 4);
		builder.setBolt("contribution_parser", new ContributionParser(), 4)
				.shuffleGrouping("contribution_spout");
		builder.setBolt("contribution_recorder", new ContributionRecord(), 4)
				.fieldsGrouping("contribution_parser", new Fields("contributorId"));
		
		LocalCluster cluster = new LocalCluster();
		Config conf = new Config();
		cluster.submitTopology("wiki-contributors", conf, builder.createTopology());
		
		Utils.sleep(5000);
		
		cluster.killTopology("wiki-contributors");
		cluster.shutdown();
	}

}
