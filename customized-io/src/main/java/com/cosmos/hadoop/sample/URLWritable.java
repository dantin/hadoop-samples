package com.cosmos.hadoop.sample;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * URL对象
 */
public class URLWritable implements Writable {

    protected URL url;

    public URLWritable() {
    }

    public URLWritable(URL url) {
        this.url = url;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(url.toString());
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.url = new URL(in.readUTF());
    }

    public void set(String s) throws MalformedURLException {
        url = new URL(s);
    }
}
