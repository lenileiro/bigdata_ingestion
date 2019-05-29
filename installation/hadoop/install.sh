#!/bin/bash -eu

echo "Cleaning"
rm -rf /opt/hadoop
rm -rf /opt/hadoop-3.1.2
wget -O /opt/hadoop-3.1.2.tar.gz https://archive.apache.org/dist/hadoop/core/hadoop-3.1.2/hadoop-3.1.2.tar.gz

echo "Extracting"
tar -xzf /opt/hadoop-3.1.2.tar.gz -C /opt

rm /opt/hadoop-3.1.2.tar.gz
ln -s /opt/hadoop-3.1.2 /opt/hadoop

#set java path
echo "set configurations"
echo 'export JAVA_HOME=/usr/lib/jvm/java-8-openjdk'>>/opt/hadoop/etc/hadoop/hadoop-env.sh
cat hadoop/configurations/core-site.xml > /opt/hadoop/etc/hadoop/core-site.xml
cat hadoop/configurations/hdfs-site.xml > /opt/hadoop/etc/hadoop/hdfs-site.xml
cat hadoop/configurations/mapred-site.xml > /opt/hadoop/etc/hadoop/mapred-site.xml
cat hadoop/configurations/yarn-site.xml > /opt/hadoop/etc/hadoop/yarn-site.xml

mkdir -p /opt/hadoop/input
mkdir -p /var/log/hadoop
mkdir -p /var/log/hadoop-yarn/apps
rm -rf /opt/hadoop-3.1.2/logs
ln -s /var/log/hadoop /opt/hadoop-3.1.2/logs

chmod +x /opt/hadoop/etc/hadoop/*-env.sh

cp /opt/hadoop/etc/hadoop/*.xml /opt/hadoop/input

chmod -R 0777 /opt/hadoop/
chmod -R 0777 /opt/hadoop-3.1.2/
chmod -R 0777 /opt/hadoop-3.1.2/logs
echo "setup done"
