package bmstu.iu9;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * Created by runnerpeople on 08.10.16.
 */
public class FlightComparator extends WritableComparator {

    protected FlightComparator() {
        super(FlightWritableComparable.class,true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        FlightWritableComparable that = (FlightWritableComparable) a;
        FlightWritableComparable other = (FlightWritableComparable) b;
        return that.compareToDelayTime(other);
    }
}
