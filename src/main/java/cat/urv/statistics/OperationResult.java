package cat.urv.statistics;

import cat.urv.data.DataTypes;
import cat.urv.workloadgen.generation.WorkloadTask.OperationType;

public class OperationResult {
	
	private int fileSize;
	
	private OperationType operationType;
	
	private DataTypes dataType;
	
	private long startTime;
	
	private long endTime;
	
	private boolean failed;

	public OperationResult(int fileSize, OperationType operationType, DataTypes dataType, long startTime, long endTime,
			boolean failed) {
		this.fileSize = fileSize;
		this.operationType = operationType;
		this.dataType = dataType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.failed = failed;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	public DataTypes getDataType() {
		return dataType;
	}

	public void setDataType(DataTypes dataType) {
		this.dataType = dataType;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public boolean isFailed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}

}
