### Configure Environment

``` bash
bash env.sh
```

### Configure Hadoop
``` bash
sudo hadoop/install.sh
```

### init Hadoop RMBS

``` bash
hadoop_init
```
### start Hadoop

``` bash
hadoop start
```

## Browse the web interface

#### ResourceManager

ResourceManager - http://localhost:8088/

#### NameNode

NameNode - http://localhost:50070/

### Configure Zookeeper

``` bash
sudo zookeeper/install.sh
```

### start Zookeeper
``` bash
hadoop start
```

### Configure Accumulo

``` bash
sudo accumulo/install.sh
```

### start Accumulo
``` bash
accumulo start
```