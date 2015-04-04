package restpro.app;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import providers.MongoWorker;
import configurations.ConnectionProperties;

//host:8080/restpro/v1/resource

@ApplicationPath("v1")
public class AppConfig extends Application{

	Set<Class<?>> resources = new HashSet<>();
	
    public AppConfig() {
	      System.out.println("Created AppConfig");
	      resources.add(ApiManager.class);
	      ConnectionProperties properties = new ConnectionProperties();
	        properties.setDbName("USERS");
	        properties.setHost("localhost");
	        properties.setPort("27017");
	        properties.setTableName("parents");

	        MongoWorker mongoWorkerStatic = MongoWorker.getInstance();
	        mongoWorkerStatic.createConnectionWithGivenProperties(properties);
    }
	
	@Override
	public Set<Class<?>> getClasses() {
		return this.resources;
	}

	
	
}
