package it.unifi.ing.pc.wordcount;

import java.io.File;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

	private static final String INPUT_FILE = "/input/lorem.txt";
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
	    
	    FileInputFormat.addInputPath(job, new Path(
	    		WordCount.class.getResource("/input/lorem1.txt").toURI()));
	    FileInputFormat.addInputPath(job, new Path(
	    		WordCount.class.getResource("/input/lorem2.txt").toURI()));
	    FileOutputFormat.setOutputPath(job, new Path(OUTPUT_DIR));
	    
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
	
	private static void clearOutputFolder() throws Exception {
		File outputDir = new File(OUTPUT_DIR);
		
		if(outputDir.listFiles() != null) {
			for(File f : outputDir.listFiles()) {
				f.delete();
			}
		}
		
		outputDir.delete();
	}

}
