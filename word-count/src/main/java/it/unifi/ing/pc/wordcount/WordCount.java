package it.unifi.ing.pc.wordcount;

import java.io.File;
import java.net.URI;

import org.apache.commons.io.FileUtils;

import it.unifi.ing.pc.wordcount.config.Config;
import it.unifi.ing.pc.wordcount.elephantdb.DatabaseOperation;
import it.unifi.ing.pc.wordcount.elephantdb.EndpointFactory;

public class WordCount {

	public static void main(String[] args) throws Exception {
//		clearOutputFolder();
//
//		Configuration conf = new Configuration();
//
//		Job job = Job.getInstance(conf, "word count");
//		job.setJarByClass(WordCount.class);
//
//		job.setMapperClass(Map.class);
//		job.setCombinerClass(Reduce.class);
//		job.setReducerClass(Reduce.class);
//
//		job.setOutputKeyClass(Text.class);
//		job.setOutputValueClass(IntWritable.class);
//
//		FileInputFormat.addInputPath(job, new Path(asURI("/input/lorem1.txt")));
//		FileInputFormat.addInputPath(job, new Path(asURI("/input/lorem2.txt")));
//		FileOutputFormat.setOutputPath(job, new Path(Config.OUTPUT_DIR));
//
//		job.waitForCompletion(true);

//		DatabaseOperation.writeToDb( EndpointFactory.getHfs() );
		
		DatabaseOperation.writeToStd( EndpointFactory.getEdb() );
	}
	
	private static void clearOutputFolder() throws Exception {
		FileUtils.deleteDirectory( new File(Config.OUTPUT_DIR) );
	}

}
