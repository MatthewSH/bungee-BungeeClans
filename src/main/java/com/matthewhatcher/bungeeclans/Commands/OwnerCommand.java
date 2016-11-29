package com.matthewhatcher.bungeeclans.Commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.matthewhatcher.bungeeclans.BungeeClans;
import com.matthewhatcher.bungeeclans.Utils.MessageUtils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class OwnerCommand extends Command {
	/*
	 *  
	 *  transfer <clan tag> <player>
	 *  invite <player>
	 *  kick <player>
	 *  joinrequest || jr
	 *  delete
	 *  edit <field> <entry>
	 */
	
	public OwnerCommand() {
		super("bco", "bungeeclans.commands.owner", new String[] { "bungeeclansowner", "bcowner" });
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length < 1) {
			MessageUtils.send(sender, "Too little arguments. Run /bc help for help.");
			return;
		}
		
		String command = args[0];
		ProxiedPlayer player = (ProxiedPlayer)sender;
		
		if(command.equalsIgnoreCase("transfer")) {
			String clanTag = args[1];
			String currentOwnerUUID = player.getUniqueId().toString().replace("-", "");
			String newOwnerUUID = null;
			String newOwnerUsername = args[2];
			int clanId = 0;
			
			if(BungeeClans.getInstance().getProxy().getPlayer(newOwnerUsername).isConnected()) {
				newOwnerUUID = BungeeClans.getInstance().getProxy().getPlayer(newOwnerUsername).getUniqueId().toString().replace("-", "");
			} else {
				try {
					URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + newOwnerUsername);
					HttpURLConnection request = (HttpURLConnection) url.openConnection();
					request.connect();
					JsonParser jp = new JsonParser();
					JsonObject obj = jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
					newOwnerUUID = obj.get("id").getAsString();
				} catch (IOException e) {
					throw new RuntimeException("Could not get the UUID for " + newOwnerUsername, e);
				}
			}
			
			try {
				ResultSet clan = BungeeClans.getInstance().sqlUtils.runSelectSQL("SELECT `id` FROM `clans` WHERE `owner`='" + currentOwnerUUID + "' AND `tag`='" + clanTag + "'");
				if(clan.first()) {
					if(!clan.next()) {
						clan.first();
						clanId = clan.getInt("id");
					} else {
						BungeeClans.getInstance().getLogger().severe("Two or more clans with the tag " + clanTag + " were found.");
						MessageUtils.send(sender, "Something went wrong when transfering ownership.");
					}
				} else {
					MessageUtils.send(sender, "Either no clans with the tag " + clanTag + " exist, or you do not own the clan.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			if(clanId > 0) {
				if(BungeeClans.getInstance().sqlUtils.runUpdateSQL("UPDATE `clans` SET `owner` = '" + newOwnerUUID + "' WHERE `id`=" + clanId)) {
					java.sql.Date dateNow = (java.sql.Date) new Date(Calendar.getInstance().getTimeInMillis());
					
					BungeeClans.getInstance().sqlUtils.runSQL("DELETE FROM `memberships` WHERE `uuid`='" + currentOwnerUUID + "' AND `clan-id`=" + clanId);
					BungeeClans.getInstance().sqlUtils.runSQL("INSERT INTO `memberships` ( `clan-id`, `created_at`, `type`, `uuid`) VALUES (" + clanId + " , '" + dateNow + "', 'owner', '" + newOwnerUUID + "' )");
					
					MessageUtils.send(sender, "The clan has been transfered to " + newOwnerUsername + ".");
				} else {
					MessageUtils.send(sender, "Hm, we couldn't transfer the clan to " + newOwnerUsername + " for some reason.");
				}
			}
		} else if (command.equalsIgnoreCase("invite")) {
			
		} else if (command.equalsIgnoreCase("kick")) {
			
		} else if (command.equalsIgnoreCase("delete")) {
			
		} else if (command.equalsIgnoreCase("edit")) {
			
		} else if (command.equalsIgnoreCase("joinrequest") || command.equalsIgnoreCase("jr")) {
			
		} else {
			MessageUtils.send(sender, "Command not recognized, run /bco help for commands.");
		}
	}
}
