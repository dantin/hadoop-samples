package com.cosmos.hadoop.sample;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;

/**
 * TimeUrl格式的文件加载器
 */
public class TimeUrlTextInputFormat extends FileInputFormat<Text, URLWritable> {

    @Override
    public RecordReader<Text, URLWritable> getRecordReader(InputSplit input, JobConf job, Reporter reporter) throws IOException {
        return new TimeUrlLineRecordReader(job, (FileSplit)input);
    }

}
