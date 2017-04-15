package ru.bmstu.iu9;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HBaseFillTable {

    private static final byte[] TABLE_NAME = Bytes.toBytes("flights");

    private static final byte[] COLUMN_FAMILY = Bytes.toBytes("data");

    private static final byte[] COLUMN_0_YEAR = Bytes.toBytes("YEAR");
    private static final byte[] COLUMN_1_QUARTER = Bytes.toBytes("QUARTER");
    private static final byte[] COLUMN_2_MONTH = Bytes.toBytes("MONTH");
    private static final byte[] COLUMN_3_DAY_OF_MONTH = Bytes.toBytes("DAY_OF_MONTH");
    private static final byte[] COLUMN_4_DAY_OF_WEEK = Bytes.toBytes("DAY_OF_WEEK");
    private static final byte[] COLUMN_5_FL_DATE = Bytes.toBytes("FL_DATE");
    private static final byte[] COLUMN_6_UNIQUE_CARRIER = Bytes.toBytes("UNIQUE_CARRIER");
    private static final byte[] COLUMN_7_AIRLINE_ID = Bytes.toBytes("AIRLINE_ID");
    private static final byte[] COLUMN_8_CARRIER = Bytes.toBytes("CARRIER");
    private static final byte[] COLUMN_9_TAIL_NUM = Bytes.toBytes("TAIL_NUM");
    private static final byte[] COLUMN_10_FL_NUM = Bytes.toBytes("FL_NUM");
    private static final byte[] COLUMN_11_ORIGIN_AIRPORT_ID = Bytes.toBytes("ORIGIN_AIRPORT_ID");
    private static final byte[] COLUMN_12_ORIGIN_AIRPORT_SEQ_ID = Bytes.toBytes("ORIGIN_AIRPORT_SEQ_ID");
    private static final byte[] COLUMN_13_ORIGIN_CITY_MARKET_ID = Bytes.toBytes("ORIGIN_CITY_MARKET_ID");
    private static final byte[] COLUMN_14_DEST_AIRPORT_ID = Bytes.toBytes("DEST_AIRPORT_ID");
    private static final byte[] COLUMN_15_WHEELS_ON = Bytes.toBytes("WHEELS_ON");
    private static final byte[] COLUMN_16_ARR_TIME = Bytes.toBytes("ARR_TIME");
    private static final byte[] COLUMN_17_ARR_DELAY = Bytes.toBytes("ARR_DELAY");
    private static final byte[] COLUMN_18_ARR_DELAY_NEW = Bytes.toBytes("ARR_DELAY_NEW");
    private static final byte[] COLUMN_19_CANCELLED = Bytes.toBytes("CANCELLED");
    private static final byte[] COLUMN_20_CANCELLATION_CODE = Bytes.toBytes("CANCELLATION_CODE");
    private static final byte[] COLUMN_21_AIR_TIME = Bytes.toBytes("AIR_TIME");
    private static final byte[] COLUMN_22_DISTANCE = Bytes.toBytes("DISTANCE");

    private static final int FL_DATE = 5;
    private static final int AIRLINE_ID = 7;



    public static void main(String[] args) throws IOException {
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeper.quorum","localhost");

        Connection connection = ConnectionFactory.createConnection();

        Admin admin = connection.getAdmin();
        if (!admin.tableExists(TableName.valueOf(TABLE_NAME))) {
            HTableDescriptor descriptor = new HTableDescriptor(TableName.valueOf(TABLE_NAME));
            descriptor.addFamily(new HColumnDescriptor(COLUMN_FAMILY));
            admin.createTable(descriptor);
        }

        Table flights = connection.getTable(TableName.valueOf(TABLE_NAME));

        String flightsData = "/home/runnerpeople/IdeaProjects/Hadoop_Labs/Hadoop_Lab6/664600583_T_ONTIME_sample.csv";
        BufferedReader reader = new BufferedReader(new FileReader(flightsData));

        // except first line //
        String line = reader.readLine();
        int row_number = 1;

        while ((line = reader.readLine()) != null) {
            String[] columns = line.replace("\"","").split(",");
            Put put = new Put(Bytes.toBytes(columns[FL_DATE] + " " + columns[AIRLINE_ID] + " " + row_number));
            put.addColumn(COLUMN_FAMILY,COLUMN_0_YEAR,Bytes.toBytes(columns[0]));
            put.addColumn(COLUMN_FAMILY,COLUMN_1_QUARTER,Bytes.toBytes(columns[1]));
            put.addColumn(COLUMN_FAMILY,COLUMN_2_MONTH,Bytes.toBytes(columns[2]));
            put.addColumn(COLUMN_FAMILY,COLUMN_3_DAY_OF_MONTH,Bytes.toBytes(columns[3]));
            put.addColumn(COLUMN_FAMILY,COLUMN_4_DAY_OF_WEEK,Bytes.toBytes(columns[4]));
            put.addColumn(COLUMN_FAMILY,COLUMN_5_FL_DATE,Bytes.toBytes(columns[5]));
            put.addColumn(COLUMN_FAMILY,COLUMN_6_UNIQUE_CARRIER,Bytes.toBytes(columns[6]));
            put.addColumn(COLUMN_FAMILY,COLUMN_7_AIRLINE_ID,Bytes.toBytes(columns[7]));
            put.addColumn(COLUMN_FAMILY,COLUMN_8_CARRIER,Bytes.toBytes(columns[8]));
            put.addColumn(COLUMN_FAMILY,COLUMN_9_TAIL_NUM,Bytes.toBytes(columns[9]));
            put.addColumn(COLUMN_FAMILY,COLUMN_10_FL_NUM,Bytes.toBytes(columns[10]));
            put.addColumn(COLUMN_FAMILY,COLUMN_11_ORIGIN_AIRPORT_ID,Bytes.toBytes(columns[11]));
            put.addColumn(COLUMN_FAMILY,COLUMN_12_ORIGIN_AIRPORT_SEQ_ID,Bytes.toBytes(columns[12]));
            put.addColumn(COLUMN_FAMILY,COLUMN_13_ORIGIN_CITY_MARKET_ID,Bytes.toBytes(columns[13]));
            put.addColumn(COLUMN_FAMILY,COLUMN_14_DEST_AIRPORT_ID,Bytes.toBytes(columns[14]));
            put.addColumn(COLUMN_FAMILY,COLUMN_15_WHEELS_ON,Bytes.toBytes(columns[15]));
            put.addColumn(COLUMN_FAMILY,COLUMN_16_ARR_TIME,Bytes.toBytes(columns[16]));
            put.addColumn(COLUMN_FAMILY,COLUMN_17_ARR_DELAY,Bytes.toBytes(columns[17]));
            put.addColumn(COLUMN_FAMILY,COLUMN_18_ARR_DELAY_NEW,Bytes.toBytes(columns[18]));
            put.addColumn(COLUMN_FAMILY,COLUMN_19_CANCELLED,Bytes.toBytes(columns[19]));
            put.addColumn(COLUMN_FAMILY,COLUMN_20_CANCELLATION_CODE,Bytes.toBytes(columns[20]));
            put.addColumn(COLUMN_FAMILY,COLUMN_21_AIR_TIME,Bytes.toBytes(columns[21]));
            put.addColumn(COLUMN_FAMILY,COLUMN_22_DISTANCE,Bytes.toBytes(columns[22]));
            flights.put(put);
            row_number++;
        }
        reader.close();
        flights.close();
        connection.close();

    }
}
