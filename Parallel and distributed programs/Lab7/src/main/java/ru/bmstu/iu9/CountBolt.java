package ru.bmstu.iu9;


import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

public class CountBolt extends BaseRichBolt {
    private OutputCollector outputCollector;
    private Map<String,Integer> tableCounts;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.outputCollector = outputCollector;
        tableCounts = new HashMap<>();
    }

    @Override
    public void execute(Tuple tuple) {
        if (tuple.getSourceStreamId().equals("sync")) {
            tableCounts.forEach((key,value) -> System.out.println(key + " : " + value));
            tableCounts.clear();
        }
        else {
            String word = tuple.getStringByField("word");
            Integer count = tableCounts.get(word);
            if (count == null)
                count = 0;
            count++;
            tableCounts.put(word,count);
            outputCollector.ack(tuple);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {}
}
