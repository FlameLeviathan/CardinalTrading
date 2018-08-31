package com.FlameKnight15;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TradeGUI {
    Core core;

    TradeListener tradeListener;
    public TradeGUI(TradeListener listener, Core c){
        tradeListener = listener;
        core = c;
    }
    ItemStack rejectTrade = editMaterial(Material.BARRIER, ChatColor.RED + "Cancel Trade");
    Inventory tradeInventory;

    /**
     * Creates a trade GUI between the trader and the trade accepter
     * @param trader Player who initiated the trade
     * @param accepter Player who accepted the trade
     */
    public void openTrade(Player trader, Player accepter){
        tradeInventory = Bukkit.createInventory(new CustomHolder(trader, accepter), 54, ChatColor.translateAlternateColorCodes('&', "&6Trade: &b" + trader.getName() + " &6& &c" + accepter.getName()));


        double amountOne = core.config.getDouble("moneyAmountOne");
        double amountTwo = core.config.getDouble("moneyAmountTwo");
        double amountThree = core.config.getDouble("moneyAmountThree");
        double amountFour = core.config.getDouble("moneyAmountFour");
        //Set items in the trade
        //TODO: Change the money amount names
        ItemStack blackGlass = new ItemStack(Material.STAINED_GLASS_PANE,1, (byte) 0);
        ItemStack addFiftyCor = editMaterial(Material.PAPER,ChatColor.GREEN+ "Add " +amountOne+" Cor");
        ItemStack addFiveHundredCor = editMaterial(Material.PAPER,ChatColor.GREEN+ "Add " +amountTwo+" Cor");
        ItemStack removeFiftyCor = editMaterial(Material.PAPER,ChatColor.GREEN+ "Add " +amountThree+" Cor");
        ItemStack removeFiveHundredCor = editMaterial(Material.PAPER,ChatColor.GREEN+ "Add " +amountFour+" Cor");
        ItemStack moneyItem = editMaterial(Material.PAPER,ChatColor.GREEN+ "Cor: 0");
        ItemStack acceptTrade = editMaterial(Material.POISONOUS_POTATO, ChatColor.GREEN + "Accept Trade", "&B&nStatuses:",  ("&f"+trader.getName() + ": &ePROCESSING"),  ("&f"+accepter.getName()+ ": &ePROCESSING"));



        
        //tradeInventory.setItem(13, blackGlass);
        tradeInventory.setItem(4, addFiftyCor);
        tradeInventory.setItem(13, addFiveHundredCor);
        tradeInventory.setItem(31, removeFiveHundredCor);
        tradeInventory.setItem(22, removeFiftyCor);
        //tradeInventory.setItem(49, blackGlass);
        tradeInventory.setItem(40,acceptTrade);
        rejectTrade.setAmount(1);
        tradeInventory.setItem(49, rejectTrade);
        tradeInventory.setItem(48, moneyItem);
        tradeInventory.setItem(50, moneyItem);


        //REMEMBER TO DO ECONOMY CHECKS
        //MAKE SURE THEY HAVE ENOUGH TO ADD AND SUBTRACT FROM BALANCE
        //CAN'T Remove 500 when only 50 is in the trade or 0 etc...
        trader.openInventory(tradeInventory);
        accepter.openInventory(tradeInventory);
        tradeListener.tradingPlayers.put(trader, accepter);
    }

    public ItemStack editMaterial(Material material, String name, String... lore){
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        ArrayList<String> itemLore = new ArrayList<String>();
        for(String stringInLore: lore){
            itemLore.add(parseColors(stringInLore));
        }
        itemMeta.setLore(itemLore);
        item.setItemMeta(itemMeta);
        return item;
    }

    /**
     * Translates the color codes of the text given
     * @param string
     * @return
     */
    public String parseColors(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
