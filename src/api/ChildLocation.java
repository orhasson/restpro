package api;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import objects.ChildUser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import providers.MongoWorker;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Roman on 2/7/2015.
 */
public class ChildLocation {

    public static String getChildLocation(String childID){
        String locationResult = "NOT";
        ObjectMapper objectMapper = new ObjectMapper();
        ChildUser childUser;

        MongoWorker mongoWorker = MongoWorker.getInstance();
        mongoWorker.setTableByName("children");
        BasicDBObject queryToFindParent = new BasicDBObject();
        queryToFindParent.put("id", childID);

        DBObject result = mongoWorker.getTable().findOne(queryToFindParent);
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            childUser = objectMapper.readValue(result.toString(), ChildUser.class);
            locationResult = objectMapper.writeValueAsString(childUser.getLocationObject());
        }catch (Exception e){}
        return locationResult;
    }
}
