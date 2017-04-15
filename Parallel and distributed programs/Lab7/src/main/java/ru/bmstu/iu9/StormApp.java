package ru.bmstu.iu9;


import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

public class StormApp {
    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("generator",new PollSpout());

        builder.setBolt("splitter",new SplitBolt(),10)
                .shuffleGrouping("generator","words");

        builder.setBolt("counter",new CountBolt(),10)
                .fieldsGrouping("splitter",new Fields("word"))
                .allGrouping("generator","sync");

        Config config = new Config();
        config.setDebug(false);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("Frequency Dictionary", config,builder.createTopology());
    }
}
