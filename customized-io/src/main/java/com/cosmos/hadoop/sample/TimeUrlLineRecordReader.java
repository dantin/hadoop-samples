package com.cosmos.hadoop.sample;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.KeyValueLineRecordReader;
import org.apache.hadoop.mapred.RecordReader;

import java.io.IOException;

/**
 * TimeUrl格式的行数据加载器,以{@code Tab}分割
 *
 * Example:
 *
 *   17:16:18    http://hadoop.apache.org/core/docs/r0.19.0/api/index.html
 */
public class TimeUrlLineRecordReader implements RecordReader<Text, URLWritable> {

    private KeyValueLineRecordReader lineReader;
    private Text lineKey, lineValue;

    public TimeUrlLineRecordReader(JobConf job, FileSplit split) throws IOException {
        lineReader = new KeyValueLineRecordReader(job, split);
        lineKey = lineReader.createKey();
        lineValue = lineReader.createValue();
    }

    @Override
    public boolean next(Text key, URLWritable value) throws IOException {
        if (!lineReader.next(lineKey, lineValue)) {
            return false;
        }
        key.set(lineKey);
        // 将Text对象转换成URL对象
        value.set(lineValue.toString());
        return true;
    }

    @Override
    public Text createKey() {
        return new Text("");
    }

    @Override
    public URLWritable createValue() {
        return new URLWritable();
    }

    @Override
    public long getPos() throws IOException {
        return lineReader.getPos();
    }

    @Override
    public void close() throws IOException {
        lineReader.close();
    }

    @Override
    public float getProgress() throws IOException {
        return lineReader.getProgress();
    }
}
