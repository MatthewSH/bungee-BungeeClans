package com.matthewhatcher.bungeeclans.Commands;

import com.matthewhatcher.bungeeclans.Utils.ClanChatUtils;
import com.matthewhatcher.bungeeclans.Utils.ClanUtils;
import com.matthewhatcher.bungeeclans.Utils.MessageUtils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UserCommand extends Command {
	/*
	 *  bungeeclans || bc || bclans
	 *  
	 *  create <id>
	 *  accept <id>
	 *  reject <id>
	 *  leave <id>
	 *  clanchat <on/off> <id>
	 *  cc <message> Send a message to team
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
		ProxiedPlayer p = (ProxiedPlayer)sender;
		
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
			if(ClanUtils.isInvited(p, args[1])) {
				ClanUtils.acceptInviteToClan(p, args[1]);
			} else {
				MessageUtils.send(p, "You do not have an outstanding invite to that clan.");
			}
		} else if (command.equalsIgnoreCase("reject")) {
			if(ClanUtils.isInvited(p, args[1])) {
				ClanUtils.rejectInviteToClan(p, args[1]);
			} else {
				MessageUtils.send(p, "You do not have an outstanding invite to that clan.");
			}
		} else if (command.equalsIgnoreCase("leave")) {
			
		} else if (command.equalsIgnoreCase("clanchat")) {
			
		} else if (command.equalsIgnoreCase("cc")) {
			
		} else if (command.equalsIgnoreCase("help")) {
			
		} else {
			MessageUtils.send(sender, "Command not recognized, run /bc help for commands.");
		}
	}
}
