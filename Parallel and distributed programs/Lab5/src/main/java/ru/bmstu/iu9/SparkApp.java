package ru.bmstu.iu9;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.Map;

public class SparkApp {

    private static final int ORIGIN_AIRPORT_ID = 11;
    private static final int DEST_AIRPORT_ID = 14;
    private static final int DELAY_TIME = 18;
    private static final int IS_CANCELLED = 19;

    private static final float F_ZERO = 0.0f;
    private static final float F_ONE = 1.0f;
    private static final String NULL_STRING = "";


    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("lab5");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> flightsFile = sc.textFile("664600583_T_ONTIME_sample.csv");
        JavaRDD<String> airportsFile = sc.textFile("L_AIRPORT_ID.csv");


        JavaPairRDD<Integer,String> airportsData = airportsFile
                .filter(s -> !s.contains("Code"))
                .mapToPair(s -> {
                    s = s.replace("\"","");
                    int indexFirstComma = s.indexOf(",");
                    return new Tuple2<>(
                            Integer.valueOf(s.substring(0,indexFirstComma)),
                            s.substring(indexFirstComma+1,s.length())
                    );
                });

        final Broadcast<Map<Integer,String>> airportsBroadcasted = sc.broadcast(airportsData.collectAsMap());

        JavaPairRDD<Tuple2<Integer,Integer>,FlightSerializable> flightData = flightsFile
                .filter(s -> !s.contains("YEAR"))
                .mapToPair(s -> {
                    String[] columns = s.split(",");
                    int originAirportID = Integer.parseInt(columns[ORIGIN_AIRPORT_ID]);
                    int destAirportID = Integer.parseInt(columns[DEST_AIRPORT_ID]);
                    float delayTime = (columns[DELAY_TIME].equals(NULL_STRING)) ? F_ZERO : Float.parseFloat(columns[DELAY_TIME]);
                    float isCancelled = Float.parseFloat(columns[IS_CANCELLED]);
                    return new Tuple2<>(new Tuple2<>(originAirportID,destAirportID),
                            new FlightSerializable(originAirportID,destAirportID,delayTime,isCancelled));
                });


        JavaPairRDD<Tuple2<Integer,Integer>,String> flightDataResult = flightData
                .combineByKey(
                p -> new StatisticCount(1,
                                        p.getCancelled() == F_ONE ? 1 : 0,
                                        p.getDelayTime() >  F_ZERO ? 1 : 0,
                                        p.getDelayTime()),
                (statisticCount,p) -> StatisticCount.addValue(statisticCount,
                                        p.getCancelled()==F_ONE,
                                        p.getDelayTime()>F_ZERO,
                                        p.getDelayTime()),
                StatisticCount::add)
                .mapToPair(a->
                        new Tuple2<>(a._1(),StatisticCount.outputString(a._2()))
                );


        JavaRDD<String> result = flightDataResult.map(a -> {
            Map<Integer,String> airportsOriginDestID = airportsBroadcasted.value();
            Tuple2<Integer,Integer> key = a._1();
            String value = a._2();

            String originAirportName = airportsOriginDestID.get(key._1());
            String destAirportName = airportsOriginDestID.get(key._2());

            return "From: " + originAirportName + " -> " + destAirportName + "\t " + value;
        });


        result.saveAsTextFile("hdfs://localhost:9000/user/runnerpeople/output");

        // For output to bash //
//        System.out.println(result.collect());

    }
}
