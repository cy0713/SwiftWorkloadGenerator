package cat.urv.workloadgen.execution;

import java.util.Random;

import org.javaswift.joss.model.Container;

import cat.urv.communication.SwiftAPI;
import cat.urv.data.DataTypes;
import cat.urv.workloadgen.generation.WorkloadTask;
import cat.urv.workloadgen.generation.WorkloadTask.OperationType;
import cat.urv.workloadgen.generation.WorkloadWorkerPool;

public class SimpleWorkloadExecutor extends WorkloadExecutor {
	
	public void executeWorkload(String targetContainer) {		
		WorkloadWorkerPool pool = new WorkloadWorkerPool();
		Container container = new SwiftAPI().getSwiftContainer(targetContainer);		
		pool.initialize(container);
		for (int i = 0; i<10; i++){
			WorkloadTask t = new WorkloadTask();
			t.setDataType(DataTypes.DOCS);
			t.setId(new Random().nextLong());
			t.setOperationType(OperationType.WRITE);
			t.setSize(32*1024);
			pool.ingestJob(t);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Writing file " + i);
		}
		
		pool.tearDown();
		new SwiftAPI().cleanContainer(targetContainer);
	}

	

}
