package cat.urv.workloadgen.generation;

import static cat.urv.config.WorkloadConfig.tmpFileDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import org.javaswift.joss.model.Container;

import cat.urv.communication.SwiftAPI;
import cat.urv.data.ContentAwareDataGenerator;
import cat.urv.data.DataTypes;
import cat.urv.statistics.OperationResult;
import cat.urv.statistics.StatisticsManager;

public class WorkloadWorker implements Runnable{
	
	private boolean finish = false; 
	private BlockingQueue<WorkloadTask> taskQueue;
	private Container myContainer;
	private ContentAwareDataGenerator dataGenerator;
	private Set<String> allUploadedFiles;
	private StatisticsManager statisticsManager;

	public WorkloadWorker(BlockingQueue<WorkloadTask> taskQueue, Set<String> allUploadedFiles, Container container) {
		this.taskQueue = taskQueue;
		this.myContainer = container;
		this.dataGenerator = ContentAwareDataGenerator.getInstance();
		this.statisticsManager = StatisticsManager.getInstance();
		this.allUploadedFiles = allUploadedFiles;
		System.out.println("Finishing worker constructor");
	}

	public void run() {
		while (!finish) {
			try {
				processTask(taskQueue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}

	private void processTask(WorkloadTask t) {
		
		SwiftAPI api = new SwiftAPI();
		switch (t.getOperationType()) {
		case WRITE:	
			//Track the time spent on generating the synthetic file
			long generationTimeMillis = System.currentTimeMillis();
			byte[] fileContents = writeTempFile(t.getSize(), t.getDataType());
			generationTimeMillis = System.currentTimeMillis() - generationTimeMillis;
			//Track the time spent on uploading the file
			long uploadStartTimeMillis = System.currentTimeMillis();
			t.setActualWaitingTime(System.currentTimeMillis()-t.getActualWaitingTime());
			boolean failed = api.putObject(myContainer, t.getId().toString(), fileContents);
			long uploadEndTimeMillis = System.currentTimeMillis();
			//Log the result of this file operation
			OperationResult operationResult = new OperationResult(t.getSize(), t.getOperationType(),
					t.getDataType(), uploadStartTimeMillis, uploadEndTimeMillis, failed);
			statisticsManager.logOperationResult(operationResult);
			//Print some debug info
			System.out.println(">>UPLOADING File: " + t.getId().toString() + "\n" +
					"File Size (MBytes): " + t.getSize()/(1024.0*1024.0) + "\n" +
					"Transfer Time (Seconds): " + (uploadEndTimeMillis-uploadStartTimeMillis)/1000.0 + "\n" +
					"Throughput (MBytes/sec.): " + (t.getSize()/(1024.0*1024.0))/((uploadEndTimeMillis-uploadStartTimeMillis)/1000.0) + "\n" +
					"Data Generation Time: " + generationTimeMillis + "\n" +
					"Actual vs Expected Waiting Time: " + t.getExpectedWaitingTime() + " vs " + t.getActualWaitingTime() + "-> " + 
					(t.getActualWaitingTime() - t.getExpectedWaitingTime()));
			//Add the id of this uploaded file to the index, for future downloads
			allUploadedFiles.add(t.getId().toString());
			break;		
		case READ:	
			UUID uuid = UUID.randomUUID();
			String objectId = getAnObjectIdForDownload(t.getId().toString());			
			if (objectId!=null) {	
				long downloadStartTimeMillis = System.currentTimeMillis();
				t.setActualWaitingTime(System.currentTimeMillis()-t.getActualWaitingTime());
				failed = api.getObject(myContainer, objectId, tmpFileDir + uuid);
				long downloadEndTimeMillis = System.currentTimeMillis();
				File donwloadedFile = new File(tmpFileDir + uuid);
				//Log the result of this file operation
				operationResult = new OperationResult(t.getSize(), t.getOperationType(),
						t.getDataType(), downloadStartTimeMillis, downloadEndTimeMillis, failed);
				statisticsManager.logOperationResult(operationResult);
				System.out.println("**DOWNLOADING File: " + objectId + "\n" +
						"File size (MBytes): " + (donwloadedFile.length()/(1024.0*1024.0)) + "\n" +
						"Transfer Time (Seconds): " + ((downloadEndTimeMillis-downloadStartTimeMillis)/1000.0) + "\n" +
						"Throughput (MBytes/sec.): " + ((donwloadedFile.length()/(1024.0*1024.0))/((downloadEndTimeMillis-downloadStartTimeMillis)/1000.0)) + "\n" +
						"Actual vs Expected Waiting Time: " + t.getActualWaitingTime() + " vs " + t.getExpectedWaitingTime() + "-> " + 
						(t.getActualWaitingTime()-t.getExpectedWaitingTime()));
				donwloadedFile.delete();
			}else System.err.println("No files to download yet!");
			break;
		default:
			System.err.println("Incorrect operation type!");
			break;
		}
	}

	private String getAnObjectIdForDownload(String objectId) {
		if (!allUploadedFiles.contains(objectId)) {
			int size = allUploadedFiles.size();
			int randomElement = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
			int i = 0;
			for(String obj : allUploadedFiles){
			    if (i == randomElement) return obj;
			    i = i + 1;
			}
			//objectId = null;
			//Iterator<String> objectIdIterator = allUploadedFiles.iterator();
			//while (objectIdIterator.hasNext()){
			//	objectId = objectIdIterator.next();
			//	break;
			//}
		}
		return objectId;
	}

	/*private void writeTempFile(int fileSize, DataTypes dataType, String tmpDir) {
		int currentSize = 0;
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(new File(tmpDir));
	        //Fill the file with the appropriate contents
	        while (currentSize < fileSize) {
	        	byte[] data = dataGenerator.getDataChunkByType(dataType);
				if (currentSize + data.length > fileSize){
					fileOutputStream.write(Arrays.copyOfRange(data, 0, fileSize - currentSize));
				}else fileOutputStream.write(data);
				currentSize+=data.length;
			}
	        fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}*/
	
	private byte[] writeTempFile(int fileSize, DataTypes dataType) {
		int currentSize = 0;
		byte [] toWrite;
		ByteArrayOutputStream data = new ByteArrayOutputStream(32*1024);
		try {			
	        //Fill the file with the appropriate contents
	        while (currentSize < fileSize) {
	        	toWrite = new byte[32*1024]; //dataGenerator.getDataChunkByType(dataType);
			Arrays.fill(toWrite, (byte)0);
				if (currentSize + toWrite.length > fileSize){
					data.write(Arrays.copyOfRange(toWrite, 0, fileSize - currentSize));
				}else data.write(toWrite);
				currentSize+=toWrite.length;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data.toByteArray();
	}
	public void finish() {
		this.finish = true;
	}

}
