package ru.bmstu.iu9;


import com.google.common.io.Files;
import org.apache.storm.shade.com.google.common.base.Charsets;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.spout.IBatchSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BatchSpout implements IBatchSpout {
    private TopologyContext topologyContext;
    private File workingDirectory;

    public BatchSpout() {
        workingDirectory = new File("/home/runnerpeople/IdeaProjects/Hadoop_Labs/Hadoop_Lab8/work_directory/input");
    }

    @Override
    public void open(Map map, TopologyContext topologyContext) {
        this.topologyContext = topologyContext;
    }

    @Override
    public void emitBatch(long l, TridentCollector tridentCollector) {
        File[] filesListDirectory = workingDirectory.listFiles();
        if (filesListDirectory != null && filesListDirectory.length != 0) {
            File currentFile = filesListDirectory[0];
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(currentFile), Charsets.UTF_8));
                // Missing first line //
                String line = reader.readLine();
                while ((line = reader.readLine()) != null) {
                    tridentCollector.emit(new Values(line));
                }
                reader.close();
                Files.move(currentFile, new File("/home/runnerpeople/IdeaProjects/Hadoop_Labs/Hadoop_Lab8/work_directory/output/" + currentFile.getName()));
            }
            catch (FileNotFoundException e) {
                final Logger log = Logger.getLogger(BatchSpout.class.getName());
                log.log(Level.WARNING,"FileNotFoundException in reader");
                e.printStackTrace();
            }
            catch (IOException e) {
                final Logger log = Logger.getLogger(BatchSpout.class.getName());
                log.log(Level.WARNING,"IOException in reader");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void ack(long l) {}

    @Override
    public void close() {}

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    @Override
    public Fields getOutputFields() {
        return new Fields("row");
    }
}
