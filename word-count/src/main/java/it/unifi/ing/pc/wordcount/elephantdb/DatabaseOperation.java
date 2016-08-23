package it.unifi.ing.pc.wordcount.elephantdb;

import com.twitter.maple.tap.StdoutTap;

import cascading.flow.FlowProcess;
import cascading.operation.FunctionCall;
import cascading.tuple.Tuple;
import cascalog.CascalogFunction;
import jcascalog.Api;
import jcascalog.Subquery;

@SuppressWarnings({"serial", "rawtypes"})
public class DatabaseOperation {

	public static void writeToDb(Object tap) {
		Subquery toEdb =
				new Subquery("?key", "?value")
				.predicate(tap, "?lineNum", "?line")
				.predicate(new CascalogFunction() {
					//FIXME queste poi possono diventare classi separate
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
		
		Api.execute(EndpointFactory.getEdb(), toEdb);
	}
	
	public static void writeToStd(Object tap) {
		Subquery toStd = new Subquery("?word", "?count")
				.predicate(tap, "?key", "?value")
				.predicate(new CascalogFunction() {
					//FIXME cast verso e da byte più esplicito?
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
	
}
