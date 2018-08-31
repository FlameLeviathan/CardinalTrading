package com.FlameKnight15;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
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

        Player whoClicked = (Player) event.getWhoClicked();
        Inventory inventoryTemp = tradeInv;

        Player playerTemp = (Player) event.getWhoClicked();

        Inventory invTemp = playerTemp.getOpenInventory().getTopInventory();
        Inventory invTempBottom = playerTemp.getOpenInventory().getBottomInventory();

        InventoryHolder holderTemp = invTemp.getHolder();


        Player tradeInitiatorTemp, tradeAccepterTemp;
        if (!(holderTemp instanceof CustomHolder))
            return;

        CustomHolder tradeInfoTemp = (CustomHolder) holderTemp;
        tradeAccepterTemp = tradeInfoTemp.getAccepter();
        tradeInitiatorTemp = tradeInfoTemp.getInitiator();


        ArrayList slots = new ArrayList();
        ArrayList playerSlots = new ArrayList();
        if (whoClicked == tradeInitiatorTemp) {
            slots = accepterSlots;
            playerSlots = traderSlots;
        }
        if (whoClicked == tradeAccepterTemp) {
            slots = traderSlots;
            playerSlots = accepterSlots;
        }
        /*whoClicked.sendMessage(" " + event.getInventory().equals(tradeInv)
                + " " + tradeInv.contains(event.getInventory().getItem(event.getSlot()))
                + " " + event.isShiftClick()); //+ (inventoryTemp.getItem((int)slots.get(i)).getType() == event.getCursor().getType()));
/*        if(event.getClickedInventory().equals(invTempBottom)){
            if (event.isShiftClick()) {
                event.setCancelled(true);
            }
        }*/


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
        if (playerSlots.contains(event.getSlot()) || event.getClickedInventory().equals(whoClicked.getOpenInventory().getBottomInventory())) {
            for (int i = 0; i < slots.size(); i++) {

                if ((inventoryTemp.getItem((int) slots.get(i)) != null && inventoryTemp.getItem((int) slots.get(i)).getType() != Material.AIR) && inventoryTemp.getItem((int) slots.get(i)).getType() == event.getCursor().getType()) {
                    if (countdown) {
                        countdown = false;
                    }

                    event.getClickedInventory().setItem(event.getSlot(), event.getCursor());
                    whoClicked.getOpenInventory().setCursor(null);
                    event.setCancelled(true);
                    continue;
                }
            }
        }/*tradeInv.contains(event.getInventory().getItem(event.getSlot())) ||*/
        if (event.getClickedInventory() != null)
            if (event.getClickedInventory().equals(tradeInv)) {

                Inventory inventory = tradeInv;

                Player player = (Player) event.getWhoClicked();

                Inventory inv = player.getOpenInventory().getTopInventory();

                InventoryHolder holder = inv.getHolder();


                Player tradeInitiator, tradeAccepter;
                if (!(holder instanceof CustomHolder))
                    return;

                CustomHolder tradeInfo = (CustomHolder) holder;
                tradeAccepter = tradeInfo.getAccepter();
                tradeInitiator = tradeInfo.getInitiator();

                int slotClicked = event.getSlot();
                double playerMoneyTrade, accepterMoneyTrade;
                countdown = false;



/*            ArrayList slots = new ArrayList();
            if(whoClicked == tradeInitiator)
                slots = accepterSlots;
            if( whoClicked == tradeAccepter)
                slots = traderSlots;
            if(event.getInventory().equals(tradeInv)){

                for(int i =0; i <= slots.size(); i++){
                    whoClicked.sendMessage(" " + event.getInventory().equals(tradeInv)
                            + " " + tradeInv.contains(event.getInventory().getItem(event.getSlot()))
                            + " " + (inventory.getItem((int)slots.get(i)).getType() == event.getCursor().getType()));
                    if (inventory.getItem((int)slots.get(i)).getType() == event.getCursor().getType()){
                        if(countdown){
                            countdown = false;
                        }
                        event.setCancelled(true);
                    }
                }*//*tradeInv.contains(event.getInventory().getItem(event.getSlot())) ||*//*
            }*/

                //We know it is the trader clicking
                if (tradingPlayers.containsKey(player)) {
                    if (traderSlots.contains(slotClicked) || moneySlots.contains(slotClicked) || slotClicked == 40 || slotClicked == 49) {

                        if (traderSlots.contains(slotClicked)) {
                            if (inv.getItem(slotClicked) != null && inv.getItem(slotClicked).getType() != Material.AIR || player.getItemOnCursor() != null && player.getItemOnCursor().getType() != Material.AIR) {
                                if (countdown) {
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

                        if (slotClicked == 48) {
                            if (inv.getItem(slotClicked) != null && inv.getItem(slotClicked).getType() == Material.PAPER) {
                                removeMoneyTrade(player, moneyTrades.get(player));
                                event.setCancelled(true);
                            } else
                                event.setCancelled(true);
                        }

                    } else {
                        event.setCancelled(true);
                    }
                } else { //We know it is the accepter clicking
                    if (accepterSlots.contains(slotClicked) || moneySlots.contains(slotClicked) || slotClicked == 40 || slotClicked == 49) {
                        if (accepterSlots.contains(slotClicked)) {
                            if (inv.getItem(slotClicked) != null && inv.getItem(slotClicked).getType() != Material.AIR || player.getItemOnCursor() != null && player.getItemOnCursor().getType() != Material.AIR) {
                                if (countdown) {
                                    countdown = false;
                                }
                            }
                        }

                        if (moneySlots.contains(slotClicked)) {
                            event.setCancelled(true);
                            if (countdown) {
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

                        if (slotClicked == 50) {
                            if (inv.getItem(slotClicked) != null && inv.getItem(slotClicked).getType() == Material.PAPER) {
                                removeMoneyTrade(player, moneyTrades.get(player));
                                event.setCancelled(true);
                            } else
                                event.setCancelled(true);
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
        double amountOne = core.config.getDouble("moneyAmountOne");
        double amountTwo = core.config.getDouble("moneyAmountTwo");
        double amountThree = core.config.getDouble("moneyAmountThree");
        double amountFour = core.config.getDouble("moneyAmountFour");


        if (slotClicked == 4) {
            addMoneyTrade(player, amountOne);
        }
        if (slotClicked == 13) {
            addMoneyTrade(player, amountTwo);
        }
        if (slotClicked == 22) {
            addMoneyTrade(player, amountFour);
        }
        if (slotClicked == 31) {
            addMoneyTrade(player, amountThree);
        }
    }

    @EventHandler
    public void onDragEvent(InventoryDragEvent event) {
        Inventory inventory = tradeInv;

        Player player = (Player) event.getWhoClicked();

        Inventory inv = player.getOpenInventory().getTopInventory();

        InventoryHolder holder = inv.getHolder();


        Player tradeInitiator, tradeAccepter;
        if (!(holder instanceof CustomHolder))
            return;

        CustomHolder tradeInfo = (CustomHolder) holder;
        tradeAccepter = tradeInfo.getAccepter();
        tradeInitiator = tradeInfo.getInitiator();
        ArrayList slots = new ArrayList();
        ArrayList playerSlots = new ArrayList();
        if (player == tradeInitiator) {
            slots = accepterSlots;

        }
        if (player == tradeAccepter) {
            slots = traderSlots;

        }
        /*player.sendMessage(event.getInventory().equals(inventory)
        + " " + slots.contains(event.getInventorySlots()));*/
        if (event.getInventory().equals(inventory))
            if (!slots.contains(event.getInventorySlots())) {
                event.setCancelled(true);
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
                ItemStack moneyItem = core.tradeGUI.editMaterial(Material.PAPER, ChatColor.GREEN + "Cor: 0");

                if (amountNew <= 0) {
                    if (tradingPlayers.containsKey(player)) {
                        //Add a check here when extending plugin to make sure no trade viewers are getting items
                        player.getOpenInventory().getTopInventory().setItem(48, moneyItem);
                    } else {
                        //Add a check here when extending plugin to make sure no trade viewers are getting items
                        player.getOpenInventory().getTopInventory().setItem(50, moneyItem);

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

    public ItemStack giveMoneyItem(double amount) {
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
     *
     * @param inventory inventory that you are looking for a slot in
     * @param slots     The available slots to put an item into
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
     *
     * @param inventory Inventory the trade is happening from
     */
    public void countdownTrade(Inventory inventory) {
        int delay = 0;
        int period = (core.config.getInt("tradeCountdownTime")) * 200;/*core.config.getInt("tradeCountdownTime");*/
        periodCount = period / 200;

        countdown = true;

        InventoryHolder holder = inventory.getHolder();

        Player tradeInitiator, tradeAccepter;
        if (!(holder instanceof CustomHolder))
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
                    if (periodCount == 0) {
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

        String msgTradeSuccess = parseStringMsg(core.config.getString("tradeSuccessMsg"));
        String prefix = parseStringMsg(core.config.getString("chatPrefix"));

        Player tradeInitiator, tradeAccepter;
        Inventory inv = inventory;

        InventoryHolder holder = inv.getHolder();

        if (!(holder instanceof CustomHolder))
            return;

        CustomHolder tradeInfo = (CustomHolder) holder;

        tradeAccepter = tradeInfo.getAccepter();
        tradeInitiator = tradeInfo.getInitiator();



        for (int a = 0; a < traderSlots.size(); a++) {
            if (inventory.getItem(traderSlots.get(a)) != null && (inventory.getItem(traderSlots.get(a)).getType() != Material.AIR)) {
                if (tradeAccepter.getInventory().firstEmpty() != -1) {
                    tradeAccepter.getInventory().addItem(inventory.getItem(traderSlots.get(a)));
                    inventory.removeItem(inventory.getItem(traderSlots.get(a)));
                } else {
                    if (Bukkit.isPrimaryThread()) {
                        tradeAccepter.getWorld().dropItem(tradeAccepter.getLocation(), inventory.getItem(traderSlots.get(a)));
                        inventory.removeItem(inventory.getItem(traderSlots.get(a)));
                    } else{
                        final ItemStack dropItem = inventory.getItem(traderSlots.get(a));
                        Bukkit.getScheduler().runTask(core, new Runnable() {
                            @Override
                            public void run() {
                                tradeAccepter.getWorld().dropItem(tradeAccepter.getLocation(), dropItem);
                            }
                        });
                        inventory.removeItem(inventory.getItem(traderSlots.get(a)));
                    }


                }
            }
        }
        for (int a = 0; a < accepterSlots.size(); a++) {
            if (inventory.getItem(accepterSlots.get(a)) != null && (inventory.getItem(accepterSlots.get(a)).getType() != Material.AIR)) {
                if (tradeInitiator.getInventory().firstEmpty() != -1) {
                    tradeInitiator.getInventory().addItem(inventory.getItem(accepterSlots.get(a)));
                    inventory.removeItem(inventory.getItem(accepterSlots.get(a)));
                } else {
                    if (Bukkit.isPrimaryThread()) {
                        tradeInitiator.getWorld().dropItem(tradeInitiator.getLocation(), inventory.getItem(accepterSlots.get(a)));
                        inventory.removeItem(inventory.getItem(accepterSlots.get(a)));
                    } else{
                        final ItemStack dropItem = inventory.getItem(accepterSlots.get(a));
                        Bukkit.getScheduler().runTask(core, new Runnable() {
                            @Override
                            public void run() {
                                tradeInitiator.getWorld().dropItem(tradeInitiator.getLocation(), dropItem);
                            }
                        });
                        inventory.removeItem(inventory.getItem(accepterSlots.get(a)));
                    }
                }
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
        tradeInitiator.sendMessage(prefix + msgTradeSuccess);
        tradeAccepter.sendMessage(prefix + msgTradeSuccess);
        tradeInfo.getInitiator().closeInventory();
        tradeInfo.getAccepter().closeInventory();
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        String prefix = parseStringMsg(core.config.getString("chatPrefix"));
        Player whoClosed = (Player) event.getPlayer();

        Inventory inv = whoClosed.getOpenInventory().getTopInventory();

        InventoryHolder holder = inv.getHolder();

        Player tradeInitiator, tradeAccepter;
        if (!(holder instanceof CustomHolder))
            return;

        CustomHolder tradeInfo = (CustomHolder) holder;

        tradeAccepter = tradeInfo.getAccepter();
        tradeInitiator = tradeInfo.getInitiator();

        if (whoClosed == tradeAccepter) {

            if ((tradeInitiator.isOnline()) && (tradeInitiator.getOpenInventory() != null) &&
                    (tradeInitiator.getOpenInventory().getTopInventory() != null) && (tradeInitiator.getOpenInventory().getTopInventory().equals(core.tradeGUI.tradeInventory)) && (tradingPlayers.containsKey(tradeInitiator))) {
                ItemStack cursor = tradeInitiator.getOpenInventory().getCursor();
                String msgTradeCancelled = parseStringMsg(core.config.getString("tradeCancelledMsg"), "{TRADECANCELLEDBY}:" + tradeAccepter.getName());
                tradeInitiator.getOpenInventory().setCursor(null);
                tradeInitiator.getInventory().addItem(new ItemStack[]{cursor});
                cursor = tradeAccepter.getOpenInventory().getCursor();
                tradeAccepter.getOpenInventory().setCursor(null);
                tradeAccepter.getInventory().addItem(new ItemStack[]{cursor});
                countdown = false;
                tradingPlayers.remove(tradeInitiator, tradeAccepter);
                tradeInitiator.closeInventory();
                tradeInitiator.updateInventory();
                returnItems(inv, tradeInitiator, tradeAccepter);
                tradeInitiator.sendMessage(prefix + msgTradeCancelled);
                tradeAccepter.sendMessage(prefix + msgTradeCancelled);
                return;
            }
        } else if (whoClosed == tradeInitiator) {
            if ((tradeAccepter.isOnline()) && (tradeAccepter.getOpenInventory() != null) &&
                    (tradeAccepter.getOpenInventory().getTopInventory() != null) && (tradeAccepter.getOpenInventory().getTopInventory().equals(core.tradeGUI.tradeInventory)) && (tradingPlayers.containsValue(tradeAccepter))) {
                ItemStack cursor = tradeAccepter.getOpenInventory().getCursor();
                String msgTradeCancelled = parseStringMsg(core.config.getString("tradeCancelledMsg"), "{TRADECANCELLEDBY}:" + tradeInitiator.getName());
                tradeAccepter.getOpenInventory().setCursor(null);
                tradeAccepter.getInventory().addItem(new ItemStack[]{cursor});
                cursor = tradeInitiator.getOpenInventory().getCursor();
                tradeInitiator.getOpenInventory().setCursor(null);
                tradeInitiator.getInventory().addItem(new ItemStack[]{cursor});
                countdown = false;
                tradingPlayers.remove(tradeInitiator, tradeAccepter);
                tradeAccepter.closeInventory();
                tradeAccepter.updateInventory();
                returnItems(inv, tradeInitiator, tradeAccepter);
                tradeInitiator.sendMessage(prefix + msgTradeCancelled);
                tradeAccepter.sendMessage(prefix + msgTradeCancelled);
                return;
            }
        }
        returnItems(inv, tradeInitiator, tradeAccepter);
        return;
    }

    /**
     * Replaces all custom variables
     *
     * @param msg          Msg to parse variables in
     * @param replacements Variables in config to be replaced with Convention: Variable:Replacement i.e. PLAYERTRADESENTTO:playerReceiver.getName()
     * @returnMsg with variables replaced
     */
    public String parseStringMsg(String msg, String... replacements) {
        String parsedMsg = msg;
        //Make second mthd w/o second param
        //replacements is like Variable:Replacement i.e. PLAYERTRADESENTTO:playerReceiver.getName()
        for (String s : replacements) {
            String[] replace = s.split(":");
            if (msg.contains(replace[0])) {
                parsedMsg = msg.replace(replace[0], replace[1]);
            }
        }
        parsedMsg = ChatColor.translateAlternateColorCodes('&', parsedMsg);

        return parsedMsg;
    }

    /**
     * Returns the items to the player in the trade
     *
     * @param inventory      Inventory that items are being returned from (Trade Inventory)
     * @param tradeInitiator Player who initiated the trade
     * @param tradeAccepter  Player who accepted the trade
     */
    public void returnItems(Inventory inventory, Player tradeInitiator, Player tradeAccepter) {

        if (inventory.getName().contains("Trade:")) {

            for (int a = 0; a < traderSlots.size(); a++) {
                if (inventory.getItem(traderSlots.get(a)) == null || inventory.getItem(traderSlots.get(a)).getType() == Material.AIR || inventory.getItem(traderSlots.get(a)).getAmount() == 0) {
                } else {
                    tradeInitiator.getInventory().addItem(inventory.getItem(traderSlots.get(a)));
                    inventory.removeItem(inventory.getItem(traderSlots.get(a)));
                }
            }
            for (int a = 0; a < accepterSlots.size(); a++) {
                if (inventory.getItem(accepterSlots.get(a)) == null || inventory.getItem(accepterSlots.get(a)).getType() == Material.AIR || inventory.getItem(accepterSlots.get(a)).getAmount() == 0) {
                } else {
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
