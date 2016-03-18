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

### put-merge

上传合并文件集

    hadoop jar putmerge/target/putmerge-1.0-SNAPSHOT.jar input merged.txt


hadoop jar patent/target/patent-1.0-SNAPSHOT.jar com.cosmos.hadoop.sample.InvertedCitation input/cite75_99.txt output
hadoop jar patent/target/patent-1.0-SNAPSHOT.jar com.cosmos.hadoop.sample.CitationCount input/cite75_99.txt citation_count
hadoop jar patent/target/patent-1.0-SNAPSHOT.jar com.cosmos.hadoop.sample.CitationHistogram citation_count citation_histogram

### Hadoop Streaming

    hadoop jar share/hadoop/tools/lib/hadoop-streaming-2.7.2.jar \
        -input input/cite75_99.txt \
        -output test \
        -mapper 'cut -f 2 -d ,' \
        -reducer 'uniq'
    
    hadoop jar share/hadoop/tools/lib/hadoop-streaming-2.7.2.jar \
        -D mapred.reduce.tasks=0 \
        -input test \
        -output test_a \
        -mapper 'wc -l'

