#!/bin/bash -eu
echo "downloading"
rm -rf /opt/accumulo
rm -rf /opt/accumulo-2.0.0-alpha/
wget -O /opt/accumulo-2.0.0-alpha-2-bin.tar.gz http://archive.apache.org/dist/accumulo/2.0.0-alpha-2/accumulo-2.0.0-alpha-2-bin.tar.gz
tar -xzf /opt/accumulo-2.0.0-alpha-2-bin.tar.gz -C /opt
rm /opt/accumulo-2.0.0-alpha-2-bin.tar.gz

echo "setup"
ln -s /opt/accumulo-2.0.0-alpha-2/ /opt/accumulo
rm -rf /opt/accumulo-2.0.0-alpha-2/logs
mkdir -p /var/log/accumulo
ln -s /var/log/accumulo /opt/accumulo-2.0.0-alpha-2/logs

cat accumulo/configurations/accumulo-client.properties > /opt/accumulo/conf/accumulo-client.properties
cat accumulo/configurations/accumulo.properties > /opt/accumulo/conf/accumulo.properties

chmod -R 0777 /opt/accumulo
echo "complete"