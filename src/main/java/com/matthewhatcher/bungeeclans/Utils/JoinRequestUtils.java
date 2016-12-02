package com.matthewhatcher.bungeeclans.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.matthewhatcher.bungeeclans.BungeeClans;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class JoinRequestUtils {
	public static HashMap<String, ProxiedPlayer> openRequests = new HashMap<>();
	public static HashMap<String, Long> openRequestTimes = new HashMap<>();
	public static Integer requestLifetime = 5, requestDelay = 3;
	
	public static void openRequest(String clanTag, ProxiedPlayer player) {
		if(ClanUtils.isClanOwner(player, clanTag)) {
			if(openRequests.containsKey(clanTag) && (openRequestTimes.get(clanTag).longValue() > Long.valueOf(System.currentTimeMillis())))
				MessageUtils.send(player, "A request is already open and valid.");
			else {
				openRequests.put(clanTag, player);
				openRequestTimes.put(clanTag, Long.valueOf(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(requestLifetime)));
				MessageUtils.send(player, "Join request opened. This invite will expire in " + requestLifetime.toString() + " minute(s).");
				MessageUtils.send(player, "Sending invites to online clan members...");
				sendRequests(clanTag);
			}
		} else {
			MessageUtils.send(player, "You are not the owner of the clan, you can not send join requests.");
		}
	}
	
	public static void sendRequests(String clanTag) {
		ArrayList<ProxiedPlayer> onlineMembers = ClanUtils.getOnlineMembers(clanTag);
		
		for(ProxiedPlayer member : onlineMembers) {
			MessageUtils.send(member, "You've been invited to join your clan (" + clanTag + ") by the owner of the clan.");
			MessageUtils.send(member, "Type /bc jraccept " + clanTag + " to join them!");
			MessageUtils.send(member, "This invite expires in " + JoinRequestUtils.requestLifetime.toString() + " minutes.");
		}
	}
	
	public static void join(String clanTag, final ProxiedPlayer player) {
		if(ClanUtils.isInClan(player, clanTag)) {
			if(openRequests.containsKey(clanTag) && (openRequestTimes.get(clanTag).longValue() > Long.valueOf(System.currentTimeMillis()))) {
				ProxiedPlayer clanOwner = openRequests.get(player);
				final ServerInfo server = clanOwner.getServer().getInfo();
				
				BungeeClans.getInstance().getProxy().getScheduler().schedule(BungeeClans.getInstance(), new Runnable() {
					@Override
					public void run() {
						player.connect(server);
					}
					
				}, requestDelay.longValue(), TimeUnit.SECONDS);
				
				MessageUtils.send(player, "You will be sent to the server " + ServerInfoUtils.getAlias(server.getName()) + " in " + requestDelay.toString() + " seconds.");
			} else {
				MessageUtils.send(player, "The join requested you accepted is no longer valid or was never opened.");
				MessageUtils.send(player, "Please have your owner open another one.");
			}
		} else {
			MessageUtils.send(player, "You are not in the clan, so you can not join them.");
		}
	}
}
