package api;

import com.mongodb.BasicDBObject;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import providers.MongoWorker;
import objects.ParentUser;

/**
 * Created by Roman on 1/3/2015.
 */
public class AddParent {

    public static String addGivenParent(String parentObjectAsString){
        AddParent addParent;
        try{
            addParent = new AddParent(parentObjectAsString);
            return "OK";
        }catch (Exception e){
            e.printStackTrace();
            return "Failed to add given parent. ";
        }
    }

    public AddParent(String parentObjectAsString){
        ParentUser parentUser;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            parentUser = objectMapper.readValue(parentObjectAsString, ParentUser.class);
            addParentUser(parentUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Add given ParentUser object to DB:
    public void addParentUser(ParentUser parentUser){
        MongoWorker mongoWorker = MongoWorker.getInstance();
        mongoWorker.setTableByName("parents");
        BasicDBObject document = new BasicDBObject();
        // Insert user:
        document.put("id", parentUser.getID());
        document.put("name", parentUser.getName());
        document.put("email", parentUser.getEmail());
        document.put("password", parentUser.getPassword());
        document.put("childrenIDs", parentUser.getChildrenIDs().toString());
        // insert document to table:
        mongoWorker.getTable().insert(document);
    }
}
