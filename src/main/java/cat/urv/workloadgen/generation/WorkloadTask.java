package cat.urv.workloadgen.generation;

import cat.urv.data.DataTypes;

public class WorkloadTask {
	
	public enum OperationType {WRITE, READ};
	
	private Long id = 0L;
	
	private OperationType operationType = OperationType.WRITE;
	
	private Integer size = 0;
	
	private DataTypes dataType = DataTypes.DOCS;
	
	private Long expectedWaitingTime = 0L;
	
	private Long actualWaitingTime = 0L;

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public DataTypes getDataType() {
		return dataType;
	}

	public void setDataType(DataTypes dataType) {
		this.dataType = dataType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getExpectedWaitingTime() {
		return expectedWaitingTime;
	}

	public void setExpectedWaitingTime(Long toWait) {
		this.expectedWaitingTime = toWait;
	}

	public Long getActualWaitingTime() {
		return actualWaitingTime;
	}

	public void setActualWaitingTime(Long actualWaitingTime) {
		this.actualWaitingTime = actualWaitingTime;
	}
	
}
