package it.unifi.parallel.storm_images.bolt;

import java.util.Arrays;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.storm.guava.base.Throwables;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;

import com.google.common.collect.ImmutableList;
import com.metamx.tranquility.beam.Beam;
import com.metamx.tranquility.druid.DruidBeamConfig;
import com.metamx.tranquility.druid.DruidBeams;
import com.metamx.tranquility.druid.DruidDimensions;
import com.metamx.tranquility.druid.DruidLocation;
import com.metamx.tranquility.druid.DruidRollup;
import com.metamx.tranquility.storm.BeamFactory;
import com.metamx.tranquility.typeclass.Timestamper;

import backtype.storm.task.IMetricsContext;
import io.druid.granularity.QueryGranularity;
import io.druid.query.aggregation.AggregatorFactory;
import io.druid.query.aggregation.CountAggregatorFactory;

@SuppressWarnings("serial")
public class ImageBeamFactory implements BeamFactory<Map<String, Object>> {

	@Override
	public Beam<Map<String, Object>> makeBeam(Map<?, ?> arg0, IMetricsContext arg1) {
		try {
			final CuratorFramework curator = CuratorFrameworkFactory.newClient("127.0.0.1", new RetryOneTime(1000));
			curator.start();
			
			final DruidBeams.Builder<Map<String, Object>, Map<String, Object>> builder = DruidBeams
					.builder(new Timestamper<Map<String, Object>>() {

						@Override
						public DateTime timestamp(Map<String, Object> arg0) {
							System.out.println(arg0);
							return DateTime.now( DateTimeZone.UTC );
						}
						
					})
					.curator(curator)
					.discoveryPath("/druid/discovery")
					.location( DruidLocation.create("druid/overlord", "images") )
					.rollup( DruidRollup.create(DruidDimensions.specific(Arrays.asList("searchKey", "link")), 
							ImmutableList.<AggregatorFactory>of( new CountAggregatorFactory(
	                        "count"
			                )), QueryGranularity.NONE) )
	                .druidBeamConfig(
	                      DruidBeamConfig
	                           .builder()
	                           .indexRetryPeriod(new Period("PT10M"))
	                           .build());

			final Beam<Map<String, Object>> beam = builder.buildBeam();
			return beam;
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

}
