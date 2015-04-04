package api;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import providers.MongoWorker;
import objects.ChildUser;
import objects.ParentUser;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Roman on 1/3/2015.
 */
public class AddChild {
    public static String addGivenChild(String childObjectAsString){
        AddChild addChild;
        try{
            addChild = new AddChild(childObjectAsString);
            return "OK";
        }catch (Exception e){
            e.printStackTrace();
            return "Failed to add given child. ";
        }
    }

    public AddChild(String childObjectAsString){
        ChildUser childUser;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            childUser = objectMapper.readValue(childObjectAsString, ChildUser.class);
            addChildUser(childUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Add given ChildUser object to DB:
    public void addChildUser(ChildUser childUser){
        MongoWorker mongoWorker = MongoWorker.getInstance();
        ObjectMapper mapper = new ObjectMapper();
        mongoWorker.setTableByName("parents");
        String parentID = "";
        try {
            parentID = updateChildrenIDsOnParentObject(mongoWorker, childUser.getParentUserName(), childUser.getParentPassword(), childUser.getID());
            ArrayList<String> tempParentIDs = childUser.getParentIDs();
            tempParentIDs.add(parentID);
            mongoWorker.setTableByName("children");
            BasicDBObject document = new BasicDBObject();
            // Insert user:
            document.put("id", childUser.getID());
            document.put("name", childUser.getName());
            document.put("email", childUser.getEmail());
            document.put("parentIDs", tempParentIDs.toString());
            document.put("locationObject", childUser.getLocationObject());
            // insert document to table:
            mongoWorker.getTable().insert(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
       /*
        *  Get parent by username and password
        *  Update children IDs of parent object
        *  Add parent ID to ParentsIDs of child object
        */
    }

    public String updateChildrenIDsOnParentObject(MongoWorker mongoWorker, String parentName, String parentPassword, String childID) throws IOException {
        ParentUser parentUser = null;
        String parentID = "";
        BasicDBObject searchQuery = new BasicDBObject();;
        ArrayList<String> tempChildrenIDs;
        BasicDBObject queryToFindParent = new BasicDBObject();
        queryToFindParent.put("name", parentName);
        queryToFindParent.put("password", parentPassword);

        DBObject result = mongoWorker.getTable().findOne(queryToFindParent);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            parentUser = objectMapper.readValue(result.toString(), ParentUser.class);
            parentID = parentUser.getID();
            tempChildrenIDs = parentUser.getChildrenIDs();
            tempChildrenIDs.add(childID);
            searchQuery.append("$set", new BasicDBObject().append("childrenIDs", tempChildrenIDs));

            /*BasicDBObject newDocument = new BasicDBObject();
            newDocument.append("$set", new BasicDBObject().append("clients", 110));

            BasicDBObject searchQuery = new BasicDBObject().append("hosting", "hostB");

            collection.update(searchQuery, newDocument);*/

           // searchQuery = new BasicDBObject().append("childrenIDs", tempChildrenIDs.toString());

            //Update Data:
            mongoWorker.getTable().update(queryToFindParent, searchQuery);
        } catch (IOException e) {
            throw e;
        }
        return parentID;
    }
}