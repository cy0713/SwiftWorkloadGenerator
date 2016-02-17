package cat.urv.communication;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;

import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.client.factory.AccountFactory;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import static cat.urv.config.WorkloadConfig.*;

public class SwiftAPI {
	
	public Account getSiwftAccount(String tenant, String user, String password, String authURL) {
		AccountConfig config = new AccountConfig();
    	config.setUsername(user);
    	config.setPassword(password);
    	config.setAuthUrl(authURL);
    	config.setTenantName(tenant);
    	return new AccountFactory(config).createAccount();
	}
	
	public Account getSiwftAccount() {
    	return getSiwftAccount(tenant, user, password, authURL);
	}
	
	public Container getSiwftContainer(String tenant, String user, String password, String authURL, String container) {
		AccountConfig config = new AccountConfig();
    	config.setUsername(user);
    	config.setPassword(password);
    	config.setAuthUrl(authURL);
    	config.setTenantName(tenant);
    	Account account = new AccountFactory(config).createAccount();
		return account.getContainer(container);
	}
	
	public Container getSwiftContainer(String container) {
		return getSiwftAccount().getContainer(container);
	}
	
	public boolean putObject(Container container, String remoteName, String localFilePath) {
		boolean failed = false;
		try {
			StoredObject object = container.getObject(remoteName);
			object.uploadObject(new File(localFilePath));
		}catch (Exception e){
			failed = true;
			e.printStackTrace();
		}
		return failed;
	}
	
	public boolean putObject(Container container, String remoteName, byte[] fileContents) {
		boolean failed = false;
		try {
			StoredObject object = container.getObject(remoteName);
	        object.uploadObject(fileContents);
		}catch (Exception e){
			failed = true;
			e.printStackTrace();
		}
		return failed;
	}
	
	public boolean getObject(Container container, String remoteName, String localFilePath) {
		boolean failed = false;
		try {
			StoredObject object = container.getObject(remoteName);
	        object.downloadObject(new File(localFilePath));
		}catch (Exception e){
			failed = true;
			e.printStackTrace();
		}
		return failed;
	}
	
	public InputStream getObject(Container container, String remoteName) {
		StoredObject object = container.getObject(remoteName);
        return object.downloadObjectAsInputStream();
	}
	
	public Collection<StoredObject> listContainerObjects(String container) {		
		return getSwiftContainer(container).list();
	}
	
	public void cleanContainer(String container) {
		Collection<StoredObject> objects = getSwiftContainer(container).list();
	    for (StoredObject currentObject : objects) {
	        currentObject.delete();;
	    }
	}

}
