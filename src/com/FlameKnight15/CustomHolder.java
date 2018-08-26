package com.FlameKnight15;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CustomHolder implements InventoryHolder {

    private Player initiator, accepter;
    Inventory inventory;

    public CustomHolder(Player initiator, Player accepter){
        this.initiator = initiator;
        this.accepter = accepter;
        //this.inventory = Bukkit.createInventory(this, 54, ChatColor.translateAlternateColorCodes('&', "&6Trade: &b" + initiator.getName() + " &6& &c" + accepter.getName()));
    }

    public Player getInitiator(){
        return initiator;
    }

    public Player getAccepter(){
        return accepter;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
