package com.matthewhatcher.bungeeclans.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.common.io.ByteStreams;
import com.matthewhatcher.bungeeclans.BungeeClans;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ConfigUtils {
	public Configuration getConfig() {
		try {
			return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(BungeeClans.getInstance().getDataFolder().getPath(), "config.yml"));
		} catch (IOException e) {
			BungeeClans.getInstance().getLogger().severe("Could not get the configuration file.");
			return null;
		}
	}
	
	public void firstRun() {
		BungeeClans pl = BungeeClans.getInstance();
		try {
			if(!pl.getDataFolder().exists())
				pl.getDataFolder().mkdir();
			
			File file = new File(pl.getDataFolder().getPath(), "config.yml");
			
			if(!file.exists()) {
				try {
					file.createNewFile();
					try(InputStream in = pl.getResourceAsStream("config.yml"); OutputStream out = new FileOutputStream(file)) {
						ByteStreams.copy(in, out);
					}
							
				} catch (IOException e) {
					throw new RuntimeException("Unable to create configuration file", e);
				}
			}
			
		} catch (Exception e) {
			pl.getLogger().severe("Configuration data failed to save.");
			e.printStackTrace();
		}
	}
}
