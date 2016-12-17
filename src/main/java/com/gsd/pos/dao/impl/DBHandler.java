package com.gsd.pos.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.gsd.pos.utils.Config;

public class DBHandler {
	private Connection conn = null;
	private static final DBHandler dbh = new DBHandler();
	private static final Logger logger = Logger.getLogger(DBHandler.class
			.getName());

	private DBHandler() {
	}

	public static DBHandler getInstance() {
		return dbh;
	}

	public Connection getConnection() throws SQLException {
		if ((conn == null) || (conn.isClosed())) {
			initConnection();
		}
		return conn;
	}

	private void initConnection() throws SQLException {
		// Load the JDBC driver
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; 
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException cnfe) {
			throw new SQLException(cnfe.getMessage());
		}
		// Create a connection to the database
		String serverName = Config.getProperty("servername");
		String mydatabase = Config.getProperty("databasename");
		String username = Config.getProperty("username");
		String password = Config.getProperty("password");
		String url = "jdbc:sqlserver://"+ serverName+ ";databaseName="+ mydatabase	+ ";user=" + username + ";password=" + password + ";"; 
/**		
		String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
				   "databaseName=AdventureWorks;user=MyUserName;password=*****;";
				  http://msdn.microsoft.com/en-us/library/ms378526.aspx
				   *
				   */

		logger.trace("Database URL  >>" + url);
		conn = DriverManager.getConnection(url);
	}

	public Vector<String> getItems(String tableName, String columnName) {
		Vector<String> items = new Vector<String>();
		items.add("");
		try {
			String sql = "select distinct(" + columnName + ") from "
					+ tableName;
			Connection con = getConnection();
			PreparedStatement st = con.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				items.add(rs.getString(1));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return items;
	}
}
