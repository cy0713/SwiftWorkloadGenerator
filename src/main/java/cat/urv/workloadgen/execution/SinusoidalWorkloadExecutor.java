package cat.urv.workloadgen.execution;

import java.util.Random;

import org.javaswift.joss.model.Container;

import cat.urv.communication.SwiftAPI;
import cat.urv.data.DataTypes;
import cat.urv.statistics.StatisticsManager;
import cat.urv.workloadgen.generation.WorkloadTask;
import cat.urv.workloadgen.generation.WorkloadWorkerPool;
import cat.urv.workloadgen.generation.WorkloadTask.OperationType;

public class SinusoidalWorkloadExecutor extends WorkloadExecutor{
	
	public void executeWorkload(String targetContainer) {		
		pool = new WorkloadWorkerPool();
		statisticsManager = StatisticsManager.getInstance();
		Container container = new SwiftAPI().getSwiftContainer(targetContainer);		
		pool.initialize(container);
		for (int i = 0; i<100; i+=1){
			int requestsPerSecond = (int) (Math.sin(Math.toRadians(i%180))*10);
			for (int j=0; j<requestsPerSecond; j++) {
				WorkloadTask t = new WorkloadTask();
				t.setDataType(DataTypes.DOCS);
				t.setId(String.valueOf(new Random().nextLong()));
				if (j%2==0){
					t.setOperationType(OperationType.WRITE);
					t.setSize(128*1024);
				}else t.setOperationType(OperationType.READ);
				pool.ingestJob(t);
				try {
					System.out.println("Waiting:" + 1000.0/requestsPerSecond);
					Thread.sleep(new Double(1000.0/requestsPerSecond).longValue());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}		
		pool.tearDown();
		statisticsManager.closeStatisticsManager();
		new SwiftAPI().cleanContainer(targetContainer);			
	}
}
