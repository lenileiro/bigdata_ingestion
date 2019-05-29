## Download Apache Accumulo

The latest stable version of Accumulo is 1.9.3. Download it using wget.
```
    wget "http://www.eu.apache.org/dist/accumulo/1.6.1/accumulo-2.0.0-alpha-2-bin.tar.gz"
```

Use the tar command to extract the contents of accumulo-2.0.0-alpha-2-bin.tar.gz.

```
    tar -xvzf ~/Downloads/accumulo-2.0.0-alpha-2-bin.tar.gz
```

Move directory to /opt

```
   sudo cp -r accumulo-2.0.0-alpha-2-bin/accumulo-2.0.0-alpha-2/ /opt
```

Change permissions
```
    sudo chmod 777 /opt/accumulo-2.0.0-alpha-2/
```
Change directory 
```
    cd /opt/accumulo-2.0.0-alpha-2/
```

There are four scripts in the bin/ directory that are used to manage Accumulo:

   - accumulo - Runs Accumulo command-line tools and starts Accumulo processes
   - accumulo-service - Runs Accumulo processes as services
   - accumulo-cluster - Manages Accumulo cluster on a single node or several nodes
   - accumulo-util - Accumulo utilities for creating configuration, native libraries, etc.
