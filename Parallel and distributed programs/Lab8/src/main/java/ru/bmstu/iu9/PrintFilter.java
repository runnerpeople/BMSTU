package ru.bmstu.iu9;


import org.apache.storm.trident.operation.BaseFilter;
import org.apache.storm.trident.tuple.TridentTuple;

public class PrintFilter extends BaseFilter {
    public PrintFilter() {
    }

    @Override
    public boolean isKeep(TridentTuple tridentTuple) {
        System.out.println(tridentTuple);
        return false;
    }
}
