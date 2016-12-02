package com.matthewhatcher.bungeeclans.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;

import com.matthewhatcher.bungeeclans.BungeeClans;

public class SQLUtils {
	private String uri, username, password;
	private Connection connection;
	
	public HashMap<String, String> uuidCache, memberships, clans;
	
	public SQLUtils(String uri, String username, String password) {
		this.uri = uri;
		this.username = username;
		this.password = password;
		
		uuidCache = new HashMap<String, String>();
		memberships = new HashMap<String, String>();
		clans = new HashMap<String, String>();
		
		addStatements();
		
		try {
			this.connection = DriverManager.getConnection("jdbc:mysql://" + this.uri, this.username, this.password);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void addStatements() {
		uuidCache.put("insert", "INSERT INTO `uuid_cache` ( `name`, `uuid`) VALUES ( '%name%', '%uuid%' );");
		uuidCache.put("update", "UPDATE `uuid_cache` SET `name` = '%name%' WHERE `uuid` = '%uuid%';");
		uuidCache.put("selectByUUID", "SELECT `name`, `uuid` FROM `uuid_cache` WHERE `uuid` = '%uuid%';");
		uuidCache.put("selectByName", "SELECT `name`, `uuid` FROM `uuid_cache` WHERE `name` = '%name%';");
		
		memberships.put("delete", "DELETE FROM `memberships` WHERE `uuid` = '%uuid%' AND `clan-id` = '%clanId%';");
		
	}
	
	public boolean isDriverLoaded() {
		boolean loaded = false;
		Enumeration<Driver> e = DriverManager.getDrivers();
		
		while(e.hasMoreElements()) {
			String name = e.nextElement().getClass().getName();
			if(!name.equalsIgnoreCase("com.mysql.jbdc.Driver"))
				continue;
			loaded = true;
		}
		
		return loaded;
	}
	
	public Connection getConnection() throws SQLException {
		return this.connection;
	}
	
	public void firstRun() {
		try {
			ScriptRunner r = new ScriptRunner(getConnection(), false, false);
			r.runScript(new BufferedReader(new InputStreamReader(BungeeClans.getInstance().getResourceAsStream("database.sql"), "UTF-8")));
		} catch (SQLException | IOException e) {
			throw new RuntimeException("Could not create necessary tables", e);
		}
	}
	
	public void setupDatabase() {
		try {
			if(!isDriverLoaded()) {
				Class.forName("com.mysql.jbdc.Driver").newInstance();
			}
		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
			throw new RuntimeException("Could not setup instance.", e);
        }
	}
	
	public boolean runSQL(String sqlString) {
		try {
			PreparedStatement sql = getConnection().prepareStatement(sqlString);
			sql.executeUpdate();
			sql.close();
			
			return true;
		} catch (SQLException e) {
            e.printStackTrace();
            
            return false;
        }
	}
	
	public boolean runUpdateSQL(String sqlString) {
		try {
			PreparedStatement sql = getConnection().prepareStatement(sqlString);
			sql.executeUpdate();
			sql.close();
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            
            return false;
        }
	}
	
	public ResultSet runSelectSQL(String sqlString) {
		PreparedStatement sql;
		try {
			sql = getConnection().prepareStatement(sqlString);
			ResultSet rs = sql.executeQuery();
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		

		
	}
}
