package ru.bmstu.iu9;


import org.apache.storm.trident.operation.BaseFilter;
import org.apache.storm.trident.tuple.TridentTuple;

public class FlightFilter extends BaseFilter {

    private static final float IN_TIME = 0.0f;

    @Override
    public boolean isKeep(TridentTuple tridentTuple) {
        String arrayDelay =  tridentTuple.getString(0);
        String isCancelled = tridentTuple.getString(1);
        if (!isCancelled.equals("") && isCancelled.equals("1.00"))
            return true;
        else {
            if (!arrayDelay.equals("") && Float.parseFloat(arrayDelay) >= IN_TIME)
                return true;
            else
                return false;
        }
    }
}
