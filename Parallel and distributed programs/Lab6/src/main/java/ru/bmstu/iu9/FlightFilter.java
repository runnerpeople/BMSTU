package ru.bmstu.iu9;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.exceptions.DeserializationException;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterBase;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlightFilter extends FilterBase {

    private static final String COLUMN_18_ARR_DELAY_NEW = "ARR_DELAY_NEW";
    private static final String COLUMN_19_CANCELLED = "CANCELLED";

    private static final float F_ZERO = 0.0f;


    private float delayTime;
    private boolean remove;

    public FlightFilter() {
        super();
        this.remove = true;
    }

    public FlightFilter(float delayTime) {
        this.delayTime = delayTime;
        this.remove = true;
    }

    @Override
    public void reset() throws IOException {
        this.remove = true;
    }

    @Override
    public ReturnCode filterKeyValue(Cell cell) throws IOException {
        String columnName = Bytes.toString(CellUtil.cloneQualifier(cell));
        String value = Bytes.toString(CellUtil.cloneValue(cell));
        if (columnName.equals(COLUMN_18_ARR_DELAY_NEW)) {
            if (!value.isEmpty() && Float.parseFloat(value) > this.delayTime)
                this.remove = false;
        }
        else {
            if (columnName.equals(COLUMN_19_CANCELLED)) {
                if (Float.parseFloat(value) != F_ZERO)
                    this.remove = false;
            }
        }
        return ReturnCode.INCLUDE;
    }

    @Override
    public boolean filterRow() throws IOException {
        return this.remove;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        return Bytes.toBytes(delayTime);
    }

    public static Filter parseFrom(byte[] pbBytes) throws DeserializationException {
        Logger log = Logger.getLogger(FlightFilter.class.getName());
        log.log(Level.INFO,"Creating custom filter");
        return new FlightFilter(Bytes.toFloat(pbBytes));
    }

}
