package com.matthewhatcher.bungeeclans.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.matthewhatcher.bungeeclans.BungeeClans;

import net.md_5.bungee.api.connection.ProxiedPlayer;

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
			java.sql.Date dateNow = (java.sql.Date) new Date(Calendar.getInstance().getTimeInMillis());
			BungeeClans.getInstance().sqlUtils.runSQL("INSERT INTO `memberships` ( `clan-id`, `created_at`, `type`, `uuid`) VALUES (" + clanId + " , '" + dateNow + "', 'member', '" + p.getUniqueId().toString().replace("-", "") + "' )");
			outstandingInvites.remove(p.getUniqueId(), clanPrefix);
			MessageUtils.send(p, "You have accepted the invite to the clan.");
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
	
	// Get Clan info
	// Get clan count for member
	// Create clan
	// edit field in clan
	// get clan members
	// kick clan member
	// get player info
	// delete clan
}
