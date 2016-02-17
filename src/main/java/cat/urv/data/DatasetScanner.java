package cat.urv.data;

import com.ibm.characterization.DatasetCharacterization;
import com.ibm.scan.DataScanner;

public class DatasetScanner {
	
	private static String datasetPath = "/home/raul/Desktop/";
	private static String originalDataset = datasetPath + "text.tar";
	private static int chunkSize = 32*1024;
	
	public static void main(String[] args) {
		
		//1) Scan a dataset. If the dataset is a collection of files, to run this test is better
		//to pack them into a .tar fall to do a fair comparison with the synthetic file generated.
		DataScanner scanner = new DataScanner();
		long time = System.currentTimeMillis();
		scanner.scan(originalDataset, chunkSize);
		
		//2) Here the scan process finishes. In this point we want to persist the characterization of this
		//dataset in a sharable data structure.
		DatasetCharacterization characterization = scanner.finishScanAndBuildCharacterization();
		characterization.save(datasetPath + "docs");
		
		//3) In this point, we load the dataset characterization just created to show how characterization can be loaded
		//and shared among users.
		DatasetCharacterization newCharacterization = new DatasetCharacterization();
		newCharacterization.load(datasetPath + "docs");
		System.out.println("Scanning time: " + (System.currentTimeMillis()-time)/1000.0);		
	}

}
