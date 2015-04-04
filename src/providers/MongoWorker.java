package providers;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import configurations.ConnectionProperties;

import java.net.UnknownHostException;

public class MongoWorker {

    private static MongoWorker instance = null;

    private ConnectionProperties connectionProperties;

    private MongoClient mongoClient;

    private DB db;

    private boolean authenticate;

    private DBCollection table;

    public static synchronized MongoWorker getInstance(){
        if(instance == null) {
           instance = new MongoWorker();
        }
        return instance;
    }

    public void createMongoClientConnection(){
        try {
            mongoClient = new MongoClient(connectionProperties.getHost(),Integer.parseInt(connectionProperties.getPort()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void connectToDB(){
        db = mongoClient.getDB(connectionProperties.getDbName());
    }

    public void connectToTable(){
        table = db.getCollection(connectionProperties.getTableName());
    }

    private MongoWorker(){}

    public void createConnectionWithGivenProperties(ConnectionProperties connectionProperties) {
        this.connectionProperties = connectionProperties;
        createMongoClientConnection();
        connectToDB();
        connectToTable();
    }

    public DB getDb() {return db;}

    public void setDb(DB db) {this.db = db;}

    public void setTable(DBCollection table) {this.table = table;}

    public void setTableByName(String tableName) {this.table = db.getCollection(tableName);}

    public DBCollection getTable() {return table;}


}
