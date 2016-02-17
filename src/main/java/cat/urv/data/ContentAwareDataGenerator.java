package cat.urv.data;

import java.util.HashMap;
import java.util.Map;

import com.ibm.characterization.DatasetCharacterization;
import com.ibm.generation.DataProducer;

import static cat.urv.config.WorkloadConfig.*;

public class ContentAwareDataGenerator {
	
	private static ContentAwareDataGenerator instance;
	
	private Map<DataTypes, DataProducer> dataGenerators = new HashMap<DataTypes, DataProducer>(); 
	
	private ContentAwareDataGenerator() {
		for (DataTypes dataType: DataTypes.values()){
			//Load the dataset characterization from the recipe to an object
			DatasetCharacterization characterization = new DatasetCharacterization();
			characterization.load(datasetCharacterizationsDir + dataType);
			//Start a producer for a specific data type
			DataProducer producer = new DataProducer(characterization);
			producer.startProducing();
			//Associate the data type with the producer
			dataGenerators.put(dataType, producer);
		}
	}
	
	public static ContentAwareDataGenerator getInstance() {
		if (instance==null) {
			instance = new ContentAwareDataGenerator();			
		}
		return instance;
	}
	
	public byte[] getDataChunkByType(DataTypes dataType){
		return dataGenerators.get(dataType).getSyntheticData();
	}

}
