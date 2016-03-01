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

public class ArcturTraceWorkloadExecutor extends TraceBasedWorkloadExecutor {

	@Override
	public void replayTrace() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(inputWorkloadTrace));
			String line;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long initialDateMillis = format.parse(startTracePoint).getTime();
			long finalDateMillis = format.parse(endTracePoint).getTime();
			long currentExecutionTimeMillis = initialDateMillis;
			//"site100","2016-01-11 14:31:38","GET","2015060113590218"
			while ((line = br.readLine()) != null) {
				line = line.replace("\"", "");
				String[] parts = line.split(",");
				long millis = -1;
				if (!parts[0].equals("site100")) break;
				millis = format.parse(parts[1]).getTime();
				if (millis>=initialDateMillis && millis<=finalDateMillis){
					System.out.println(line);
					WorkloadTask task = new WorkloadTask();
					//Set id to the data object being managed
					task.setId(new Long(parts[3]));
					//Set operation type
					if (parts[2].equals("GET")){
						task.setOperationType(OperationType.READ);
					}else if (parts[2].equals("PUT")){
						task.setOperationType(OperationType.WRITE);
					}else System.err.println("Unkown operation!: " + parts[2]);
					//Set data type for generation
					task.setDataType(DataTypes.DOCS);
					//Set the size of the data object
					task.setSize(1024*128);
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
