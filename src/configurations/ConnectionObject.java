package configurations;

import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * Created by Roman on 12/25/2014.
 */
public class ConnectionObject {
    //DB it's self
    private DB db;

    //login authorization
    private boolean authenticate;

    //collections/ tables
    private DBCollection table;

    public DB getDB(){return this.db;}

    public void setDB(DB db){this.db = db;}

    public void setAuthenticate(boolean authenticate) {this.authenticate = authenticate;}

    public void setTable(DBCollection table) {this.table = table;}

    public DBCollection getTable() {return table;}

    public boolean isAuthenticate() {return authenticate;}
}
