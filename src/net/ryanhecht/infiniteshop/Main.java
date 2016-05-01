package net.ryanhecht.infiniteshop;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public static Main instance;
	public static Economy eco = null;
	@Override
	public void onEnable() {
	instance = this;
	new SignListener(this);
	if(!setupEconomy()) {
		getLogger().severe("Disabled, no Vault/economy found!");
		getServer().getPluginManager().disablePlugin(this);
	}
	}
	
	
	
	public static Main getInstance() {
		if(instance == null) {
			return null;
		}
		else {
			return instance;
		}
	}
	public static Economy getEco() {
		return eco;
	}
	
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        eco = rsp.getProvider();
        return eco != null;
    }
}
