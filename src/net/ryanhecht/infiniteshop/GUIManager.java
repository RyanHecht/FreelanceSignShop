package net.ryanhecht.infiniteshop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIManager {
	Inventory inv;
	ItemStack is;
	String type;
	int price;
	public GUIManager(String TYPE, ItemStack IS, int PRICE) {
		type=TYPE;
		inv=Bukkit.createInventory(null, 9, type+":");
		price=PRICE;
		is=IS;
	}
	public void display(Player player) {
		ItemMeta im=is.getItemMeta();
		inv.setItem(3, is);
		im.setDisplayName(type + ChatColor.YELLOW + " 1 Item" + ChatColor.WHITE + " for " + ChatColor.GREEN + "$" + price);
		inv.getItem(3).setItemMeta(im);
		
		inv.setItem(4, is);
		im=inv.getItem(4).getItemMeta();
		inv.getItem(4).setAmount(32);
		im.setDisplayName(type + ChatColor.YELLOW + " 32 Items" + ChatColor.WHITE + " for " + ChatColor.GREEN + "$" + (price*32));
		inv.getItem(4).setItemMeta(im);
		
		inv.setItem(5, is);
		im=inv.getItem(4).getItemMeta();
		inv.getItem(5).setAmount(64);
		im.setDisplayName(type + ChatColor.YELLOW + " 64 Items" + ChatColor.WHITE + " for " + ChatColor.GREEN + "$" + (price*64));
		inv.getItem(5).setItemMeta(im);
		
		
		if(type.equalsIgnoreCase("sell")) {
			inv.setItem(6, is);
			im=inv.getItem(6).getItemMeta();
			int amount=0;
			for(ItemStack pis : player.getInventory()) {
				if(pis!=null && pis.isSimilar(is)) {
					amount+=pis.getAmount();
				}
			}
			inv.setMaxStackSize(amount);
			inv.getItem(6).setAmount(amount);
			im.setDisplayName(type + ChatColor.YELLOW + " All Items (" + amount + ") " + ChatColor.WHITE + " for " + ChatColor.GREEN + "$" + (price*amount));
			inv.getItem(6).setItemMeta(im);
		}
		
		
		
		player.openInventory(inv);
	}
}
