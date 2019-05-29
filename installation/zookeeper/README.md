## Download Apache ZooKeeper

The latest stable version of ZooKeeper is 3.4.14. Download it using wget.
```
    wget "http://www.eu.apache.org/dist/zookeeper/stable/zookeeper-3.4.14.tar.gz"
```

## Install and Configure ZooKeeper

Use tar to extract zookeeper-3.4.14.tar.gz.

```
    tar -xvzf ~/Downloads/zookeeper-3.4.14.tar.gz
```
Move directory to /opt

```
    sudo cp -r zookeeper-3.4.14 /opt
```

Change permissions
```
    sudo chmod 777 /opt/zookeeper-3.4.14/
```
Change directory 
```
    cd /opt/zookeeper-3.4.14
```

Copy the example file zoo_sample.cfg to zoo.cfg.

```
    cp /opt/zookeeper-3.4.14/conf/zoo_sample.cfg /opt/zookeeper-3.4.14/conf/zoo.cfg
```

Configuration of ZooKeeper is now complete. Start ZooKeeper by typing in:

```
    /opt/zookeeper-3.4.14/bin/zkServer.sh start
```

You should see output that looks like this:

```
JMX enabled by default
Using config: ~/Installs/zookeeper-3.4.14/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED
```