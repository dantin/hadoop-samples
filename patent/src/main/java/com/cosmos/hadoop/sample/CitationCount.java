package com.cosmos.hadoop.sample;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * 统计专利被引用次数
 */
public class CitationCount extends Configured implements Tool {

    /**
     * 反转引用
     *
     * example:
     *
     *   专利a引用专利b, c, d
     *
     *   (a, b)
     *   (a, c)
     *   (a, d)
     *
     *   to
     *
     *   专利b被专利a引用
     *   专利c被专利a引用
     *   专利d被专利a引用
     *
     *   (b, a)
     *   (c, a)
     *   (d, a)
     */
    public static class InvertedMapper extends Mapper<LongWritable, Text, Text, Text> {

        private static final String COMMA = ",";

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] citation = value.toString().split(COMMA);
            context.write(new Text(citation[1]), new Text(citation[0]));
        }
    }

    /**
     * 归并计数
     *
     * example
     *
     *   专利b被专利a引用
     *   专利b被专利c引用
     *
     *   (b, a)
     *   (b, c)
     *
     *   to
     *
     *   专利b被引用2次
     *
     *   (b, 2)
     */
    public static class CountReduce extends Reducer<Text, Text, Text, IntWritable> {

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            int count = 0;
            for (Text ignored : values) {
                count++;
            }
            context.write(key, new IntWritable(count));
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = getConf();

        Job job = Job.getInstance(conf, "CitationCount");
        job.setJarByClass(CitationCount.class);

        Path in = new Path(args[0]);
        Path out = new Path(args[1]);
        FileInputFormat.setInputPaths(job, in);
        FileOutputFormat.setOutputPath(job, out);

        job.setMapperClass(InvertedMapper.class);
        job.setReducerClass(CountReduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);

        return 0;
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new CitationCount(), args);

        System.exit(res);
    }
}
