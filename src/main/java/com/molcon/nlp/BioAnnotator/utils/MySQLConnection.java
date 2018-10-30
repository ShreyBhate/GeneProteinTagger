package com.molcon.nlp.BioAnnotator.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Properties;

import com.mysql.jdbc.*;

@SuppressWarnings("unused")
public class MySQLConnection 
{
	private Connection CONNECTION = null;
	private Statement STATEMENT = null;
	private String HOST, USER, PASSWORD;
		
	public MySQLConnection(String host, String user, String password, String database) {
		this(host, user, password);
		connectDatabase(database);
		}

	public MySQLConnection(String host, String user, String password) {
		try {
			this.HOST = host;
			this.USER = user;
			this.PASSWORD = password;
			connect();
			} catch(Exception sql) {
				System.out.println(sql);
				}
		}
	
	public void connectDatabase(String database) {
		try {
			STATEMENT.executeUpdate("use "+ database);
			} catch (SQLException sql) {
				System.out.println("Error in connecting to database..." + sql);
				}
	}
	
	private void connect() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
//		Class.forName("com.mysql.cj.jdbc.Driver");
		CONNECTION = DriverManager.getConnection(HOST, USER, PASSWORD);
//		CONNECTION = DriverManager.getConnection("jdbc:mysql://192.168.1.27:3306/MC_TAGGER","root", "2k51125_MCPL");
//		CONNECTION = DriverManager.getConnection("jdbc:mysql://192.168.1.27:3306/NlpDB","root", "2k51125_MCPL");
		STATEMENT = CONNECTION.createStatement();
		}
	
	public void closeConnections() {
		try {
			if(STATEMENT!=null)
				STATEMENT.close();
			if(CONNECTION!=null)
				CONNECTION.close();
			} catch (Exception sqx) {
				System.out.println("Error in closing: " + sqx.getMessage());
				}
		}

	public ResultSet readData(String command) {
		try {
			ResultSet rs = STATEMENT.executeQuery(command);
			return rs;
			} catch(SQLException sqx) {
				System.out.println("Error in extracting: "+ sqx.getMessage());
				return null;
				}
		}

	public ResultSet readData(String command, int cursorSize) {
		try {
			CONNECTION.setAutoCommit(false);
			STATEMENT.setFetchSize(cursorSize);
			ResultSet rs = STATEMENT.executeQuery(command);
			return rs;
		} catch(SQLException sqx) {
			System.out.println("Error in extracting: "+ sqx.getMessage());
			return null;
		}
	}

	public void update(String command) {
		try {
			STATEMENT.executeUpdate(command);
			} catch(SQLException sqx) {
				System.out.println("Error in extracting: "+ sqx.getMessage());
				}
		}
	
	public PreparedStatement getPreparedStatementFor(String updateCommand) {
		if(CONNECTION == null) return null;
		try {
			return CONNECTION.prepareStatement(updateCommand);
			} catch(SQLException sql) {
				System.err.println("Error in preparing statement... " + sql.getMessage());
				}
		return null;
		}
}