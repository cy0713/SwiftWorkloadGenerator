package cat.urv.workloadgen.execution;

import org.javaswift.joss.model.Container;

import cat.urv.communication.SwiftAPI;
import cat.urv.statistics.StatisticsManager;
import cat.urv.workloadgen.generation.WorkloadWorkerPool; 

public abstract class TraceBasedWorkloadExecutor extends WorkloadExecutor {

	public void executeWorkload(String targetContainer) {
		this.pool = new WorkloadWorkerPool();
		this.statisticsManager = StatisticsManager.getInstance();
		Container container = new SwiftAPI().getSwiftContainer(targetContainer);		
		pool.initialize(container);		
		replayTrace();
		pool.tearDown();
		statisticsManager.closeStatisticsManager();
		new SwiftAPI().cleanContainer(targetContainer);	
	}

	public abstract void replayTrace();
}
