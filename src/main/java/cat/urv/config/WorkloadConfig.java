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
	public static final String startTracePoint = "2015-09-09 10:00:00.000";
	public static final String endTracePoint = "2015-09-09 10:00:59.000";
			
	//Point to the dataset characterizations directory
	public static final String datasetCharacterizationsDir = "";
	
	//Temporal directory where file to be uploaded or downloaded are created
	public static final String tmpFileDir =	"./";
	public static final String resultsFilePath = "./results.dat";

}
