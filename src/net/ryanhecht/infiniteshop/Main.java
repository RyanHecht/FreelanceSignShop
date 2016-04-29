package net.ryanhecht.infiniteshop;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public static Main instance;
	@Override
	public void onEnable() {
	instance = this;
	new SignListener(this);
	}
	
	
	
	public static Main getInstance() {
		if(instance == null) {
			return null;
		}
		else {
			return instance;
		}
	}
}
