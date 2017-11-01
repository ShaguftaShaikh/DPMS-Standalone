package com.dpms.dairyproducts.configurations;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dpms.dairyproducts.utils.DPMSConstants;

/**
 * @author Shagufta
 * This class will be responsible for connecting to database
 *
 */
public class ConnectToDB {

	//static Logger LOG = LoggerFactory.getLogger(ConnectToDB.class);

	/**
	 * Load postgres drivers
	 */
	public void LoadDrivers() {
		//LOG.info("Loading database drivers");
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			//LOG.error("Databse Drivers not Found");
			e.printStackTrace();
		}
	}

	/**
	 * This method will load properties file and fetch necessary 
	 * details to make connection to database
	 * @return connection
	 */
	public Connection connect(String username,String password){
		Connection connection = null;
		try {
			FileReader fileReader = new FileReader(DPMSConstants.FILE_PATH);
			Properties properties = new Properties();
			properties.load(fileReader);
			
			connection = DriverManager.getConnection(
						 properties.getProperty(DPMSConstants.DB_URL),username,password);
						 //properties.getProperty(DPMSConstants.DB_USERNAME),
						// properties.getProperty(DPMSConstants.DB_PASSWORD));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//LOG.error("Error connecting database");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//LOG.error("Error loadng properties file");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//LOG.error("Error reading properties file");
			e.printStackTrace();
		}
		if(connection!=null)
			return connection;
		
		//LOG.error("Failed to connect!");
		return null;
	}
}
