## Step 1 — Install and Configure JDK

Accumulo, HDFS, and ZooKeeper are all written in Java and need a JVM (Java Virtual Machine) to run. So, let's start by installing the JDK.

Update the package list index.

```
    sudo apt-get update
```

Install OpenJDK using apt-get.

```
    sudo apt-get install jdk
```

Use nano to edit your shell environment file, .bashrc.

```
    nano ~/.bashrc
```

Add JAVA_HOME as an environment variable at the end of the file.

```
    export JAVA_HOME=/usr/lib/jvm/java-8-openjdk
```

## Step 2 — Install SSH

Hadoop needs SSH and Rsync to manage its daemons. Install them using the following command:

```
    sudo apt-get install ssh rsync
```

## Step 3 — Enable Passwordless SSH Connectivity

Hadoop should be able to connect to your server over SSH without being prompted for a password.

Generate an RSA key using ssh-keygen.

```
    ssh-keygen -P ''
```
Press ENTER when prompted, to choose the default values.

Add the generated key to the authorized_keys file.

```
    cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
```

The values localhost and 0.0.0.0 should be added to the list of known hosts. The easiest way to do this is by running the ssh command.

Let us add localhost first.

```
    ssh localhost
```

You will be prompted by a message that looks like this:

```
The authenticity of host 'localhost (127.0.0.1)' can't be established.
ECDSA key fingerprint is bf:01:63:5b:91:aa:35:db:ee:f4:7e:2d:36:e7:de:42.
Are you sure you want to continue connecting (yes/no)?
```


```
Type in yes and press ENTER.
```

Once the login is complete, exit the child SSH session by typing in:

```
exit
```

Let us add 0.0.0.0 now.

```
    ssh 0.0.0.0
```

```
Type in yes and press ENTER when prompted.
```
Once again, exit the child SSH session by typing in:

```
exit
```

SSH setup is now complete.

## Step 5 — Download Apache Hadoop

At the time of writing, the latest stable version of Hadoop is 2.9.2. Download it using wget.

```
    wget "http://www.eu.apache.org/dist/hadoop/common/stable/hadoop-2.9.2.tar.gz"
```


Use the tar command to extract the contents of hadoop-2.9.2.tar.gz.

```
    tar -xvzf ~/Downloads/hadoop-2.9.2.tar.gz
```

Move directory to /opt

```
    sudo cp -r hadoop-2.9.2 /opt
```

Change permissions
```
    sudo chmod 777 /opt/hadoop-2.9.2/
```
Change directory 
```
    cd /opt/hadoop-2.9.2
```

Add Java environment path to hadoop-env.sh.

```
nano /opt/hadoop-2.9.2/etc/hadoop/hadoop-env.sh
```

```
    export JAVA_HOME=/usr/lib/jvm/java-11-openjdk
```

## Step 6 — Apache Hadoop Configuration

Use nano to open core-site.xml.

```
nano /opt/hadoop-2.9.2/etc/hadoop/core-site.xml
```

```
    <?xml version="1.0" encoding="UTF-8"?>
    <?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
    <configuration>
        <property>
            <name>fs.defaultFS</name>
            <value>hdfs://localhost:9000</value>
        </property>
    </configuration>
```

Use nano to open hdfs-site.xml.

```
nano /opt/hadoop-2.9.2/etc/hadoop/hdfs-site.xml
```
The following properties need to be added to this file:

   - dfs.replication: This number specifies how many times a block is replicated by Hadoop. By default, Hadoop creates 3 replicas for each block. In this tutorial, use the value 1, as we are not creating a cluster.

   - dfs.name.dir: This points to a location in the filesystem where the namenode can store the name table. You need to change this because Hadoop uses /tmp by default. Let us use hdfs_storage/name to store the name table.

   - dfs.data.dir: This points to a location in the filesystem where the datanode should store its blocks. You need to change this because Hadoop uses /tmp by default. Let us use hdfs_storage/data to store the data blocks.

```
    <?xml version="1.0" encoding="UTF-8"?>
    <?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
    <configuration>
        <property>
            <name>dfs.replication</name>
            <value>1</value>
        </property>
        <property>
            <name>dfs.name.dir</name>
            <value>file:///opt/hadoop/hdfs_storage/name</value>
        </property>
        <property>
            <name>dfs.data.dir</name>
            <value>file:///opt/hadoop/hdfs_storage/data</value>
        </property>
    </configuration>
```

Use nano to create a new file named mapred-site.xml.
```
nano /opt/hadoop-2.9.2/etc/hadoop/mapred-site.xml
```
NB 
    - You can run a MapReduce job on YARN in a pseudo-distributed mode by setting a few parameters and running ResourceManager daemon and NodeManager daemon in addition.
    
```
    <?xml version="1.0"?>
    <?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
    <configuration>
        <property>
            <name>mapred.job.tracker</name>
            <value>localhost:9001</value>
        </property>
        <property>
            <name>mapreduce.framework.name</name>
            <value>yarn</value>
        </property>
    </configuration>
```
Use nano to open yarn-site.xml.
```
nano /opt/hadoop-2.9.2/etc/hadoop/yarn-site.xml
```
```
    <?xml version="1.0"?>
    <?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
    <configuration>
        <property>
            <name>yarn.nodemanager.aux-services</name>
            <value>mapreduce_shuffle</value>
        </property>
    </configuration>
```

## Step 7 — Run Apache Hadoop

The NameNode can now be initialized by typing in:

```
    /opt/hadoop-2.9.2/bin/hdfs namenode -format
```

Start ResourceManager daemon and NodeManager daemon:

```
     /opt/hadoop-2.9.2/sbin/start-yarn.sh
```


Browse the web interface for the ResourceManager; by default it is available at:

ResourceManager - http://localhost:8088/


Next, start the NameNode by typing in:

```
     /opt/hadoop-2.9.2/sbin/start-dfs.sh
```
Browse the web interface for the NameNode; by default it is available at:

NameNode - http://localhost:50070/



## Step 8 - Troubleshooting

If you are not able to access the web interface, check if the NameNode is active by using the following command:
```
jps
```
Your output should contain the following three processes along with the Jps process:

   - DataNode
   - NameNode
   - SecondaryNameNode

```
    sbin/stop-dfs.sh # Stop Hadoop's nodes
    rm -rf hdfs_storage # Delete the namenode data
    rm -rf /tmp/hadoop-* # Delete the temporary directories
    bin/hdfs namenode -format # Reformat the namenode
```

Restart Hadoop using start-dfs.sh

```
    sbin/start-dfs.sh
```