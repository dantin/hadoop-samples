package com.cosmos.hadoop.sample;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.IOException;

/**
 * 上传合并文件集
 */
public class PutMerge {

    public static void main(String[] args) throws IOException {
        Configuration conf = new Configuration();
        FileSystem hdfs = FileSystem.get(conf);
        FileSystem local = FileSystem.getLocal(conf);

        Path inputDir = new Path(args[0]);
        Path hdfsFile = new Path(args[1]);

        try {
            FileStatus[] inputFiles = local.listStatus(inputDir);
            FSDataOutputStream out = hdfs.create(hdfsFile);

            for (FileStatus inputFile : inputFiles) {
                System.out.println(inputFile.getPath().getName());
                FSDataInputStream in = local.open(inputFile.getPath());
                byte buffer[] = new byte[256];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                in.close();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
