package bmstu.iu9;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * Created by runnerpeople on 22.10.16.
 */
public class JoinComparator extends WritableComparator {
    protected JoinComparator() {
        super(JoinWritableComparable.class,true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        JoinWritableComparable that = (JoinWritableComparable) a;
        JoinWritableComparable other = (JoinWritableComparable) b;
        return that.compareToAirportID(other);
    }
}
