package com.FlameKnight15;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
    Core core;


    public CommandHandler(Core c){
        core = c;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

        String prefix = parseStringMsg(core.config.getString("chatPrefix"));
        double tradeRadius = core.config.getDouble("tradeRadius");
        String msgPlayerNotOnline = parseStringMsg(core.config.getString("playerNotOnlineMsg"));
        String msgPlayerNotNearby = parseStringMsg(core.config.getString("playerNotNearbyMsg"));
        String msgCommandIncorrect = parseStringMsg(core.config.getString("commandIncorrectMsg"));


        if(sender instanceof Player){
            Player player = (Player) sender;
            //player.sendMessage(cmd.getName() + " " + cmd.toString());
            if(cmd.getName().equalsIgnoreCase("trade")){
                if (args.length == 1){
                    if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
                        Player tradeWith = Bukkit.getPlayer(args[0]);
                        if (core.trade.getNearbyPlayers(player).contains(tradeWith)) {
                            //If they have a trade request go into trade if not, send a request
                            //Check to make sure the player is still online when the trade is accepted
                            //If not online remove from hashmap
                            if (core.trade.hasTradeRequest(player)){
                                if(core.trade.getTradeRequests(tradeWith).contains(player)){
                                    core.trade.initiateTrade(tradeWith, player);
                                }else {
                                    core.trade.sendTradeRequest(player, tradeWith);
                                }
                            }else {
                                core.trade.sendTradeRequest(player, tradeWith);
                            }
                        } else {
                            sender.sendMessage(prefix + msgPlayerNotNearby);
                        }
                    } else{
                        sender.sendMessage(prefix + msgPlayerNotOnline);
                    }
                } else{
                    player.sendMessage(prefix + msgCommandIncorrect);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Replaces all custom variables
     * @param msg Msg to parse variables in
     * @param replacements Variables in config to be replaced with Convention: Variable:Replacement i.e. PLAYERTRADESENTTO:playerReceiver.getName()
     * @returnMsg with variables replaced
     */
    public String parseStringMsg(String msg, String... replacements){
        String parsedMsg = msg;
        //Make second mthd w/o second param
        //replacements is like Variable:Replacement i.e. PLAYERTRADESENTTO:playerReceiver.getName()
        for(String s : replacements){
            String[] replace = s.split(":");
            if(msg.contains(replace[0])){
                parsedMsg = msg.replace(replace[0], replace[1]);
            }
        }
        parsedMsg = ChatColor.translateAlternateColorCodes('&', parsedMsg);

        return parsedMsg;
    }
}
