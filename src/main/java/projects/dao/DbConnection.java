package projects.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import projects.exception.DbException;

public class DbConnection {

	private static final String SCHEMA = "projects";
	private static final String USER = "projects";
	private static final String PASSWORD = "projects";
	private static final String HOST = "localhost"; // running on my computer
	private static final int PORT = 3306;

	// create a method that makes a connection for us every time we call it
	public static Connection getConnection() {
		String url = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s&useSSL=false", HOST, PORT, SCHEMA, USER,
				PASSWORD); // %s means string, %d is port, %s is schema name
		System.out.println("Connecting with url=" +  url);

		//Connection conn = DriverManager.getConnection(url); red sqigglies: try/catch:
		try {
			Connection conn = DriverManager.getConnection(url);
			System.out.println("Successfully obtained connection!");
			return conn;
		} catch (SQLException e) {
			System.out.println("Error getting connection.");
			throw new DbException(e);
		}
	}

}
