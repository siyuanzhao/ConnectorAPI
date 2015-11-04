package org.assistments.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.assistments.connector.utility.Constants;
import org.assistments.connector.utility.LocalhostSettings;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ConnectionFactory {

	private static String url;
	private static String dbuser;
	private static String dbpassword;
	private static String driverClassName = "org.postgresql.Driver";
	
	//private Properties  prop = new Properties();
	//private InputStream input = null;
	
	private static ConnectionFactory connectionFactory = null;
	
	private static DataSource ds = null;
	
	public static void LoadProperties() {
		url = Constants.DATABASE_URL;
		dbpassword = Constants.DATABASE_PASSWORD;
		dbuser = "postgres";
		
	}
	
    private ConnectionFactory() {
        try {
                Class.forName(driverClassName);
                LoadProperties();
                
        } catch (ClassNotFoundException e) {
                e.printStackTrace();
        }
    }
	
    public Connection getConnection() throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection(url, dbuser, dbpassword);
        return conn;
    }
    
    public static ConnectionFactory getInstance() {
        if (connectionFactory == null) {
            connectionFactory = new ConnectionFactory();
        }
        return connectionFactory;
    }
    
    public static Connection getDBConnection() throws SQLException {
    	return getInstance().getConnection();
    }
    
    public static DataSource getDataSource() {
    	if(ds == null) {
    		ApplicationContext ac = new ClassPathXmlApplicationContext("connector.xml");
    		ds = (DataSource) ac.getBean("dataSource");
    	}
    	return ds;
    }
    
}
