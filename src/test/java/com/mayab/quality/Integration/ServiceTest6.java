package com.mayab.quality.Integration;

import java.io.File;

import java.io.FileInputStream;
import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mayab.quality.integrationtest.dao.IDAOUser;
import com.mayab.quality.integrationtest.dao.UserMysqlDAO;
import com.mayab.quality.integrationtest.model.User;
import com.mayab.quality.integrationtest.service.UserService;

class ServiceTest6 extends DBTestCase {
	
	private IDAOUser dao;
	private UserService service;
	
	public ServiceTest6() {
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,"com.mysql.cj.jdbc.Driver");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,"jdbc:mysql://localhost:3307/calidad");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,"root");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,"123456");	
	}
	
	@BeforeEach
	void setup() throws Exception {
		dao = new UserMysqlDAO();
		service = new UserService(dao);
		IDatabaseConnection connection = getConnection(); 
		if (connection == null) {
	        fail("Failed to establish a connection to the database.");
	    } else {
	        System.out.println("Connection established successfully.");
	    }
		
		try {
			DatabaseOperation.TRUNCATE_TABLE.execute(connection,getDataSet());
			DatabaseOperation.CLEAN_INSERT.execute(connection, getDataSet());
			
		} catch(Exception e) {
			fail("Error in setup: "+ e.getMessage()); 
		} finally {
			connection.close(); 
		}
	}
	
	protected IDataSet getDataSet() throws Exception
    {
        return new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/initDB.xml"));
    }

	
	@Test
	public void findByEmail() {
	    service.createUser("Juan", "correo1@correo.com", "789456123");
	    service.createUser("Gerardo", "correo2@correo.com", "789456123");
	    service.createUser("Rodrigo", "correo3@correo.com", "789456123");

	    try {
	        User busqueda = service.findUserByEmail("correo2@correo.com");
	        assertEquals("Gerardo", busqueda.getName());

	        IDatabaseConnection conn = getConnection();
	        conn.getConfig().setProperty(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
	        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File("src/resources/byemail.xml"));
	        ITable expectedTable = expectedDataSet.getTable("usuarios");

	        String expectedName = (String) expectedTable.getValue(0, "name");
	        String expectedEmail = (String) expectedTable.getValue(0, "email");
	        String expectedPassword = (String) expectedTable.getValue(0, "password");

	        assertEquals(expectedName, busqueda.getName());
	        assertEquals(expectedEmail, busqueda.getEmail());
	        assertEquals(expectedPassword, busqueda.getPassword());

	    } catch (Exception e) {
	        fail("Error in findByEmail test: " + e.getMessage());
	    }
	}

}