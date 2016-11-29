package com.matthewhatcher.bungeeclans.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ClanChatUtils {
	public static HashMap<String, ArrayList<ProxiedPlayer>> activeClanChats = new HashMap<String, ArrayList<ProxiedPlayer>>();
	
	public static boolean isInChat(ProxiedPlayer p, String clanTag) {
		return activeClanChats.get(clanTag).contains(p);
	}
	
	public static boolean removeFromChat(ProxiedPlayer p, String clanTag) {
		return activeClanChats.get(clanTag).remove(p);
	}
	
	public static boolean addToChat(ProxiedPlayer p, String clanTag) {
		return activeClanChats.get(clanTag).add(p);
	}
	
	public static void sendToTeam(String clanTag, String message) {
		ArrayList<ProxiedPlayer> team = activeClanChats.get(clanTag);
		TextComponent msg = new TextComponent(ChatColor.AQUA + "[CC] " + ChatColor.WHITE + message);
		
		for(ProxiedPlayer p : team) {
			p.sendMessage(msg);
		}
	}
}
