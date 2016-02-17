package cat.urv.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {
	
	public List<File> getFilesFromDir(String dirName) {
		List<File> files = new ArrayList<File>();
		File[] listResult = new File(dirName).listFiles();
	    for (int i = 0; i < listResult.length; i++) {
	    	if (listResult[i].isFile()) {
	    		files.add(listResult[i]);
	    	}
	    }
	    return files;
	}
}
