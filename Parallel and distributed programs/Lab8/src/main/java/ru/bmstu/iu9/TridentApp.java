package ru.bmstu.iu9;


import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.tuple.Fields;

public class TridentApp {
    public static void main(String[] args) {
        TridentTopology topology = new TridentTopology();

        topology.newStream("generator",new BatchSpout())
                .parallelismHint(1).shuffle()
                .each(new Fields("row"),new SplitFunction(),new Fields("day_of_week","array_delay","is_cancelled")).shuffle()
                .each(new Fields("array_delay","is_cancelled"),new FlightFilter())
                .groupBy(new Fields("day_of_week"))
                .aggregate(new Fields("day_of_week"),new DayAggregator(),new Fields("count"))
                .parallelismHint(7)
                .each(new Fields("day_of_week","count"),new PrintFilter());

        Config config = new Config();
        config.setDebug(false);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("Distribution topology",config,topology.build());
    }
}
