package com.webmets.forceopchat;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new ForceOPChat(this), this);
	}
	
}
