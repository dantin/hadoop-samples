package com.cosmos.hadoop.sample;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Partitioner;

/**
 * {@code Edge}的Shuffle对象
 */
public class EdgePartitioner implements Partitioner<Edge, Writable> {

    @Override
    public int getPartition(Edge key, Writable value, int numPartitions) {
        return key.getDepartureNode().hashCode() % numPartitions;
    }

    @Override
    public void configure(JobConf job) {
    }
}
