package configurations;

/**
 * Created by Roman on 12/25/2014.
 */
public class ConnectionProperties {

    private String host;

    private String port;

    private String tableName;

    private String dbName;

    public String getHost(){return this.host;}

    public String getPort(){return this.port;}

    public String getTableName(){return this.tableName;}

    public String getDbName(){return this.dbName;}

    public void setDbName(String dbName) {this.dbName = dbName;}

    public void setHost(String host) {this.host = host;}

    public void setPort(String port) {this.port = port;}

    public void setTableName(String tableName) {this.tableName = tableName;}
}
