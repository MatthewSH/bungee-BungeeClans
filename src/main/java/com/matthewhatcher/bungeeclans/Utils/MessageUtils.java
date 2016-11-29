package com.matthewhatcher.bungeeclans.Utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class MessageUtils {
	public static String prefix = "&l&b[BungeeClans]&r ";
	
	public static void send(CommandSender to, String message) {
		String msg = prefix + message;
		to.sendMessage(new TextComponent(msg.replace("&", "§")));
	}
}
