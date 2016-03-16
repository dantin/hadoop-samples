package com.cosmos.hadoop.sample;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * 统计专利被引用频次
 */
public class CitationHistogram extends Configured implements Tool {

    /**
     * 频次转换
     *
     * example:
     *
     *   专利a被引用2次
     *
     *   (a, 2)
     *
     *   to
     *
     *   引用2次的专利出现1次
     *
     *   (2, 1)
     */
    public static class CountMap extends Mapper<LongWritable, Text, IntWritable, IntWritable> {

        private static final IntWritable UNO = new IntWritable(1);
        private static final String SPLIT = "\t";
        private IntWritable citationCount = new IntWritable();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] record = value.toString().split(SPLIT);
            citationCount.set(Integer.parseInt(record[1]));
            context.write(citationCount, UNO);
        }
    }

    /**
     * 归并统计
     *
     * example
     *
     *   引用2次的专利出现1次
     *   引用2次的专利出现1次
     *
     *   (2, 1)
     *   (2, 1)
     *
     *   to
     *
     *   引用2次的专利出现2次
     *
     *   (2, 2)
     */
    public static class SumReduce extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {

        @Override
        protected void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int count = 0;
            for(IntWritable value : values) {
                count += value.get();
            }
            context.write(key, new IntWritable(count));
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = getConf();

        Job job = Job.getInstance(conf, "CitationHistogram");
        job.setJarByClass(CitationHistogram.class);

        Path in = new Path(args[0]);
        Path out = new Path(args[1]);
        FileInputFormat.setInputPaths(job, in);
        FileOutputFormat.setOutputPath(job, out);

        job.setMapperClass(CountMap.class);
        job.setReducerClass(SumReduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);

        return 0;
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new CitationHistogram(), args);

        System.exit(res);
    }
}
