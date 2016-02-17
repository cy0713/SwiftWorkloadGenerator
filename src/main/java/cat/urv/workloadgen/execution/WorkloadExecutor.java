package cat.urv.workloadgen.execution;

import cat.urv.statistics.StatisticsManager;
import cat.urv.workloadgen.generation.WorkloadWorkerPool;

public abstract class WorkloadExecutor {
	
	protected WorkloadWorkerPool pool;
	
	protected StatisticsManager statisticsManager;
	
	public abstract void executeWorkload(String targetContainer);

}