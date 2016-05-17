package cat.urv.workloadgen.execution;

import static cat.urv.config.WorkloadConfig.endTracePoint;
import static cat.urv.config.WorkloadConfig.inputWorkloadTrace;
import static cat.urv.config.WorkloadConfig.startTracePoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import cat.urv.data.DataTypes;
import cat.urv.workloadgen.generation.WorkloadTask;
import cat.urv.workloadgen.generation.WorkloadTask.OperationType;

public class IdiadaTraceWorkloadExecutor extends TraceBasedWorkloadExecutor {

	@Override
	public void replayTrace() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(inputWorkloadTrace));
			String line;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			long initialDateMillis = format.parse(startTracePoint).getTime();
			long finalDateMillis = format.parse(endTracePoint).getTime();
			long currentExecutionTimeMillis = initialDateMillis;
			//2012-01-20 15:42:42.573,WRITE,109,pdf,1,1919150
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				long millis = -1;
				millis = format.parse(parts[0]).getTime();
				if (millis>=initialDateMillis && millis<=finalDateMillis){
					System.out.println(line);
					WorkloadTask task = new WorkloadTask();
					//Set id to the data object being managed
					String objectId = parts[2];
					//Set operation type
					if (parts[1].equals("READ")){
						task.setOperationType(OperationType.READ);
					}else if (parts[1].equals("WRITE")){
						task.setOperationType(OperationType.WRITE);
					}else System.err.println("Unkown operation!: " + parts[2]);
					//Set data type for generation
					if (docExtensions.contains(parts[3])){
						task.setDataType(DataTypes.DOCS);
						objectId+=".txt";
					}else{
						task.setDataType(DataTypes.PICS);
						objectId+=".jpg";
					}
					task.setId(objectId);
					//Set the size of the data object
					task.setSize(new Integer(parts[5]));
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		

	}

}
