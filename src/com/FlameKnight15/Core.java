package com.FlameKnight15;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;


public class Core extends JavaPlugin {

    File cFile = new File(getDataFolder(), "config.yml");
    public FileConfiguration config = YamlConfiguration.loadConfiguration(cFile);

    TradeListener tradeListener = new TradeListener(this);
    TradeGUI tradeGUI = new TradeGUI(tradeListener, this);
    static Economy econ;
    Trade trade = new Trade(this);
    CommandHandler commandHandler = new CommandHandler(this);

    String prefix;

    @Override
    public void onEnable(){
        this.saveDefaultConfig();
        config = YamlConfiguration.loadConfiguration(cFile);
        config.options().copyDefaults(true);
        System.out.println(config.toString());




        getLogger().info("onEnable has been initialized!");
        registerEvents(this, tradeListener);
        getCommand("trade").setExecutor(commandHandler);
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        prefix = parseStringMsg(config.getString("chatPrefix"));
    }

    @Override
    public void onDisable(){
        //config = YamlConfiguration.loadConfiguration(cFile);
        config.options().copyDefaults(true);
        for(Player player : tradeListener.tradingPlayers.keySet()){
            player.closeInventory();
            tradeListener.tradingPlayers.get(player).closeInventory();
        }


        getLogger().info("onDisable has been initialized!");
    }

    /**
     * Sets up the economy
     * @return
     */
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    /**
     * Replaces all custom variables
     * @param msg Msg to parse variables in
     * @return Msg with variables replaced
     */
    public String parseStringMsg(String msg){
        String parsedMsg = msg;
        //Make second mthd w/o second param
        //replacements is like Variable:Replacement i.e. PLAYERTRADESENTTO:playerReceiver.getName()
        return parsedMsg;
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

    /**
     * Translates the color codes of the text given
     * @param string
     * @return
     */
    public String parseColors(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Register events and listeners
     * @param plugin
     * @param listeners
     */
    public static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
}
