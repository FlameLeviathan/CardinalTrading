package com.FlameKnight15;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Array;
import java.util.*;

public class TradeListener implements Listener {


    public HashMap<Player, Player> tradingPlayers = new HashMap<Player, Player>();
    public HashMap<Player, Double> moneyTrades = new HashMap<Player, Double>();
    ArrayList<Integer> traderSlots = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39, 45, 46, 47));
    ArrayList<Integer> accepterSlots = new ArrayList<Integer>(Arrays.asList(5, 6, 7, 8, 14, 15, 16, 17, 23, 24, 25, 26, 32, 33, 34, 35, 41, 42, 43, 44, 51, 52, 53));
    ArrayList<Integer> moneySlots = new ArrayList<Integer>(Arrays.asList(4, 13, 22, 31, 48, 50));
    boolean countdown = false;

    Core core;

    public TradeListener(Core c) {
        core = c;
    }

    Inventory tradeInv;

    public void addPlayersToTradeList(Player trader, Player accepter) {
        tradingPlayers.put(trader, accepter);
    }

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent event) {
        tradeInv = core.tradeGUI.tradeInventory;


        if (event.getInventory().equals(tradeInv)) {
            if (event.isShiftClick()) {
/*                Inventory inventory = event.getInventory();
                int slot = -1;
                ItemStack item = event.getCurrentItem();
                Player tradeInitiator, tradeAccepter;
                List<HumanEntity> viewers = inventory.getViewers();
                if (tradingPlayers.containsKey((Player) viewers.get(0))) {
                    //Add a check here when extending plugin to make sure no trade viewers are getting items
                    tradeInitiator = (Player) viewers.get(0);
                    tradeAccepter = (Player) viewers.get(1);
                } else {
                    //Add a check here when extending plugin to make sure no trade viewers are getting items
                    tradeAccepter = (Player) viewers.get(0);
                    tradeInitiator = (Player) viewers.get(1);

                }
                if(tradeInitiator == event.getWhoClicked()) {
                    slot = getNextOpenSlot(inventory, traderSlots);
                } else if(tradeAccepter == event.getWhoClicked()) {
                    slot = getNextOpenSlot(inventory, accepterSlots);
                }
                inventory.setItem(slot, item);

                item.setAmount(0);
                tradeAccepter.updateInventory();
                tradeInitiator.updateInventory();*/
                event.setCancelled(true);
            }
        }

        if(event.getClickedInventory() != null)
        if (event.getClickedInventory().equals(tradeInv)) {

            Inventory inventory = tradeInv;

            Player player = (Player) event.getWhoClicked();

            Inventory inv = player.getOpenInventory().getTopInventory();

            InventoryHolder holder = inv.getHolder();


            Player tradeInitiator, tradeAccepter;
            if(!(holder instanceof CustomHolder))
                return;

            CustomHolder tradeInfo = (CustomHolder) holder;
            tradeAccepter = tradeInfo.getAccepter();
            tradeInitiator = tradeInfo.getInitiator();

            int slotClicked = event.getSlot();
            double playerMoneyTrade, accepterMoneyTrade;

            //We know it is the trader clicking
            if (tradingPlayers.containsKey(player)) {
                if (traderSlots.contains(slotClicked) || moneySlots.contains(slotClicked) || slotClicked == 40 || slotClicked == 49) {

                    if(traderSlots.contains(slotClicked)){
                        if (inv.getItem(slotClicked) != null && inv.getItem(slotClicked).getType() != Material.AIR || player.getItemOnCursor() != null && player.getItemOnCursor().getType() != Material.AIR ){
                            if(countdown){
                                countdown = false;
                            }
                        }
                    }
                    if (moneySlots.contains(slotClicked)) {
                        event.setCancelled(true);
                        moneyTrade(slotClicked, player);
                    }

                    //accepts the trade
                    if (slotClicked == 40) {
                        //Add the player to accepted list and commence trade when both players have accepted
                        event.setCancelled(true);
                        acceptTrade(player, inventory.getItem(40));
                        //Set either 48 or 50 to the current status Red - Cancelling trade, Yellow - Processing Green - Ready

                    }
                    //rejects the trade
                    if (slotClicked == 49) {
                        //Cancel the trade and return all items
                        event.setCancelled(true);
                        countdown = false;
                        returnItems(inventory, tradeInitiator, tradeAccepter);
                        tradeInitiator.closeInventory();
                        tradeAccepter.closeInventory();
                    }

                    if(slotClicked ==48) {
                        if (inv.getItem(slotClicked) != null && inv.getItem(slotClicked).getType() == Material.PAPER) {
                            removeMoneyTrade(player, moneyTrades.get(player));
                            event.setCancelled(true);
                        }
                    }

                } else {
                    event.setCancelled(true);
                }
            } else { //We know it is the accepter clicking
                if (accepterSlots.contains(slotClicked) || moneySlots.contains(slotClicked) || slotClicked == 40 || slotClicked == 49) {
                    if(accepterSlots.contains(slotClicked)){
                        if (inv.getItem(slotClicked) != null && inv.getItem(slotClicked).getType() != Material.AIR || player.getItemOnCursor() != null && player.getItemOnCursor().getType() != Material.AIR  ){
                            if(countdown){
                                countdown = false;
                            }
                        }
                    }

                    if (moneySlots.contains(slotClicked)) {
                        event.setCancelled(true);
                        if(countdown){
                            countdown = false;
                        }
                        moneyTrade(slotClicked, player);
                    }

                    //accepts the trade
                    if (slotClicked == 40) {
                        //Add the player to accepted list and commence trade when both players have accepted
                        event.setCancelled(true);
                        acceptTrade(player, inventory.getItem(40));
                        //Set either 48 or 50 to the current status Red - Cancelling trade, Yellow - Processing Green - Ready
                        event.setCancelled(true);
                    }
                    //rejects the trade
                    if (slotClicked == 49) {
                        //Cancel the trade and return all items
                        event.setCancelled(true);
                        countdown = false;
                        returnItems(inventory, tradeInitiator, tradeAccepter);
                        tradeInitiator.closeInventory();
                        tradeAccepter.closeInventory();

                    }

                    if(slotClicked == 50){
                        if(inv.getItem(slotClicked) != null && inv.getItem(slotClicked).getType() == Material.PAPER) {
                            removeMoneyTrade(player, moneyTrades.get(player));
                            event.setCancelled(true);
                        }
                    }


                } else /*if(!(slotClicked >= 54))*/ {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * Logs what money trades a player is making
     *
     * @param slotClicked
     * @param player
     */
    public void moneyTrade(int slotClicked, Player player) {
        double addLittle = 50;
        double addBig = 500;
        double removeLittle = 50;
        double removeBig = 500;


        if (slotClicked == 4) {
            //+50
            addMoneyTrade(player, addLittle);
        }
        if (slotClicked == 13) {
            //+500
            addMoneyTrade(player, addBig);
        }
        if (slotClicked == 22) {
            //-500
            removeMoneyTrade(player, removeBig);
        }
        if (slotClicked == 31) {
            //-50
            removeMoneyTrade(player, removeLittle);
        }
    }

    /**
     * Removes money from the trade by the current player
     *
     * @param player
     * @param removeAmount
     */
    private void removeMoneyTrade(Player player, double removeAmount) {
        if (moneyTrades.containsKey(player)) {
            if (moneyTrades.get(player) >= removeAmount) {
                double amountOld = moneyTrades.get(player);
                double amountNew = amountOld - removeAmount;

                if(amountNew <= 0){
                    if (tradingPlayers.containsKey(player)) {
                        //Add a check here when extending plugin to make sure no trade viewers are getting items
                        player.getOpenInventory().getTopInventory().setItem(48, new ItemStack(Material.AIR));
                    } else {
                        //Add a check here when extending plugin to make sure no trade viewers are getting items
                        player.getOpenInventory().getTopInventory().setItem(50, new ItemStack(Material.AIR));

                    }
                    moneyTrades.remove(player);
                } else {

                    moneyTrades.put(player, amountNew);
                    moneyTrades.remove(player, amountOld);

                    if (tradingPlayers.containsKey(player)) {
                        //Add a check here when extending plugin to make sure no trade viewers are getting items
                        player.getOpenInventory().getTopInventory().setItem(48, giveMoneyItem(amountNew));
                    } else {
                        //Add a check here when extending plugin to make sure no trade viewers are getting items
                        player.getOpenInventory().getTopInventory().setItem(50, giveMoneyItem(amountNew));

                    }
                }

            }
        }
    }

    public ItemStack giveMoneyItem(double amount){
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Money trade for: " + amount);
        ArrayList<String> itemLore = new ArrayList<String>();
        itemMeta.setLore(itemLore);
        item.setItemMeta(itemMeta);
        return item;
    }


    /**
     * Gives the next open slot available for the player
     * @param inventory inventory that you are looking for a slot in
     * @param slots The available slots to put an item into
     * @return
     */
    public int getNextOpenSlot(Inventory inventory, ArrayList<Integer> slots) {
        for (int a = 0; a < slots.size(); a++) {
            if ((inventory.getItem(slots.get(a)) == null) || (inventory.getItem(slots.get(a)).getType() == Material.AIR)) {
                return slots.get(a);
            }
        }
        return -1;
    }

    /**
     * Adds money to the current trade from the player
     *
     * @param player
     * @param addAmount
     */
    private void addMoneyTrade(Player player, double addAmount) {
        double amountOld = 0;
        if (moneyTrades.containsKey(player)) {
            amountOld = moneyTrades.get(player);
        }
        double amountNew = amountOld + addAmount;
        if (core.econ.getBalance(player) >= amountNew) {
            if (moneyTrades.containsKey(player)) {

                moneyTrades.put(player, amountNew);
                moneyTrades.remove(player, amountOld);
                if (tradingPlayers.containsKey(player)) {
                    player.getOpenInventory().getTopInventory().setItem(48, giveMoneyItem(amountNew));
                } else {
                    player.getOpenInventory().getTopInventory().setItem(50, giveMoneyItem(amountNew));

                }
            } else {
                double amountNewAdd = addAmount;

                moneyTrades.put(player, amountNewAdd);
                if (tradingPlayers.containsKey(player)) {
                    player.getOpenInventory().getTopInventory().setItem(48, giveMoneyItem(amountNewAdd));
                } else {
                    player.getOpenInventory().getTopInventory().setItem(50, giveMoneyItem(amountNewAdd));

                }
            }
        }
    }

    /**
     * Runs when a player clicks the accept trade button
     *
     * @param player player who clicked
     * @param item   item clicked
     */
    public void acceptTrade(Player player, ItemStack item) {
        ItemStack circle = new ItemStack(Material.POISONOUS_POTATO);
        ItemMeta itemMeta = circle.getItemMeta();
        String playerNameColor = "&f";

        itemMeta.setDisplayName(ChatColor.GREEN + "Accept Trade");
        ArrayList<String> itemLore = (ArrayList<String>) item.getItemMeta().getLore();
        if (item.getType().equals(Material.POISONOUS_POTATO)) {
            if (tradingPlayers.containsKey(player)) {
                if (item.getItemMeta().getLore().get(1).contains("READY")) {
                    //Cancel acceptance
                    countdown = false;
                    itemLore.set(1, core.tradeGUI.parseColors(playerNameColor + player.getName() + ": " + ChatColor.YELLOW + " PROCESSING"));
                } else {
                    itemLore.set(1, core.tradeGUI.parseColors(playerNameColor + player.getName() + ": " + ChatColor.GREEN + "READY"));
                    if (item.getItemMeta().getLore().get(2).contains("READY")) {
                        countdownTrade(core.tradeGUI.tradeInventory);
                    }
                }
            } else if (tradingPlayers.containsValue(player)) {
                if (item.getItemMeta().getLore().get(2).contains("READY")) {
                    //Cancel acceptance
                    countdown = false;
                    itemLore.set(2, core.tradeGUI.parseColors(playerNameColor + player.getName() + ": " + ChatColor.YELLOW + "PROCESSING"));
                } else {
                    itemLore.set(2, core.tradeGUI.parseColors(playerNameColor + player.getName() + ": " + ChatColor.GREEN + "READY"));
                    if (item.getItemMeta().getLore().get(1).contains("READY")) {
                        countdownTrade(core.tradeGUI.tradeInventory);
                    }
                }
            }

            itemMeta.setLore(itemLore);
            circle.setItemMeta(itemMeta);
            player.getOpenInventory().getTopInventory().setItem(40, circle);
        }
    }

    int periodCount;

    /**
     * Creates a timer for the trade until it completes for a player to cancel
     * @param inventory Inventory the trade is happening from
     */
    public void countdownTrade(Inventory inventory) {
        int delay = 0;
        int period = (core.config.getInt("tradeCountdownTime") + 1) * 200;/*core.config.getInt("tradeCountdownTime");*/
        periodCount = period / 200;

        countdown = true;

        InventoryHolder holder = inventory.getHolder();

        Player tradeInitiator, tradeAccepter;
        if(!(holder instanceof CustomHolder))
            return;

        CustomHolder tradeInfo = (CustomHolder) holder;
        tradeAccepter = tradeInfo.getAccepter();
        tradeInitiator = tradeInfo.getInitiator();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                if (!countdown) {
                    ItemStack item = inventory.getItem(40);
                    ItemStack circle = new ItemStack(Material.POISONOUS_POTATO);
                    ItemMeta itemMeta = circle.getItemMeta();
                    itemMeta.setDisplayName(ChatColor.GREEN + "Accept Trade");
                    ArrayList<String> itemLore = (ArrayList<String>) item.getItemMeta().getLore();
                    String playerNameColor = "&f";

                    itemLore.set(1, core.tradeGUI.parseColors(playerNameColor + tradeInitiator.getName() + ":" + ChatColor.YELLOW + " PROCESSING"));

                    itemLore.set(2, core.tradeGUI.parseColors(playerNameColor + tradeAccepter.getName() + ":" + ChatColor.YELLOW + " PROCESSING"));

                    itemMeta.setLore(itemLore);
                    circle.setItemMeta(itemMeta);
                    inventory.setItem(40, circle);
                    ItemStack cancelButton = core.tradeGUI.rejectTrade;
                    cancelButton.setAmount(1);
                    inventory.setItem(49, cancelButton);
                    timer.cancel();
                    return;
                } else {
                    if (periodCount == 1) {
                        finishTrade(inventory);
                        timer.cancel();
                    } else {
                        periodCount = periodCount - 1;
                        ItemStack cancelButton = core.tradeGUI.rejectTrade;
                        cancelButton.setAmount(periodCount);
                        //System.out.println(periodCount + "");
                        inventory.setItem(49, cancelButton);
                    }
                }

            }
        }, delay, period);
    }

    /**
     * Completes the trade and transfers the items
     *
     * @param inventory
     */
    public void finishTrade(Inventory inventory) {
        Player tradeInitiator, tradeAccepter;
        Inventory inv = inventory;

        InventoryHolder holder = inv.getHolder();

        if(!(holder instanceof CustomHolder))
            return;

        CustomHolder tradeInfo = (CustomHolder) holder;

        tradeAccepter = tradeInfo.getAccepter();
        tradeInitiator = tradeInfo.getInitiator();


        for (int a = 0; a < traderSlots.size(); a++) {
            if ((inventory.getItem(traderSlots.get(a)) == null) || (inventory.getItem(traderSlots.get(a)).getType() == Material.AIR)) {
            } else {
                tradeAccepter.getInventory().addItem(inventory.getItem(traderSlots.get(a)));
                inventory.removeItem(inventory.getItem(traderSlots.get(a)));
            }
        }
        for (int a = 0; a < accepterSlots.size(); a++) {
            if ((inventory.getItem(accepterSlots.get(a)) == null) || (inventory.getItem(accepterSlots.get(a)).getType() == Material.AIR)) {
            } else {
                tradeInitiator.getInventory().addItem(inventory.getItem(accepterSlots.get(a)));
                inventory.removeItem(inventory.getItem(accepterSlots.get(a)));
            }
        }


        if (moneyTrades.containsKey(tradeInitiator)) {
            core.econ.depositPlayer(tradeAccepter, moneyTrades.get(tradeInitiator));
            core.econ.withdrawPlayer(tradeInitiator, moneyTrades.get(tradeInitiator));
        }
        if (moneyTrades.containsKey(tradeAccepter)) {
            core.econ.depositPlayer(tradeInitiator, moneyTrades.get(tradeAccepter));
            core.econ.withdrawPlayer(tradeAccepter, moneyTrades.get(tradeAccepter));
        }

        tradingPlayers.remove(tradeInitiator, tradeAccepter);
        tradeInfo.getInitiator().closeInventory();
        tradeInfo.getAccepter().closeInventory();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player whoClosed = (Player) event.getPlayer();

        Inventory inv = whoClosed.getOpenInventory().getTopInventory();

        InventoryHolder holder = inv.getHolder();

            Player tradeInitiator, tradeAccepter;
            if(!(holder instanceof CustomHolder))
                return;

            CustomHolder tradeInfo = (CustomHolder) holder;

            tradeAccepter = tradeInfo.getAccepter();
            tradeInitiator = tradeInfo.getInitiator();

            if(whoClosed == tradeAccepter) {

                if ((tradeInitiator.isOnline()) && (tradeInitiator.getOpenInventory() != null) &&
                        (tradeInitiator.getOpenInventory().getTopInventory() != null) && (tradeInitiator.getOpenInventory().getTopInventory().equals(core.tradeGUI.tradeInventory)) && (tradingPlayers.containsKey(tradeInitiator))) {
                    ItemStack cursor = tradeInitiator.getOpenInventory().getCursor();
                    tradeInitiator.getOpenInventory().setCursor(null);
                    tradeInitiator.getInventory().addItem(new ItemStack[]{cursor});
                    tradingPlayers.remove(tradeInitiator, tradeAccepter);
                    tradeInitiator.closeInventory();
                    tradeInitiator.updateInventory();
                    returnItems(inv, tradeInitiator, tradeAccepter);
                    return;
                }
            } else
            if(whoClosed == tradeInitiator) {
                if ((tradeAccepter.isOnline()) && (tradeAccepter.getOpenInventory() != null) &&
                        (tradeAccepter.getOpenInventory().getTopInventory() != null) && (tradeAccepter.getOpenInventory().getTopInventory().equals(core.tradeGUI.tradeInventory)) && (tradingPlayers.containsValue(tradeAccepter))) {
                    ItemStack cursor = tradeAccepter.getOpenInventory().getCursor();
                    tradeAccepter.getOpenInventory().setCursor(null);
                    tradeAccepter.getInventory().addItem(new ItemStack[]{cursor});
                    tradingPlayers.remove(tradeInitiator, tradeAccepter);
                    tradeAccepter.closeInventory();
                    tradeAccepter.updateInventory();
                    returnItems(inv, tradeInitiator, tradeAccepter);
                    return;
                }
            }
            returnItems(inv, tradeInitiator, tradeAccepter);
            return;
    }

    /**
     * Returns the items to the player in the trade
     * @param inventory Inventory that items are being returned from (Trade Inventory)
     * @param tradeInitiator Player who initiated the trade
     * @param tradeAccepter Player who accepted the trade
     */
    public void returnItems(Inventory inventory, Player tradeInitiator, Player tradeAccepter) {

        if (inventory.getName().contains("Trade:")) {

            for (int a = 0; a < traderSlots.size(); a++) {
                if (inventory.getItem(traderSlots.get(a)) == null || inventory.getItem(traderSlots.get(a)).getType() == Material.AIR || inventory.getItem(traderSlots.get(a)).getAmount() == 0) {
                }else {
                    tradeInitiator.getInventory().addItem(inventory.getItem(traderSlots.get(a)));
                    inventory.removeItem(inventory.getItem(traderSlots.get(a)));
                }
            }
            for (int a = 0; a < accepterSlots.size(); a++) {
                if (inventory.getItem(accepterSlots.get(a)) == null || inventory.getItem(accepterSlots.get(a)).getType() == Material.AIR || inventory.getItem(accepterSlots.get(a)).getAmount() == 0) {
                } else{
                    tradeAccepter.getInventory().addItem(inventory.getItem(accepterSlots.get(a)));
                    inventory.removeItem(inventory.getItem(accepterSlots.get(a)));
                }
            }
            if (moneyTrades.containsKey(tradeInitiator)) {
                moneyTrades.remove(tradeInitiator);
            }
            if (moneyTrades.containsKey(tradeAccepter)) {
                moneyTrades.remove(tradeAccepter);
            }
            tradingPlayers.remove(tradeInitiator, tradeAccepter);
        }
    }
}
