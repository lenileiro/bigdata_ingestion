#HADOOP START 
export HADOOP_HOME=/opt/hadoop

hadoop() {
	if [ -z "$1" ]
		then
			echo "-No Parameter passed [ start | stop ]-"
	else
		/opt/hadoop/sbin/$1-yarn.sh
		/opt/hadoop/sbin/$1-dfs.sh
	fi
}

hadoop_init() {
	/opt/hadoop/sbin/stop-yarn.sh # Stop Hadoop's ResourceManager
	/opt/hadoop/sbin/stop-dfs.sh # Stop Hadoop's nodes
	rm -rf /opt/hadoop/hdfs_storage # Delete the namenode data
	rm -rf /opt/hadoop/tmp/hadoop-* # Delete the temporary directories
	/opt/hadoop/bin/hdfs namenode -format # Reformat the namenode
}
#HADOOP STOP
#HADOOP VARIABLES START
export HADOOP_PREFIX=/opt/hadoop
export HADOOP_COMMON_HOME=/opt/hadoop
export HADOOP_HDFS_HOME=/opt/hadoop
export HADOOP_MAPRED_HOME=/opt/hadoop 
export HADOOP_YARN_HOME=/opt/hadoop
export HADOOP_CONF_DIR=/opt/hadoop/etc/hadoop
export YARN_CONF_DIR=/opt/hadoop/etc/hadoop
export PATH=$PATH:/opt/hadoop/bin
#HADOOP VARIABLES END

