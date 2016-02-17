package cat.urv.statistics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ArrayBlockingQueue;

import static cat.urv.config.WorkloadConfig.*;

public class StatisticsManager {
	
	private ArrayBlockingQueue<OperationResult> workloadResults = new ArrayBlockingQueue<OperationResult>(16); 
	private static StatisticsManager instance;
	private File resultsFile;
	private ResultsWriter resultsWriter;
	private Thread resultsWriterThread;
	private long startWorkloadTime;
	
	private StatisticsManager () {
		resultsFile = new File(resultsFilePath);
		resultsWriter = new ResultsWriter();
		resultsWriterThread = new Thread(resultsWriter);
		resultsWriterThread.start();
		startWorkloadTime = System.currentTimeMillis();
	}
	
	public static StatisticsManager getInstance() {
		if (instance==null) {
			instance = new StatisticsManager();
		}
		return instance;
	}
	
	public void logOperationResult(OperationResult operationResult){
		workloadResults.add(operationResult);
	}
	
	public void closeStatisticsManager(){
		resultsWriter.finish();
		//Wait for closing the file in the consumer task
		try {
			Thread.sleep(1000);
			resultsWriterThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private class ResultsWriter implements Runnable{
		
		private boolean finish = false;
		
		public ResultsWriter () {}

		public void run() {			
			try {
				FileWriter fileWriter = new FileWriter(resultsFile);
				while (!finish){
					OperationResult operationResult = workloadResults.take();
					if (operationResult!=null){						
						fileWriter.write((operationResult.getStartTime()-startWorkloadTime) + "\t" +
							(operationResult.getEndTime()-startWorkloadTime) + "\t" + 
							(operationResult.getEndTime()-operationResult.getStartTime())/1000.0 + "\t" +
							(operationResult.getFileSize()/(1024.0*1024.0)) + "\t" +
							(operationResult.getFileSize()/(1024.0*1024.0))/((operationResult.getEndTime()-operationResult.getStartTime())/1000.0) + "\t" +
							operationResult.getDataType() + "\t" + 
							operationResult.getOperationType() + "\t" +
							operationResult.isFailed() + "\t" +
							tenant + "\t" + user + "\t" + container + "\n");
					}					
				}			
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 			
		}
		
		public void finish() {
			finish = true;
		}
		
	}
	

}
