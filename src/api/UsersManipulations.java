package api;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import configurations.ConnectionProperties;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import providers.MongoWorker;
import objects.ChildUser;
import objects.ParentUser;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Roman on 12/25/2014.
 */
public class UsersManipulations {

    //Read properties file before!
    private MongoWorker mongoWorker;

    public UsersManipulations(ConnectionProperties connectionProperties) {
        mongoWorker = MongoWorker.getInstance();
        mongoWorker.createConnectionWithGivenProperties(connectionProperties);
    }

    //Add given ParentUser object to DB:
    public void addParentUser(ParentUser parentUser){

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

    //Add given ChildUser object to DB:
    public void addChildUser(ChildUser childUser){

        BasicDBObject document = new BasicDBObject();

        // Insert user:
        document.put("id", childUser.getID());
        document.put("name", childUser.getName());
        document.put("email", childUser.getEmail());
        document.put("parentIDs", childUser.getParentIDs().toString());

        // insert document to table:
        mongoWorker.getTable().insert(document);
    }

      //Get Parent User by name and Password:
    public ParentUser getParentUserByNameAndPassword(String name, String password){
        ParentUser parentUser = null;
        BasicDBObject query = new BasicDBObject();
        query.put("name", name);
        query.put("password", password);

        // Search user by name:
        DBObject result = mongoWorker.getTable().findOne(query);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
             parentUser = objectMapper.readValue(result.toString(), ParentUser.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // возвращаем полученного пользователя
        return parentUser;
    }


    public void updateChildrenIDsOnParentObject(String parentName, String parentPassword, String childID){
        ParentUser parentUser = null;
        ArrayList<String> tempChildrenIDs;
        BasicDBObject searchQuery = null;
        BasicDBObject queryToFindParent = new BasicDBObject();
        queryToFindParent.put("name", parentName);
        queryToFindParent.put("password", parentPassword);

        DBObject result = mongoWorker.getTable().findOne(queryToFindParent);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            parentUser = objectMapper.readValue(result.toString(), ParentUser.class);
            tempChildrenIDs = parentUser.getChildrenIDs();
            tempChildrenIDs.add(childID);
            searchQuery = new BasicDBObject().append("childrenIDs", tempChildrenIDs);

            //Update Data:
            this.mongoWorker.getTable().update(searchQuery, queryToFindParent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("");
    }

    /*
    public void deleteByLogin(String login){
        BasicDBObject query = new BasicDBObject();

        // указываем какую запись будем удалять с коллекции
        // задав поле и его текущее значение
        query.put("login", login);

        // удаляем запись с коллекции/таблицы
        table.remove(query);
    }*/

}
