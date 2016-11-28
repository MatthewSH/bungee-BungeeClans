package com.matthewhatcher.bungeeclans;

import java.io.File;
import java.io.IOException;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BungeeClans extends Plugin {
	private static BungeeClans instance;
	
	public static BungeeClans getInstance() {
		return instance;
	}
	
	public static Configuration getConfig() {
		try {
			return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getInstance().getDataFolder().getPath(), "config.yml"));
		} catch (IOException e) {
			getInstance().getLogger().severe("Could not get the configuration file.");
			return null;
		}
	}
	
	@Override
	public void onEnable() {
		instance = this;
		
		super.onEnable();
	}
}
