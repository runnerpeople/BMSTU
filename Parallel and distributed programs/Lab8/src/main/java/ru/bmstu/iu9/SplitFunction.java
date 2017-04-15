package ru.bmstu.iu9;


import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Values;

public class SplitFunction extends BaseFunction {

    private static final int DAY_OF_WEEK = 4;
    private static final int ARR_DELAY_NEW = 18;
    private static final int IS_CANCELLED = 19;
    @Override
    public void execute(TridentTuple tridentTuple, TridentCollector tridentCollector) {
        String[] columns = tridentTuple.getString(0).split(",");
        tridentCollector.emit(new Values(columns[DAY_OF_WEEK],columns[ARR_DELAY_NEW],columns[IS_CANCELLED]));
    }
}
