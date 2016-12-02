package com.matthewhatcher.bungeeclans.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.matthewhatcher.bungeeclans.BungeeClans;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

public class ClanUtils {
	// Invitee, Clan Prefix
	public static HashMap<UUID, String> outstandingInvites = new HashMap<UUID, String>();
	
	public static void inviteToClan(ProxiedPlayer p, String clanPrefix) {
		outstandingInvites.put(p.getUniqueId(), clanPrefix);
		MessageUtils.send(p, "You have been invited to " + clanPrefix + ". ");
		MessageUtils.send(p, "To accept the invite: /bc accept " + clanPrefix);
		MessageUtils.send(p, "To reject the invite: /bc reject " + clanPrefix);
	}
	
	public static void acceptInviteToClan(ProxiedPlayer p, String clanPrefix) {
		int clanId = 0;
		
		try {
			ResultSet clan = BungeeClans.getInstance().sqlUtils.runSelectSQL("SELECT `id` FROM `clans` WHERE `tag`='" + clanPrefix + "'");
			if(clan.first()) {
				if(!clan.next()) {
					clan.first();
					clanId = clan.getInt("id");
				} else {
					BungeeClans.getInstance().getLogger().severe("Two or more clans with the tag " + clanPrefix + " were found.");
				}
			} else {
				MessageUtils.send(p, "Either no clans with the tag " + clanPrefix + " exist, or you do not own the clan.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(clanId > 0) {
			if(!isClanMaxedOut(clanPrefix)) {
				java.sql.Date dateNow = (java.sql.Date) new Date(Calendar.getInstance().getTimeInMillis());
				BungeeClans.getInstance().sqlUtils.runSQL("INSERT INTO `memberships` ( `clan-id`, `created_at`, `type`, `uuid`) VALUES (" + clanId + " , '" + dateNow + "', 'member', '" + p.getUniqueId().toString().replace("-", "") + "' )");
				outstandingInvites.remove(p.getUniqueId(), clanPrefix);
				MessageUtils.send(p, "You have accepted the invite to the clan.");
			} else {
				MessageUtils.send(p, "The clan has maxed out the amount of members it can have.");
			}
		} else {
			MessageUtils.send(p, "Something went wrong when accepting your invite. Try again?");
		}
	}
	
	public static void rejectInviteToClan(ProxiedPlayer p, String clanPrefix) {
		outstandingInvites.remove(p.getUniqueId(), clanPrefix);
		MessageUtils.send(p, "You have rejected the invite to the clan.");
	}
	
	public static boolean isInvited(ProxiedPlayer p, String clanPrefix) {
		boolean there = false;
		UUID pId = p.getUniqueId();
		
		for(Map.Entry<UUID, String> entry : outstandingInvites.entrySet()) {
			if((entry.getKey() == pId) && (entry.getValue() == clanPrefix)) {
				there = true;
				break;
			}
		}
		
		return there;
	}
	
	public static boolean isInClan(ProxiedPlayer p, String clanTag) {
		boolean isIn = false;
		
		return isIn;
	}
	
	public static boolean isClanOwner(ProxiedPlayer p, String clanTag) {
		boolean isOwner = false;
		
		return isOwner;
	}
	
	public static ArrayList<ProxiedPlayer> getOnlineMembers(String clanTag) {
		ArrayList<ProxiedPlayer> members = new ArrayList<>();
		try {
			int clanId = getClanInfo(clanTag).getInt("id");
			ResultSet rs = BungeeClans.getInstance().sqlUtils.runSelectSQL("SELECT `uuid` FROM `bc_memberships` WHERE `clan-id`=" + clanId);
			UUID currentUUID = null;
			ProxiedPlayer currentPlayer = null;
			
			while(rs.next()) {
				currentUUID = UUID.fromString(rs.getString("uuid"));
				currentPlayer = BungeeClans.getInstance().getProxy().getPlayer(currentUUID);
				
				if(currentPlayer != null) {
					members.add(currentPlayer);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return members;
	}
	
	public static int getClanCount(String clanTag) {
		int clanId;
		try {
			clanId = getClanInfo(clanTag).getInt("id");
			ResultSet rs = BungeeClans.getInstance().sqlUtils.runSelectSQL("SELECT `uuid` FROM `bc_memberships` WHERE `clan-id`=" + clanId);
			return rs.getFetchSize();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static ResultSet getClanInfo(String clanTag) {
		return BungeeClans.getInstance().sqlUtils.runSelectSQL("SELECT * FROM `bc_clans` WHERE `tag`='" + clanTag + "'");
	}
	
	/*public static ArrayList<String> parseUUIDs(ArrayList<String> uuids) {
		ArrayList<String> names = new ArrayList<>();
		ResultSet uuidCache = BungeeClans.getInstance().sqlUtils.runSelectSQL("SELECT * FROM `bc_uuid_cache");
		
		for(String uuid : uuids) {
		}
		
		return names;
	}*/
	
	public static boolean deleteClan(String clanTag) {
		int clanId;
		try {
			clanId = getClanInfo(clanTag).getInt("id");
			BungeeClans.getInstance().sqlUtils.runSQL("DELETE FROM `bc_clans` WHERE `clan-id`=" + clanId);
			BungeeClans.getInstance().sqlUtils.runSQL("DELETE FROM `bc_memberships` WHERE `clan-id`=" + clanId);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean clanExists(String clanTag) {
		boolean exists = false;
				
		try {
			ResultSet rs = BungeeClans.getInstance().sqlUtils.runSelectSQL("SELECT * FROM `bc_clans` WHERE `tag` = '" + clanTag + "'");
			if(rs.first())
				exists = true;
			else
				exists = false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return exists;
	}
	
	public static int clansOwned(ProxiedPlayer player) {
		int amount = 0;
		
		return amount;
	}
	
	public static void createClan(ProxiedPlayer owner, String clanTag) {
		Configuration config = BungeeClans.getInstance().configUtils.getConfig();
		String defaultName = config.getString("clan-defaults.name"), 
				defaultMotto = config.getString("clan-defaults.motto"), 
				defaultDescription = config.getString("clan-defaults.description");
		int defaultMaxMembers = config.getInt("clan-defaults.max-members");
		
		BungeeClans.getInstance().sqlUtils.runSQL("");
	}
	
	public static boolean isClanMaxedOut(String clanTag) {
		// return if clan can accept more players
		
		return false;
	}
	
	// Create clan
	// edit field in clan
	// get clan members
	// kick clan member
	// add clan member
	// get player info
}
