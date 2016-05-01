package net.ryanhecht.infiniteshop;

import net.milkbowl.vault.economy.EconomyResponse;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SignListener implements Listener {
	public SignListener(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		Player player = e.getPlayer();
		//[iBuy]
		//item ID
		//
		//Price
		if(e.getLine(0).equalsIgnoreCase("[ibuy]")) {
			if(e.getPlayer().isOp()) {
			int id=-1;
			int price=-1;
			try {
				id = Integer.parseInt(e.getLine(1));
			}
			catch(NumberFormatException x) {
				player.sendMessage(ChatColor.RED + "Improper formatting! Second Line: Item ID | Fourth Line: Price");
				return;
			}
			try {
				price = Integer.parseInt(e.getLine(3));
			}
			catch(NumberFormatException x) {
				player.sendMessage(ChatColor.RED + "Improper formatting! Second Line: Item ID | Fourth Line: Price");
				return;
			}
			
			e.setLine(0, ChatColor.BLUE + "[iBuy]");
			String name=formatName(Material.getMaterial(id).toString());
			if(15<name.length()) {
				
				e.setLine(1, name.substring(0, 15));
				e.setLine(2, name.substring(15) + " For");
			}
			else {
				e.setLine(1, name);
				e.setLine(2, "For");
			}
			
			e.setLine(3, "$"+price+"");
			}
			else {
				player.sendMessage("You must be opped to make a shop!");
			}
		}
		else if(e.getLine(0).equalsIgnoreCase("[isell]")) {
			if(player.isOp()) {
			int id=-1;
			int price=-1;
			try {
				id = Integer.parseInt(e.getLine(1));
			}
			catch(NumberFormatException x) {
				player.sendMessage(ChatColor.RED + "Improper formatting! Second Line: Item ID | Fourth Line: Price");
				return;
			}
			try {
				price = Integer.parseInt(e.getLine(3));
			}
			catch(NumberFormatException x) {
				player.sendMessage(ChatColor.RED + "Improper formatting! Second Line: Item ID | Fourth Line: Price");
				return;
			}
			
			e.setLine(0, ChatColor.BLUE + "[iSell]");
			String name=formatName(Material.getMaterial(id).toString());
			if(15<name.length()) {
				
				e.setLine(1, name.substring(0, 15));
				e.setLine(2, name.substring(15) + " For");
			}
			else {
				e.setLine(1, name);
				e.setLine(2, "For");
			}
			
			e.setLine(3, "$"+price+"");
		}
			else {
				player.sendMessage("You must be opped to make a shop!");
			}
		}
	}
	
	@EventHandler
	public void onSignClick(PlayerInteractEvent e) {
		if(e.getAction()==(Action.RIGHT_CLICK_BLOCK)) {
			//e.getPlayer().sendMessage(e.getClickedBlock().getType().toString());
			if(e.getClickedBlock().getType().equals(Material.SIGN) || e.getClickedBlock().getType().equals(Material.WALL_SIGN)) {
				Sign sign = (Sign) e.getClickedBlock().getState();
				if(sign.getLine(0).equals(ChatColor.BLUE + "[iBuy]")) {
					String name = sign.getLine(1);
					if(sign.getLine(2).indexOf("For") != 0) {
						name=name+sign.getLine(2).substring(0, sign.getLine(2).indexOf("For")-1);
					}
					//e.getPlayer().sendMessage(unformatName(name));
					ItemStack is = new ItemStack(Material.getMaterial(unformatName(name)));
					int price = Integer.parseInt(sign.getLine(3).substring(sign.getLine(3).indexOf("$")+1));
					new GUIManager("Buy", is, price).display(e.getPlayer());
					//buy(e.getPlayer(), is, price);
				}
				if(sign.getLine(0).equals(ChatColor.BLUE + "[iSell]")) {
					String name = sign.getLine(1);
					if(sign.getLine(2).indexOf("For") != 0) {
						name=name+sign.getLine(2).substring(0, sign.getLine(2).indexOf("For")-1);
					}
					//e.getPlayer().sendMessage(unformatName(name));
					ItemStack is = new ItemStack(Material.getMaterial(unformatName(name)));
					int price = Integer.parseInt(sign.getLine(3).substring(sign.getLine(3).indexOf("$")+1));
					new GUIManager("Sell", is, price).display(e.getPlayer());
					//sell(e.getPlayer(), is, price);
				}
			}
		}
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		Inventory inv = e.getInventory();
		if(inv.getName().contains("Buy:")) {
			if(inv.getItem(e.getSlot()) != null) {
				ItemStack is = inv.getItem(e.getSlot());
				ItemStack toBuy = new ItemStack(is.getType(), is.getAmount());
				buy((Player)e.getWhoClicked(), toBuy, Integer.parseInt(is.getItemMeta().getDisplayName().substring(is.getItemMeta().getDisplayName().indexOf("$")+1)));
				e.getWhoClicked().closeInventory();
			}
		}
		else if(inv.getName().contains("Sell:")) {
			if(inv.getItem(e.getSlot()) != null) {
				ItemStack is = inv.getItem(e.getSlot());
				ItemStack toSell = new ItemStack(is.getType(), is.getAmount());
				sell((Player)e.getWhoClicked(), toSell, Integer.parseInt(is.getItemMeta().getDisplayName().substring(is.getItemMeta().getDisplayName().indexOf("$")+1)));
				e.getWhoClicked().closeInventory();
			}
		}
	}
	
	
	
	
	
	
	
	public String formatName(String s) {
		s = s.replace("_", " ");
		s=WordUtils.capitalizeFully(s);
		return s;
	}
	public String unformatName(String s) {
		s = s.replace(" ", "_");
		s=s.toUpperCase();
		return s;
	}
	public boolean buy(Player player, ItemStack is, int price) {
		if(isFull(player.getInventory(), is)) {
			player.sendMessage(ChatColor.RED + "You can't buy that; you don't have enough room in your inventory!");
			return false;
		}
		if(price>Main.getEco().getBalance(player)) {
			player.sendMessage(ChatColor.RED + "Insufficient funds!");
			return false;
		}
		else {
		EconomyResponse r = Main.getEco().withdrawPlayer(player, price);
		if(r.transactionSuccess()) {
			player.sendMessage(ChatColor.AQUA + "You successfully bought " + ChatColor.GREEN + is.getAmount() + " " + formatName(is.getType().toString() + ChatColor.AQUA + " for " + ChatColor.GREEN + "$" + price));
			player.getInventory().addItem(is);
		}
		else {
			player.sendMessage("Error occured in your transaction! " + r.errorMessage);
			return false;
		}
		}
		return true;
	}
	public boolean sell(Player player, ItemStack is, int price) {
		int amount=0;
		for(ItemStack pis : player.getInventory()) {
			if(pis!=null && pis.isSimilar(is)) {
				amount+=pis.getAmount();
			}
		}
		if(amount>is.getAmount() || amount==0) {
			player.sendMessage(ChatColor.RED + "You don't have any of that to sell!");
			return false;
		}
		EconomyResponse r = Main.getEco().depositPlayer(player, price);
		if(r.transactionSuccess()) {
			player.sendMessage(ChatColor.AQUA + "You successfully sold " + ChatColor.GREEN + is.getAmount() + " " + formatName(is.getType().toString() + ChatColor.AQUA + " for " + ChatColor.GREEN + "$" + price));
			player.getInventory().removeItem(is);
		}
		else {
			player.sendMessage("Error occured in your transaction! " + r.errorMessage);
			return false;
		}
		
		return true;
	}
	public boolean isFull(PlayerInventory i, ItemStack contains) {
		for(ItemStack is : i) {
			if(is == null) {
				return false;
			}
			else if(is.getType().equals(contains.getType()) && is.getAmount()+contains.getAmount() <= 64) {
				return false;
			}
		}
		return true;
	}
}
