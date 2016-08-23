package it.unifi.ing.pc.wordcount.elephantdb;

import cascading.scheme.hadoop.TextLine;
import cascading.tap.hadoop.Hfs;
import elephantdb.DomainSpec;
import elephantdb.jcascalog.EDB;
import elephantdb.partition.HashModScheme;
import elephantdb.persistence.JavaBerkDB;
import it.unifi.ing.pc.wordcount.config.Config;

public class EndpointFactory {

	public static Hfs getHfs() {
		return new Hfs(new TextLine(), Config.OUTPUT_DIR + "/part-r-00000");
	}
	
	public static Object getEdb() {
		DomainSpec spec = new DomainSpec(new JavaBerkDB(), new HashModScheme(), 1);
		return EDB.makeKeyValTap(Config.OUTPUT_DIR+"/eleph", spec);
	}
	
}
