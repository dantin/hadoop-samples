# Hadoop的示例代码

### HDFS

HDFS常用操作

    hdfs dfs -mkdir /user
    hdfs dfs -put etc/hadoop input
    hdfs dfs -cat output/*
    hdfs dfs -rmr wc_test_output
    hdfs dfs -ls

### MapReduce

MapReduce的核心概念

    map: (K1, V1) --> list(K2, V2)
    reduce: (K2, list(V2)) --> list(K3, V3)

### wordcount

词频统计算子

    hadoop jar wordcount/target/wordcount-1.0-SNAPSHOT.jar wc_test_input wc_test_output

### putmerge

上传合并文件集

    hadoop jar putmerge/target/putmerge-1.0-SNAPSHOT.jar input merged.txt
