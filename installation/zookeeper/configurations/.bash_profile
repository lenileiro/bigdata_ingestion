#ZOOKEEPER START 
export ZOOKEEPER_HOME=/opt/zookeeper
zookeeper() {
	if [ -z "$1" ]
	 then
		 echo "-No Parameter passed [start | stop ]-"
	else
		 /opt/zookeeper/bin/zkServer.sh $1
   fi
}
#ZOOKEEPER STOP
