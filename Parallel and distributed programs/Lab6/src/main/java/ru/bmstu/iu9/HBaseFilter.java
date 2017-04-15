package ru.bmstu.iu9;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HBaseFilter {

    private static final byte[] TABLE_NAME = Bytes.toBytes("flights");

    private static final byte[] COLUMN_FAMILY = Bytes.toBytes("data");

    private static final byte[] COLUMN_18_ARR_DELAY_NEW = Bytes.toBytes("ARR_DELAY_NEW");
    private static final byte[] COLUMN_19_CANCELLED = Bytes.toBytes("CANCELLED");

    private static final byte[] START_ROW = Bytes.toBytes("2015-01-05");
    private static final byte[] STOP_ROW = Bytes.toBytes("2015-01-15");

    private static final float CUSTOM_DELAY_TIME = 10.0f;

    public static void main(String[] args) throws IOException {
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeper.quorum","localhost");

        Connection connection = ConnectionFactory.createConnection();
        Table flights = connection.getTable(TableName.valueOf(TABLE_NAME));

        Scan scan = new Scan();
        scan.addFamily(COLUMN_FAMILY);
        scan.setFilter(new FlightFilter(CUSTOM_DELAY_TIME));
        scan.setStartRow(START_ROW);
        scan.setStopRow(STOP_ROW);

        ResultScanner scanner = flights.getScanner(scan);
        for (Result res: scanner) {
            System.out.print("Key:" + Bytes.toString(res.getRow()) + "; ");
            System.out.print("Delay:" + Bytes.toString(res.getValue(COLUMN_FAMILY,COLUMN_18_ARR_DELAY_NEW)) + "; ");
            System.out.println("Cancelled:" + Bytes.toString(res.getValue(COLUMN_FAMILY,COLUMN_19_CANCELLED)));
        }
        System.out.println("Done!");
        scanner.close();
        flights.close();
        connection.close();
    }
}
