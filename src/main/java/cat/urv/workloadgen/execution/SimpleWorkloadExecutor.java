package cat.urv.workloadgen.execution;

import java.util.Random;

import org.javaswift.joss.model.Container;

import cat.urv.communication.SwiftAPI;
import cat.urv.data.DataTypes;
import cat.urv.workloadgen.generation.WorkloadTask;
import cat.urv.workloadgen.generation.WorkloadTask.OperationType;
import cat.urv.workloadgen.generation.WorkloadWorkerPool;

public class SimpleWorkloadExecutor extends WorkloadExecutor {
	
	static int BLOCK_SIZE = 32*1024*1024;
	
	static int INITIAL_PUTS = 5;
	
	static int READS = 50;
	
	public void executeWorkload(String targetContainer) {		
		WorkloadWorkerPool pool = new WorkloadWorkerPool();
		Container container = new SwiftAPI().getSwiftContainer(targetContainer);		
		pool.initialize(container);
		//First, initialize the container with several objects (PUT)
		System.out.println("Initializing containers with objects");
		for (int i = 0; i<INITIAL_PUTS; i++){
			WorkloadTask t = new WorkloadTask();
			t.setDataType(DataTypes.DOCS);
			t.setId(String.valueOf(new Random().nextLong()));
			t.setOperationType(OperationType.WRITE);
			t.setSize(BLOCK_SIZE);
			pool.ingestJob(t);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Writing file " + i);
		}
		
		try {
			System.out.println("PUTs done, starting reads");
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Second, do read-oriented workload
		for (int i = 0; i<READS; i++){
			WorkloadTask t = new WorkloadTask();
			t.setDataType(DataTypes.DOCS);
			t.setId(String.valueOf(new Random().nextLong()));
			t.setOperationType(OperationType.READ);
			pool.ingestJob(t);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Reading file " + i);
		}
		
		pool.tearDown();
		new SwiftAPI().cleanContainer(targetContainer);
	}

	

}
