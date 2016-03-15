package com.cosmos.hadoop.sample;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 路径节点对象
 *
 * example:
 *
 *   (San Francisco, Los Angeles)
 *   (San Francisco, Dallas)
 *
 */
public class Edge implements WritableComparable<Edge> {

    private String departureNode;
    private String arrivalNode;

    public String getDepartureNode() {
        return departureNode;
    }

    @Override
    public int compareTo(Edge o) {
        return (departureNode.compareTo(o.departureNode) != 0)
                ? departureNode.compareTo(o.departureNode)
                : arrivalNode.compareTo(o.arrivalNode);

    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(departureNode);
        out.writeUTF(arrivalNode);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        departureNode = in.readUTF();
        arrivalNode = in.readUTF();
    }
}
