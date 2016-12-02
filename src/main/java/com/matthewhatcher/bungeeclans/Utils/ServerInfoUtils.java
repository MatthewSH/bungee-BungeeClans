package com.matthewhatcher.bungeeclans.Utils;

import java.util.HashMap;

public class ServerInfoUtils {
	public static HashMap<String, String> aliases = new HashMap<>();
	
	public static void addAlias(String serverName, String serverAlias) {
		aliases.put(serverName, serverAlias);
	}
	
	public static String getAlias(String serverName) {
		if(aliases.containsKey(serverName))
			return aliases.get(serverName);
		
		return serverName;
	}
}
