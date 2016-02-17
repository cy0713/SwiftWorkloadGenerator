package cat.urv.config;

public class WorkloadConfig {
	
	
	//Swift configuration
	public static final String tenant = "spark-tenant";
	public static final String user = "spark-user";
	public static final String password = "spark-pass";
	public static final String authURL = "http://iostack.urv.cat:5000/v2.0/tokens";
	public static final String container = "data";
	
	//Point to the directory where the input workload trace is
	public static final String inputWorkloadTrace = "/media/raul/Data/Documentos/Recerca/Publicaciones/2016/2016-middleware-iostack/datasets/Zeus_Documents/sorted.csv";
	public static final String startTracePoint = "2015-09-09 10:00:00.000";
	public static final String endTracePoint = "2015-09-09 10:00:59.000";
			
	//Point to the dataset characterizations directory
	public static final String datasetCharacterizationsDir = "/media/raul/Data/Documentos/Recerca/Proyectos/IOStack/Code/WorkloadGen/characterizations/";
	
	//Temporal directory where file to be uploaded or downloaded are created
	public static final String tmpFileDir =	"./";
	public static final String resultsFilePath = "./results.dat";

}
