package cat.urv.workloadgen.execution;

import static cat.urv.config.WorkloadConfig.endTracePoint;
import static cat.urv.config.WorkloadConfig.inputWorkloadTrace;
import static cat.urv.config.WorkloadConfig.startTracePoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;

import cat.urv.data.DataTypes;
import cat.urv.workloadgen.generation.WorkloadTask;
import cat.urv.workloadgen.generation.WorkloadTask.OperationType;

public class ArcturTraceWorkloadExecutor extends TraceBasedWorkloadExecutor {
	
	int GENERATOR_INDEX = 0;

	@Override
	public void replayTrace() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(inputWorkloadTrace));
			String line;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long initialDateMillis = format.parse(startTracePoint).getTime();
			long finalDateMillis = format.parse(endTracePoint).getTime();
			long currentExecutionTimeMillis = initialDateMillis;
			//"site100","2016-01-11 14:31:38","GET","2015060113590218", mime, extension
			while ((line = br.readLine()) != null) {
				line = line.replace("\"", "");
				String[] parts = line.split(",");
				if ((Long.parseLong(parts[3]) % 2)==GENERATOR_INDEX) continue;
				long millis = -1;
				millis = format.parse(parts[1]).getTime();
				if (millis>=initialDateMillis && millis<=finalDateMillis){
					System.out.println(line);
					WorkloadTask task = new WorkloadTask();
					//Set operation type
					//FIXME: Arctur traces are write only, but we need to do some writes also to correctly execute the workload
					if (parts[2].equals("GET") & new Random().nextFloat() < 0.95){
						task.setOperationType(OperationType.READ);
					}else{
						task.setOperationType(OperationType.WRITE);
					}
					String objectId = parts[3];
					if (docExtensions.contains(parts[6])){
						task.setDataType(DataTypes.DOCS);
						objectId += ".txt";
					}else{
						task.setDataType(DataTypes.PICS);
						objectId += ".jpg";
					}
					//Set id to the data object being managed with the appropriate extension
					task.setId(objectId);					
					//Set the size of the data object
					task.setSize(Integer.parseInt(parts[4]));
					task.setExpectedWaitingTime(millis-currentExecutionTimeMillis);
					task.setActualWaitingTime(System.currentTimeMillis());
					//Wait before executing the operation
					System.out.println("Waiting for execute operation: " + (millis-currentExecutionTimeMillis) + " milliseconds");
					Thread.sleep(millis-currentExecutionTimeMillis);
					//Send the task for execution
					pool.ingestJob(task);
					currentExecutionTimeMillis = millis;
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		

	}
}
