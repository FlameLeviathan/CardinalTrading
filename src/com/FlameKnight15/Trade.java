package com.FlameKnight15;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Trade {


    Core core;

    public Trade(Core c) {
        core = c;
    }

    HashMap<Player, Player> requestTrade = new HashMap<Player, Player>();
    HashMap<String, Timer> tradeTimer = new HashMap<>();


    /**
     * Gets nearby players in a radius
     *
     * @param player
     * @return
     */
    public ArrayList<Player> getNearbyPlayers(Player player) {
        double radius = core.config.getDouble("tradeRadius");
        ArrayList<Player> nearbyPlayers = new ArrayList<>();
        for (Entity e : player.getNearbyEntities(radius, radius, radius)) {
            if (e instanceof Player) {
                nearbyPlayers.add((Player) e);
            }
        }
        return nearbyPlayers;
    }

    /**
     * Checks if a player has a trade request
     *
     * @param player
     * @return
     */
    public boolean hasTradeRequest(Player player) {
        if (requestTrade.containsValue(player)) {
            return true;
        }
        //Sort through hashmap Hashmap<Player, Player> tradeRequests (Will be second player variable = Value of key)
        return false;
    }

    /**
     * Gets the trade requests
     *
     * @param player
     * @return
     */
    public ArrayList<Player> getTradeRequests(Player player) {
        //Sort through hashmap Hashmap<Player, Player> tradeRequests (Will be second player variable = Value of key)
        //Might crash if multiple trades are out
        ArrayList<Player> tradeRequesters = new ArrayList<>();
        for (int i = 0; i <= requestTrade.size(); i++) {
            if (requestTrade.containsKey(player)) {
                tradeRequesters.add(requestTrade.get(player));
            }
        }

        return tradeRequesters;
    }

    /**
     * Sends a trade request from the tradeInitiator to the tradeReceiver
     *
     * @param tradeInitiator
     * @param tradeReceiver
     */
    public void sendTradeRequest(Player tradeInitiator, Player tradeReceiver) {
        String msgTradeSent = parseStringMsg(core.config.getString("tradeSentMsg"), "{PLAYERTRADESENTTO}:" + tradeReceiver.getName());
        String msgTradeAlreadySent = parseStringMsg(core.config.getString("tradeRequestAlreadySentMsg"), "{PLAYERTRADESENTTO}:" + tradeReceiver.getName());
        String msgTradeReceived = parseStringMsg(core.config.getString("tradeReceivedMsg"), "{PLAYERTRADESENTFROM}:" + tradeInitiator.getName());
        String msgCannotTradeWithSelf = parseStringMsg(core.config.getString("cannotTradeWithSelfMsg"));
        String prefix = parseStringMsg(core.config.getString("chatPrefix"));
        if (tradeInitiator != tradeReceiver) {
            if (getNearbyPlayers(tradeInitiator).contains(tradeReceiver)) {
                if(!getTradeRequests(tradeInitiator).contains(tradeReceiver) /*&& !(tradeTimer.containsKey(tradeInitiator) && tradeTimer.get(tradeInitiator).equals(tradeReceiver))*/) {

                    //Send messages to players informing about trade
                    tradeInitiator.sendMessage(prefix + msgTradeSent);
                    tradeReceiver.sendMessage(prefix + msgTradeReceived);
                    //Add to Hashmap Trade requests tI is 0 tR is 1
                    requestTrade.put(tradeInitiator, tradeReceiver);
                    tradeTimer(tradeInitiator, tradeReceiver);
                    // tradeInitiator.sendMessage(requestTrade.toString());
                } else{
                    tradeInitiator.sendMessage(prefix + msgTradeAlreadySent);
                }
            }
        } else {
            tradeInitiator.sendMessage(prefix + msgCannotTradeWithSelf);
        }
    }



    public void tradeTimer(Player tradeInitiator, Player tradeReceiver){
        int delay = (core.config.getInt("tradeRequestTimeout") * 1000);
        int period = 1000;/*core.config.getInt("tradeCountdownTime");*/
        String msgTradeRequestExpired = parseStringMsg(core.config.getString("tradeRequestExpiredMsg"),"{PLAYERTRADESENTTO}:" + tradeReceiver.getName());
        String prefix = parseStringMsg(core.config.getString("chatPrefix"));
        Timer timer;
        if(tradeTimer.containsKey(tradeInitiator+":"+tradeReceiver)){
            //if(tradeTimer.get(tradeInitiator) == tradeReceiver){
                timer = tradeTimer.get(tradeInitiator+":"+tradeReceiver);
                timer.cancel();
                tradeTimer.remove(tradeInitiator+":"+tradeReceiver);

                timer = new Timer();
                tradeTimer.put(tradeInitiator + ":"+tradeReceiver, timer);
                tradeCountdownTimer(tradeInitiator, tradeReceiver, delay, period, msgTradeRequestExpired, prefix, timer);
            /*} else{
                tradeTimer.put(tradeInitiator, tradeReceiver);
                tradeCountdownTimer(tradeInitiator, tradeReceiver, delay, period, msgTradeRequestExpired, prefix, timer);
            }*/
        } else {
            timer = new Timer();
            tradeTimer.put(tradeInitiator+":"+tradeReceiver, timer);
            tradeCountdownTimer(tradeInitiator, tradeReceiver, delay, period, msgTradeRequestExpired, prefix, timer);
        }





    }

    private void tradeCountdownTimer(Player tradeInitiator, Player tradeReceiver, int delay, int period, String msgTradeRequestExpired, String prefix, Timer timer) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!tradeInitiator.getOpenInventory().getTopInventory().equals(core.tradeGUI.tradeInventory)) {
                    if(tradeTimer.containsKey(tradeInitiator+":"+tradeReceiver))
                        if(tradeTimer.get(tradeInitiator+":"+tradeReceiver) == timer) {
                            tradeTimer.remove(tradeInitiator+":"+tradeReceiver);
                            requestTrade.remove(tradeInitiator, tradeReceiver);
                            tradeInitiator.sendMessage(prefix + msgTradeRequestExpired);
                            timer.cancel();
                        }
                } else{
                    tradeTimer.remove(tradeInitiator+":"+tradeReceiver);
                    requestTrade.remove(tradeInitiator, tradeReceiver);
                    timer.cancel();
                }
            }
        }, delay, period);
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
     * Initiates a trade between two players
     *
     * @param tradeInitiator
     * @param tradeReceiver
     */
    public void initiateTrade(Player tradeInitiator, Player tradeReceiver) {
        String msgTradeStarted = parseStringMsg(core.config.getString("tradeHasStartedMsg")); //Add more variables to this msg
        String prefix = parseStringMsg(core.config.getString("chatPrefix"));

        //Send messages to players informing about trade
        tradeInitiator.sendMessage(prefix + msgTradeStarted);
        tradeReceiver.sendMessage(prefix + msgTradeStarted);
        //Remove tI and tR from hashmap
        requestTrade.remove(tradeInitiator, tradeReceiver);

        //Open GUI trade
        TradeGUI tradeGUI = core.tradeGUI;
        tradeGUI.openTrade(tradeInitiator, tradeReceiver);
    }

    public void removeTradeRequest(Player tradeInitiator, Player tradeReceiver) {
        requestTrade.remove(tradeInitiator, tradeReceiver);
    }

    //Get trade gui methods here rather than take these methods over to TradeGUI
}
