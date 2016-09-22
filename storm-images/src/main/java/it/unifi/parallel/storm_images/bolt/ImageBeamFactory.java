package it.unifi.parallel.storm_images.bolt;

import java.util.List;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.joda.time.DateTime;
import org.joda.time.Period;

import com.google.common.collect.ImmutableList;
import com.metamx.common.Granularity;
import com.metamx.tranquility.beam.Beam;
import com.metamx.tranquility.beam.ClusteredBeamTuning;
import com.metamx.tranquility.druid.DruidBeamConfig;
import com.metamx.tranquility.druid.DruidBeams;
import com.metamx.tranquility.druid.DruidDimensions;
import com.metamx.tranquility.druid.DruidLocation;
import com.metamx.tranquility.druid.DruidRollup;
import com.metamx.tranquility.storm.BeamFactory;
import com.metamx.tranquility.typeclass.Timestamper;

import backtype.storm.task.IMetricsContext;
import io.druid.data.input.impl.TimestampSpec;
import io.druid.granularity.QueryGranularity;
import io.druid.query.aggregation.AggregatorFactory;
import io.druid.query.aggregation.CountAggregatorFactory;

public class ImageBeamFactory implements BeamFactory<Map<String, Object>> {

	private static final long serialVersionUID = -2746777814826330964L;

	@Override
    public Beam<Map<String, Object>> makeBeam(Map<?, ?> conf, IMetricsContext metrics) {

        final String indexService = "druid/overlord";
        final String discoveryPath = "/druid/discovery";
        final String dataSource = "speedlayer";
        final List<String> dimensions = ImmutableList.of("searchKey", "link");
        List<AggregatorFactory> aggregator = ImmutableList.<AggregatorFactory>of(
                new CountAggregatorFactory(
                        "click"
                )
        );
        
        // Tranquility needs to be able to extract timestamps from your object type (in this case, Map<String, Object>).
        final Timestamper<Map<String, Object>> timestamper = new Timestamper<Map<String, Object>>() {
			private static final long serialVersionUID = -9089483680138796043L;

			@Override
            public DateTime timestamp(Map<String, Object> theMap) {
                return new DateTime(theMap.get("timestamp"));
            }
        };

        // Tranquility uses ZooKeeper (through Curator) for coordination.
        final CuratorFramework curator = CuratorFrameworkFactory
                .builder()
                .connectString("localhost") // we can use Storm conf to get config values
                .retryPolicy(new ExponentialBackoffRetry(1000, 20, 30000))
                .build();
        curator.start();

        final TimestampSpec timestampSpec = new TimestampSpec("timestamp", "auto", null);

        final Beam<Map<String, Object>> beam = DruidBeams
                .builder(timestamper)
                .curator(curator)
                .discoveryPath(discoveryPath)
                .location(DruidLocation.create(indexService, dataSource))
                .timestampSpec(timestampSpec)
                .rollup(DruidRollup.create(DruidDimensions.specific(dimensions), aggregator, QueryGranularity.MINUTE))
                .tuning(
                        ClusteredBeamTuning
                                .builder()
                                .segmentGranularity(Granularity.HOUR)
                                .windowPeriod(new Period("PT10M"))
                                .partitions(1)
                                .replicants(1)
                                .build()
                )
                .druidBeamConfig(
                        DruidBeamConfig
                                .builder()
                                .indexRetryPeriod(new Period("PT10M"))
                                .build())
                .buildBeam();

        return beam;
    }
}