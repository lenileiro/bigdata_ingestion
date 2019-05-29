import org.apache.accumulo.core.client.*;
import org.apache.accumulo.core.client.security.tokens.PasswordToken;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Mutation;
import org.apache.accumulo.core.data.Range;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.security.Authorizations;
import org.apache.accumulo.core.security.ColumnVisibility;
import org.apache.hadoop.io.Text;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class App {
    public static void main(String[] args){
        new App().run();

    }
    private static Connector conn;
    private static final String DEFAULT_AUTHS = "public";

    private void run(){
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("/opt/hadoop/share/hadoop/tools/sls/sample-conf/log4j.properties"));
            PropertyConfigurator.configure(props);

            //Connect to the Accumulo and Zookeeper server
            String instanceName = "INSTANCE";
            //Note that this zooserver address maps to the IP address of my Hortonworks sandbox. Edit your /etc/hosts file on your local machine to contain the sandbox IP address
            String zooServers = "localhost";
            Instance inst = new ZooKeeperInstance(instanceName, zooServers);
            Connector conn = inst.getConnector("root", new PasswordToken("secret"));

            //give the table to be created a name
            String tableName = "testTable";
            //create table if it doesn't already exist
            try {
                conn.tableOperations().create(tableName);
            } catch (TableExistsException e) {
                System.out.println("Table " + tableName + " currently exists");
            }
            //Write data to the server
            Text rowID = new Text("row1");
            Text colFam = new Text("myColFam");
            Text colQual = new Text("myColQual");
            ColumnVisibility colVis = new ColumnVisibility("public");
            long timestamp = System.currentTimeMillis();

            Value value = new Value("myValue".getBytes());

            //Construct the mutation to send to the server
            Mutation mutation = new Mutation(rowID);
            mutation.put(colFam, colQual, colVis, timestamp, value);

            //Send the mutation to the table on the server using the BatchWriter

            //BatchWriterConfig has reasonable defaults
            BatchWriterConfig config = new BatchWriterConfig();
            config.setMaxMemory(10000000L); // bytes available to BatchWriter for buffering mutations

            try {
                //write to the tableName using a BatchWriter
                BatchWriter writer = conn.createBatchWriter(tableName, config);
                writer.addMutation(mutation);
                writer.close();
            } catch (TableNotFoundException e) {
                System.out.println("Error: Table " + tableName + " was not found when executing writer");
            }

            //now that data has been inserted, let's read it

            // specify which visibilities we are allowed to see

            try {
                Authorizations auths = new Authorizations(DEFAULT_AUTHS.split(","));
                //create scanner
                Scanner scan = conn.createScanner(tableName, auths);

                //set the range for the scan
                scan.setRange(new Range("row1", "row2"));

                //use the colFam variable created up top to specify the scan to only return rows with 'myColFam' as the column family
                scan.fetchColumnFamily(colFam);

                //iterate through the entries and print them to the console
                for (Map.Entry<Key, Value> entry : scan) {
                    System.out.println(entry.getKey().toString() + " -> " + entry.getValue().toString());
                }
            } catch (TableNotFoundException e) {
                System.out.println("Error: Table " + tableName + " was not found when executing scanner");
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AccumuloException e) {
            e.printStackTrace();
        } catch (AccumuloSecurityException e) {
            e.printStackTrace();
        }
    }
}
