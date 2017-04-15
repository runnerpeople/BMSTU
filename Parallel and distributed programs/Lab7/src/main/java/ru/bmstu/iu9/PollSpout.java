package ru.bmstu.iu9;


import com.google.common.io.Files;
import org.apache.storm.shade.com.google.common.base.Charsets;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.*;

import java.io.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PollSpout extends BaseRichSpout {

    private SpoutOutputCollector spoutOutputCollector;
    private BufferedReader reader;
    private File workingDirectory, currentFile;
    private Boolean readingFileCurrently;

    private int ackCount,emitCount;

    public PollSpout() {
        workingDirectory = new File("/home/runnerpeople/IdeaProjects/Hadoop_Labs/Hadoop_Lab7/work_directory/input");
        readingFileCurrently = false;
        ackCount = 0;
        emitCount = 0;
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.spoutOutputCollector = spoutOutputCollector;
    }

    @Override
    public void nextTuple() {
        if (readingFileCurrently) {
            try {
                String line = reader.readLine();
                if (line != null){
                    spoutOutputCollector.emit("words", new Values(line),emitCount);
                    emitCount++;
                }
                else {
                    if (ackCount == emitCount) {
                        Files.move(currentFile, new File("/home/runnerpeople/IdeaProjects/Hadoop_Labs/Hadoop_Lab7/work_directory/output/" + currentFile.getName()));
                        spoutOutputCollector.emit("sync", new Values());
                        readingFileCurrently = false;
                        ackCount = 0;
                        emitCount = 0;
                    }
                }
            }
            catch (IOException e) {
                final Logger log = Logger.getLogger(PollSpout.class.getName());
                log.log(Level.WARNING,"IOException in reader");
                e.printStackTrace();
            }
        }
        else {
            File[] filesListDirectory = workingDirectory.listFiles();
            if (filesListDirectory == null || filesListDirectory.length == 0) {
                Utils.sleep(100);
            }
            else {
                try {
                    currentFile = filesListDirectory[0];
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(currentFile), Charsets.UTF_8));
                    readingFileCurrently = true;
                }
                catch (FileNotFoundException e) {
                    final Logger log = Logger.getLogger(PollSpout.class.getName());
                    log.log(Level.WARNING,"FileNotFoundException in reader");
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declareStream("words",new Fields("words"));
        outputFieldsDeclarer.declareStream("sync",new Fields());
    }

    @Override
    public void ack(Object msgId) {
        ackCount++;
    }
}
