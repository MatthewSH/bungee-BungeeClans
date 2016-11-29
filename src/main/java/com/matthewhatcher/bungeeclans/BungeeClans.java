package com.matthewhatcher.bungeeclans;

import com.matthewhatcher.bungeeclans.Utils.ConfigUtils;
import com.matthewhatcher.bungeeclans.Utils.MessageUtils;
import com.matthewhatcher.bungeeclans.Utils.SQLUtils;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

public class BungeeClans extends Plugin {
	private static BungeeClans instance;
	
	public static BungeeClans getInstance() {
		return instance;
	}
	
	public ConfigUtils configUtils;
	public SQLUtils sqlUtils;
	
	@Override
	public void onEnable() {
		instance = this;
		
		configUtils = new ConfigUtils();
		configUtils.firstRun();
		
		Configuration config = configUtils.getConfig();
		
		sqlUtils = new SQLUtils(config.getString("database.uri"), config.getString("database.username"), config.getString("database.password"));
		sqlUtils.firstRun();
		
		MessageUtils.prefix = config.getString("system.chat-prefix");
		
		super.onEnable();
	}
}
