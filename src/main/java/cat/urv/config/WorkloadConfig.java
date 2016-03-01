package cat.urv.config;

public class WorkloadConfig {
	
	
	//Swift configuration
	public static final String tenant = "";
	public static final String user = "";
	public static final String password = "";
	public static final String authURL = "";
	public static final String container = "";
	
	//Point to the directory where the input workload trace is
	public static final String inputWorkloadTrace = "";
			//Zeus_Documents/sorted.csv";
	public static final String startTracePoint = "2016-01-11 14:31:38"; //"2015-09-09 10:00:00.000";
	public static final String endTracePoint = "2016-01-11 14:32:38"; // "2015-09-09 10:00:59.000";
			
	//Point to the dataset characterizations directory
	public static final String datasetCharacterizationsDir = "./characterizations/";
	
	//Temporal directory where file to be uploaded or downloaded are created
	public static final String tmpFileDir =	"./";
	public static final String resultsFilePath = "./results.dat";

}
