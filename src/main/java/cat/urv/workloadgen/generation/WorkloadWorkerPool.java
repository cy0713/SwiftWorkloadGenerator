package cat.urv.workloadgen.generation;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.javaswift.joss.model.Container;

public class WorkloadWorkerPool {
	
	private static final int parallelsm = 16;
	
	private BlockingQueue<WorkloadTask> taskQueue = new ArrayBlockingQueue<WorkloadTask>(parallelsm*100);
	private List<WorkloadWorker> workers = new ArrayList<WorkloadWorker>();
	private ExecutorService threadPool = Executors.newFixedThreadPool(parallelsm);
	
	//If we ask for downloading a non-uploaded, we get a random one from here
	private Set<String> uploadedFiles = new LinkedHashSet<String>();
	

	public WorkloadWorkerPool() {		
	}
	
	public void initialize(Container container){
		System.out.println("Initializing task pool...");
		for (int i=0; i<parallelsm; i++){
			WorkloadWorker worker = new WorkloadWorker(taskQueue, uploadedFiles, container);
			workers.add(worker);
			threadPool.execute(worker);
		}
	}
	
	public void ingestJob(WorkloadTask task) {
		System.out.println("Queued tasks: " + taskQueue.size());
		taskQueue.add(task);
	}
	
	public void tearDown() {
		System.out.println("Finishing tasks...");
		for (WorkloadWorker worker: workers){
			worker.finish();
		}
		System.out.println("Clearing queue...");
		taskQueue.clear();		
		System.out.println("Tearing down pool...");
		threadPool.shutdown();
	}

}