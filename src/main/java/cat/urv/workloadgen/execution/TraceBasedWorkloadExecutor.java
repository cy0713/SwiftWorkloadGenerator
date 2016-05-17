package cat.urv.workloadgen.execution;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.javaswift.joss.model.Container;

import cat.urv.communication.SwiftAPI;
import cat.urv.statistics.StatisticsManager;
import cat.urv.workloadgen.generation.WorkloadWorkerPool; 

public abstract class TraceBasedWorkloadExecutor extends WorkloadExecutor {
	
	protected Set<String> docExtensions = new HashSet<String>(Arrays.asList("pdf", "txt", "tmp", 
			"doc", "odt", "docx", "xls", "csv", "tex", "po", "msg", "word", "pptx", "crt", "ppsx",
			"xlsx", "docm", "ppt", "pps"));

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
