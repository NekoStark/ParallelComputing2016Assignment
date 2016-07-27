package it.unifi.ing.pc.wordcount;

import static it.unifi.ing.pc.utilities.ResourceLoader.asURI;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.twitter.maple.tap.StdoutTap;

import cascading.flow.FlowProcess;
import cascading.operation.FunctionCall;
import cascading.scheme.hadoop.TextLine;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Tuple;
import cascalog.CascalogFunction;
import elephantdb.DomainSpec;
import elephantdb.jcascalog.EDB;
import elephantdb.partition.HashModScheme;
import elephantdb.persistence.JavaBerkDB;
import jcascalog.Api;
import jcascalog.Subquery;

public class WordCount {

	private static final String OUTPUT_DIR = "src/main/resources/out";

	public static void main(String[] args) throws Exception {
		clearOutputFolder();

		Configuration conf = new Configuration();

		Job job = Job.getInstance(conf, "word count");
		job.setJarByClass(WordCount.class);

		job.setMapperClass(Map.class);
		job.setCombinerClass(Reduce.class);
		job.setReducerClass(Reduce.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(asURI("/input/lorem1.txt")));
		FileInputFormat.addInputPath(job, new Path(asURI("/input/lorem2.txt")));
		FileOutputFormat.setOutputPath(job, new Path(OUTPUT_DIR));

		job.waitForCompletion(true);

		DomainSpec spec = new DomainSpec(new JavaBerkDB(), new HashModScheme(), 1);

		Tap tap = new Hfs(new TextLine(), OUTPUT_DIR + "/part-r-00000");
		Object sink = EDB.makeKeyValTap(OUTPUT_DIR+"/eleph", spec);

		Subquery toEdb =
				new Subquery("?key", "?value")
				.predicate(tap, "?lineNum", "?line")
				.predicate(new CascalogFunction() {
					
					@Override
					public void operate(FlowProcess flow_process, FunctionCall fn_call) {
						String val1 = fn_call.getArguments().getString(0).toString().split("\t")[0];
						fn_call.getOutputCollector().add(new Tuple(val1.getBytes()));
						
					}
				}, "?line")
					.out("?key")
				.predicate(new CascalogFunction() {
					
					@Override
					public void operate(FlowProcess flow_process, FunctionCall fn_call) {
						String val1 = fn_call.getArguments().getString(0).toString().split("\t")[1];
						fn_call.getOutputCollector().add(new Tuple(val1.getBytes()));
						
					}
				}, "?line")
					.out("?value");
		
		Api.execute(sink, toEdb);
		
		Subquery toStd = new Subquery("?word", "?count")
								.predicate(sink, "?key", "?value")
								.predicate(new CascalogFunction() {
									
									@Override
									public void operate(FlowProcess flow_process, FunctionCall fn_call) {
										fn_call.getOutputCollector().add(new Tuple(new String( (byte[])fn_call.getArguments().getObject(0) )));
										
									}
								}, "?key").out("?word")
								.predicate(new CascalogFunction() {
									
									@Override
									public void operate(FlowProcess flow_process, FunctionCall fn_call) {
										fn_call.getOutputCollector().add(new Tuple(new String( (byte[])fn_call.getArguments().getObject(0) )));
										
									}
								}, "?value").out("?count");
		
		Api.execute(new StdoutTap(), toStd);
	}
	
	private static void clearOutputFolder() throws Exception {
		FileUtils.deleteDirectory( new File(OUTPUT_DIR) );
	}

}
