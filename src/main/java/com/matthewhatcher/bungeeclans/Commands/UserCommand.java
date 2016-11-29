package com.matthewhatcher.bungeeclans.Commands;

import com.matthewhatcher.bungeeclans.Utils.MessageUtils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class UserCommand extends Command {
	/*
	 *  bungeeclans || bc || bclans
	 *  
	 *  create <id>
	 *  accept <id>
	 *  reject <id>
	 *  leave <id>
	 *  tc || teamchat <on/off>
	 *  jraccept <id>
	 *  jrdeny <id>
	 *  list || directory
	 *  info <clan>
	 *  owner <clan>
	 *  help
	 */
	
	public UserCommand() {
		super("bc", "bungeeclans.commands.user", new String[] { "bungeeclans", "bclans" });
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length < 1) {
			MessageUtils.send(sender, "Too little arguments. Run /bc help for help.");
			return;
		}
		
		String command = args[0];
		
		if(command.equalsIgnoreCase("list") || command.equalsIgnoreCase("directory")) {
			// List all clans if webpanel is not enabled
			// if webpanel is enabled, grab first 5 links and then
			// send link to directory
		} else if (command.equalsIgnoreCase("info")) {
			
		} else if (command.equalsIgnoreCase("owner")) {
			
		} else if (command.equalsIgnoreCase("jraccept")) {
			
		} else if (command.equalsIgnoreCase("jrdeny")) {
			
		} else if (command.equalsIgnoreCase("create")) {
			
		} else if (command.equalsIgnoreCase("accept")) {
			
		} else if (command.equalsIgnoreCase("reject")) {
			
		} else if (command.equalsIgnoreCase("leave")) {
			
		} else if (command.equalsIgnoreCase("tc") || command.equalsIgnoreCase("teamchat")) {
			
		} else if (command.equalsIgnoreCase("help")) {
			
		} else {
			MessageUtils.send(sender, "Command not recognized, run /bc help for commands.");
		}
	}
}
