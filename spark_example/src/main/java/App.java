import org.apache.accumulo.core.client.*;
import org.apache.accumulo.core.client.security.tokens.PasswordToken;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Mutation;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.security.ColumnVisibility;
import org.apache.accumulo.hadoop.mapreduce.AccumuloInputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class App {
     private static final String inputTable = "spark_example_input";
    private static final String outputTable = "spark_example_output";
    private static final Path rootPath = new Path("/spark_example/");

    public static void main(String[] args) throws Exception {
        //logger
        Logger.getLogger("org.apache").setLevel(Level.WARN);

        // Read client properties from file
        Properties props = new Properties();
        props.load(new FileInputStream("/opt/accumulo/conf/accumulo-client.properties"));
        cleanupAndCreateTables(props);

        try (AccumuloClient client = Accumulo.newClient().from(props).build();
             BatchWriter bw = client.createBatchWriter("employess")) {

            //Write data to the server
            Text rowID = new Text("row1");
            Text colFam = new Text("myColFam");
            Text colQual = new Text("myColQual");
            ColumnVisibility colVis = new ColumnVisibility();
            Text Age = new Text("");
            long timestamp = System.currentTimeMillis();

            Value value = new Value("myValue".getBytes());
            Mutation mutation = new Mutation(rowID);
            mutation.put(colFam, colQual, colVis, timestamp, value);

            try {
                bw.addMutation(mutation);
            } catch (MutationsRejectedException e) {
                e.printStackTrace();
            }
        }

        SparkConf conf = new SparkConf();
        conf.setAppName("CopyPlus5K");
        conf.setMaster("local");
        // KryoSerializer is needed for serializing Accumulo Key when partitioning data for bulk import
        conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        conf.registerKryoClasses(new Class[]{Key.class, Value.class, Properties.class});

        JavaSparkContext sc = new JavaSparkContext(conf);

        Job job = Job.getInstance();
        // Read input from Accumulo
        AccumuloInputFormat.configure().clientProperties(props).table(inputTable).store(job);
        JavaPairRDD<Key,Value> data = sc.newAPIHadoopRDD(job.getConfiguration(),
                AccumuloInputFormat.class, Key.class, Value.class);

        data.foreach(o -> System.out.println(o));
        System.out.println(data);
        System.out.println("sucess");
    }
    private static void cleanupAndCreateTables(Properties props) throws Exception {
        FileSystem hdfs = FileSystem.get(new Configuration());
        if (hdfs.exists(rootPath)) {
            hdfs.delete(rootPath, true);
        }
        try (AccumuloClient client = Accumulo.newClient().from(props).build()) {
            if (client.tableOperations().exists(inputTable)) {
                client.tableOperations().delete(inputTable);
            }
            // Create tables
            client.tableOperations().create(inputTable);
        }
    }
}
