#!/bin/bash -eu

wget -O /opt/zookeeper-3.4.14.tar.gz http://www.us.apache.org/dist/zookeeper/zookeeper-3.4.14/zookeeper-3.4.14.tar.gz
tar -xzf /opt/zookeeper-3.4.14.tar.gz -C /opt
rm /opt/zookeeper-3.4.14.tar.gz
mv -i /opt/zookeeper-3.4.14 /opt/zookeeper
cp /opt/zookeeper/conf/zoo_sample.cfg /opt/zookeeper/conf/zoo.cfg

chmod -R 0777 /opt/zookeeper