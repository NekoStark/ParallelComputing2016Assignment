package it.unifi.ing.pc.images.elephantdb;

import cascading.scheme.hadoop.TextLine;
import cascading.tap.hadoop.Dfs;
import cascading.tap.hadoop.Hfs;
import elephantdb.DomainSpec;
import elephantdb.jcascalog.EDB;
import elephantdb.partition.HashModScheme;
import elephantdb.persistence.JavaBerkDB;

public class EndpointFactory {

	public static Hfs getHfs() {
		return new Dfs(new TextLine(), "hdfs://localhost:54310/output/part-r-00000");
	}
	
	public static Object getEdb() {
		DomainSpec spec = new DomainSpec(new JavaBerkDB(), new HashModScheme(), 1);
		return EDB.makeKeyValTap("hdfs://localhost:54310/output/eleph", spec);
	}
	
}
