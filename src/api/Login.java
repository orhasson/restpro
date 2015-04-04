package api;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import objects.ChildUser;
import objects.LoginObject;
import objects.LoginResultObject;
import objects.ParentUser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import providers.MongoWorker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Roman on 1/3/2015.
 */
public class Login {

    public static String login(String loginObjectAsString){
        Login login = new Login();
        return login.checkAuthorization(loginObjectAsString);
    }

    private String checkAuthorization(String loginObjectAsString){
        ParentUser parentUser;
        DBObject result = null;
        LoginObject loginObject;
        Map<String,String> childrenNamesAndIDs;
        ObjectMapper objectMapper = new ObjectMapper();
        MongoWorker mongoWorker = MongoWorker.getInstance();
        BasicDBObject queryToFindParent = new BasicDBObject();
        LoginResultObject loginResultObject = new LoginResultObject();
        mongoWorker.setTableByName("parents");
        try {
            loginObject = objectMapper.readValue(loginObjectAsString, LoginObject.class);
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            queryToFindParent.put("name", loginObject.getUserName());
            queryToFindParent.put("password", loginObject.getPassword());
            result = mongoWorker.getTable().findOne(queryToFindParent);
            if (result != null) {
                parentUser = objectMapper.readValue(result.toString(), ParentUser.class);
                childrenNamesAndIDs = getChildrenNames(parentUser);
                loginResultObject.setNamesAndIDs(childrenNamesAndIDs);
                loginResultObject.setResult("OK");
                return objectMapper.writeValueAsString(loginResultObject);
            }
            else{
                loginResultObject.setResult("NOT");
                loginResultObject.setChildrenNames(null);
                return objectMapper.writeValueAsString(loginResultObject);
            }
        }catch (Exception e){
        	e.printStackTrace();
            return "MAIN ERROR";
        }
    }

    private static Map<String,String> getChildrenNames(ParentUser parentUser){
        ChildUser tempChild = null;
        DBObject result = null;
        ObjectMapper objectMapper = new ObjectMapper();
        BasicDBObject queryToFindChild = new BasicDBObject();
        MongoWorker mongoWorker = MongoWorker.getInstance();
        mongoWorker.setTableByName("children");
        Map<String,String> childrenNamesAndIDs = new HashMap<>();
        ArrayList<String> childrenID = parentUser.getChildrenIDs();
        for(String id : childrenID) {
            if(id.equals("000")) continue;
            try {
                queryToFindChild.put("id", id);
                result = mongoWorker.getTable().findOne(queryToFindChild);
                objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                tempChild =  objectMapper.readValue(result.toString(),ChildUser.class);
                childrenNamesAndIDs.put(tempChild.getName(), id);
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
        return childrenNamesAndIDs;
    }

    //Check authorization of given user:
   /* private String checkAuthorization(String loginObjectAsString){
        ParentUser parentUser;
        DBObject result = null;
        LoginObject loginObject;
        ArrayList<String> childrenNames;
        ObjectMapper objectMapper = new ObjectMapper();
        MongoWorker mongoWorker = MongoWorker.getInstance();
        BasicDBObject queryToFindParent = new BasicDBObject();
        LoginResultObject loginResultObject = new LoginResultObject();
        mongoWorker.setTableByName("parents");
        try {
            loginObject = objectMapper.readValue(loginObjectAsString, LoginObject.class);
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            queryToFindParent.put("name", loginObject.getUserName());
            queryToFindParent.put("password", loginObject.getPassword());
            result = mongoWorker.getTable().findOne(queryToFindParent);
            if (result != null) {
                parentUser = objectMapper.readValue(result.toString(), ParentUser.class);
                childrenNames = getChildrenNames(parentUser);
                loginResultObject.setChildrenNames(childrenNames);
                loginResultObject.setResult("OK");
                return objectMapper.writeValueAsString(loginResultObject);
            }
            else{
                loginResultObject.setResult("NOT");
                loginResultObject.setChildrenNames(null);
                return objectMapper.writeValueAsString(loginResultObject);
            }
        }catch (Exception e){
            return "MAIN ERROR";
        }
    }

    private static ArrayList<String> getChildrenNames(ParentUser parentUser){
        ChildUser tempChild;
        DBObject result = null;
        ObjectMapper objectMapper = new ObjectMapper();
        BasicDBObject queryToFindChild = new BasicDBObject();
        MongoWorker mongoWorker = MongoWorker.getInstance();
        mongoWorker.setTableByName("children");
        ArrayList<String> childrenNames = new ArrayList<>();
        ArrayList<String> childrenID = parentUser.getChildrenIDs();
        for(String id : childrenID) {
            if(id.equals("000")) continue;
            try {
                queryToFindChild.put("id", id);
                result = mongoWorker.getTable().findOne(queryToFindChild);
                objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                tempChild =  objectMapper.readValue(result.toString(),ChildUser.class);
                childrenNames.add(tempChild.getName());
            } catch (Exception e) {
                 // e.printStackTrace();
            }
        }
        return childrenNames;
    }*/
}
