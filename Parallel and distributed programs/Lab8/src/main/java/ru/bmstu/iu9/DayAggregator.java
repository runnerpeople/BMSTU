package ru.bmstu.iu9;


import org.apache.storm.trident.operation.CombinerAggregator;
import org.apache.storm.trident.tuple.TridentTuple;

public class DayAggregator implements CombinerAggregator<Long> {
    @Override
    public Long init(TridentTuple tridentTuple) {
        return 1L;
    }

    @Override
    public Long combine(Long aLong, Long t1) {
        return aLong + t1;
    }

    @Override
    public Long zero() {
        return 0L;
    }
}
