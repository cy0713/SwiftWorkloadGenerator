package cat.urv.config;

public class WorkloadConfig {
	
	
	//Swift configuration
	//public static final String tenant = "spark-tenant"; 
	//public static final String user = "spark-user";
	//public static final String password = "spark-pass";
	//public static final String authURL = "http://iostack.urv.cat:5000/v2.0/tokens";

	public static final String tenant = "josep";
	public static final String user = "josep";
	public static final String password = "jsampe1";
	public static final String authURL = "http://192.168.2.1:5000/v2.0/tokens";
	public static final String container = "c1";
	
	//Point to the directory where the input workload trace is
	public static final String inputWorkloadTrace = "../to_execute_idiada.csv";
			//Zeus_Documents/sorted.csv";
	//USE DIFFERENT TENANTS/CONTAINERS TO EXECUTE EACH WORKLOAD CONCURRENTLY AND APPLY POLICIES!
	public static final String startTracePoint = "2015-09-21 09:00:00.000"; //Idiada: "2015-09-21 09:00:00.000"; Arctur: "2016-02-02 06:00:000"
	public static final String endTracePoint = "2015-09-21 21:00:00.000"; // Idiada: "2015-09-21 21:00:00.000"; Arctur: "2016-02-02 18:00:000"
			
	//Point to the dataset characterizations directory
	public static final String datasetCharacterizationsDir = "./characterizations/";
	
	//Temporal directory where file to be uploaded or downloaded are created
	public static final String tmpFileDir =	"./";
	public static final String resultsFilePath = "./results.dat";

}
