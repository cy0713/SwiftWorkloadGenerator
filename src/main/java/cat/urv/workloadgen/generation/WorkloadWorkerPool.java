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
	
	private static final int parallelism = 1;
	
	private ArrayList<ArrayBlockingQueue<WorkloadTask>> taskQueues = new ArrayList<ArrayBlockingQueue<WorkloadTask>>();
	private List<WorkloadWorker> workers = new ArrayList<WorkloadWorker>();
	//private ExecutorService threadPool = Executors.newFixedThreadPool(parallelism);
	
	//If we ask for downloading a non-uploaded, we get a random one from here
	private Set<String> uploadedFiles = new LinkedHashSet<String>();
	

	public WorkloadWorkerPool() {		
	}
	
	public void initialize(Container container){
		System.out.println("Initializing task pool...");
		for (int i=0; i<parallelism; i++){
			ArrayBlockingQueue<WorkloadTask> taskQueue = new ArrayBlockingQueue<WorkloadTask>(parallelism*10);
			taskQueues.add(taskQueue);
			WorkloadWorker worker = new WorkloadWorker(taskQueue, uploadedFiles, container);
			Thread t = new Thread(worker);
			workers.add(worker);
			t.start();
			//threadPool.execute(worker);
		}
	}
	
	public void ingestJob(WorkloadTask task) {
		try {
			//Get the numerical id of the identifier (without the file extension)
			long numericObjectId = Long.parseLong(task.getId().substring(0, task.getId().length()-4));
			//Infer the correct queue to push the task
			int taskQueueIndex = (int) (numericObjectId%parallelism);
			System.out.println("Queued tasks: " + taskQueues.get(taskQueueIndex).size());
			taskQueues.get(taskQueueIndex).add(task);
		} catch (Exception e) {
			System.err.println("Error routing task to the queue: " + task.getId());
			e.printStackTrace();
		}
	}
	
	public void tearDown() {
		System.out.println("Finishing tasks...");
		for (WorkloadWorker worker: workers){
			worker.finish();
		}
		System.out.println("Clearing queue...");
		for (int i=0; i<parallelism; i++)
			taskQueues.get(i).clear();		
		System.out.println("Tearing down pool...");
		//threadPool.shutdown();
	}

}