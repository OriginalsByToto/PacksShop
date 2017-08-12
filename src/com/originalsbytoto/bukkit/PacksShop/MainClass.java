package com.originalsbytoto.bukkit.packsshop;

import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.GeneratedValue;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class MainClass extends JavaPlugin implements Listener{

	public static Economy econ = null;
	
	@Override
	public void onEnable() {
		
		getLogger().info("PackShops initialization ...");
		
			PluginManager pm = getServer().getPluginManager();
			pm.registerEvents(this, this);
			
			setupEconomy();
			
			saveDefaultConfig();
		
		getLogger().info("PackShops is enabled");
		
		
		
	}
	
	@Override
	public void onDisable() {
		
		getLogger().info("PackShops disabling ...");
		
		getLogger().info("PackShops is disabled");
		
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		
		int pId = 96517;
		ApiUpdate updater = new ApiUpdate(pId);
		updater.query();
		String n = updater.getLatestName();

		if(!compare(getDescription().getVersion(), n)) {
			return;
		}

		
		if(getConfig().getBoolean("update.active", true)) {
			
			String message = "A new update for PacksShop is available. Find it on http://http://dev.bukkit.org/bukkit-plugins/packsshop/ !";
			
			if(getConfig().getBoolean("update.onlyOPs", true)) {
				
				if(e.getPlayer().isOp()) e.getPlayer().sendMessage(message);
				
			} else {
				
				e.getPlayer().sendMessage(message);
				
			}
			
		}
		
	}
	
	private boolean setupEconomy() {
 
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
 
        if (economyProvider != null) {
 
            econ = economyProvider.getProvider();
 
        }
 
        return (econ != null);
 
    }
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		
		if(cmd.getName().equalsIgnoreCase("packsshop")) {
			
			if(sender.isOp() == false) {
				
				if(sender.hasPermission("packsshop.use") == false) {
				
					sender.sendMessage(getConfig().getString("messages.nopermission", "§4You can't do that!"));
					return true;
				
				}
				
			}
			
			if(args.length >= 2) {
				
				if(getConfig().contains("packs." + args[0].toString())) {
					
					if(getConfig().contains("packs." + args[0].toString() + ".items")) {
						
						List<String> items = getConfig().getStringList("packs." + args[0].toString() + ".items");
						
						for(int i = 0; i < items.size(); i++) {
							
							String item = items.get(i);
							String[] values = item.split("/");
							
							String name = values[0];
							int q = Integer.valueOf(values[1]);
							
							String subid = "";
							
							if(values.length >= 3) subid = values[2];
							
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + args[1].toString() + " " + name + " " + q + " " + subid);
							
						}
						
					}
					
					if(getConfig().contains("packs." + args[0].toString() + ".money")) {
						
						econ.depositPlayer(args[1].toString(), getConfig().getDouble("packs." + args[0] + ".money"));
						
					}
					
				} else {
					
					sender.sendMessage(getConfig().getString("messages.noexist", "§4This pack doesn't exist!"));
					
				}
									
			} else {
				
				sender.sendMessage(getConfig().getString("messages.arguments", "§4Please provide a minimum of 2 arguments in the command !"));
				
			}
			
		} else if(cmd.getName().equalsIgnoreCase("packsreload")) {
			
			if(sender.isOp() == false) {
				
				if(sender.hasPermission("packsshop.reload") == false) {
				
					sender.sendMessage(getConfig().getString("messages.nopermission", "§4You can't do that!"));
					return true;
				
				}
				
				this.reloadConfig();
				
				sender.sendMessage(getConfig().getString("messages.reload", "§2Configuration reloaded!"));
				
			}
			
		}
		
		return true;
		
	}
	
    private static boolean compare(String v1, String v2) {
        String s1 = normalisedVersion(v1);
        String s2 = normalisedVersion(v2);
        int cmp = s1.compareTo(s2);
        boolean cmpStr = cmp < 0 ? false : cmp > 0 ? true : true;
        return cmpStr;
    }

    public static String normalisedVersion(String version) {
        return normalisedVersion(version, ".", 4);
    }

    public static String normalisedVersion(String version, String sep, int maxWidth) {
        String[] split = Pattern.compile(sep, Pattern.LITERAL).split(version);
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            sb.append(String.format("%" + maxWidth + 's', s));
        }
        return sb.toString();
    }
	
}
