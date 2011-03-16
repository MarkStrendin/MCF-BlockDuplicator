package ca.strendin.Bukkit.BlockDuplicator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockDuplicator extends JavaPlugin {
    
    private final BDBlockListener blockListener = new BDBlockListener(this);
    private final BDPlayerListener playerListener = new BDPlayerListener(this);
    
    
    private static String configFileName = "BlockDuplicator.config";  
    public static Properties configSettings = new Properties();
    
    @Override
    public void onLoad() {
        // This is called *before* onEnable
    }  
    
    @Override
    public void onDisable() {        
        System.out.println(this.getDescription().getName() + " disabled");
        
    }

    @Override
    public void onEnable() {
        
        // Permissions
        BDPermissions.initPermissions(getServer());

        // Load config file
        try {
            loadConfigFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            BDLogging.sendConsole("Config file could not be loaded!");
            
            try {
                createConfigFile();
            } catch (IOException e1) {
                BDLogging.sendConsole("A new config file could not be created!");
            }
        }
        
        System.out.println(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " enabled");

        // Register events
        PluginManager pm = getServer().getPluginManager();        
        pm.registerEvent(Event.Type.BLOCK_RIGHTCLICKED, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_DAMAGED, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, this.playerListener, Event.Priority.Normal, this);
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args ) {
        if (sender instanceof Player) {
            
            // If the sender is a player
            Player requestplayer = (Player)sender;
        
            if ((commandLabel.equalsIgnoreCase("clearinv")) || (commandLabel.equalsIgnoreCase("ci"))) {
                if (BDPermissions.canUseClearInvCommand(requestplayer)) {
                    BDCommands.handleClearInvCmd(requestplayer);
                } else {                    
                    BDLogging.permDenyMsg(requestplayer);
                }
            } else if ((commandLabel.equalsIgnoreCase("more")) || (commandLabel.equalsIgnoreCase("m"))) {                
                if (BDPermissions.canUseMoreCommand(requestplayer)) {
                    BDCommands.handleMoreCmd(requestplayer,args);
                } else {                    
                    BDLogging.permDenyMsg(requestplayer);
                }
            } else if ((commandLabel.equalsIgnoreCase("pick")) || (commandLabel.equalsIgnoreCase("p"))) {                
                if (BDPermissions.canUsePickCommand(requestplayer)) {
                    BDCommands.handlePickCmd(requestplayer,args);
                } else {                    
                    BDLogging.permDenyMsg(requestplayer);
                }               
            } else if ((commandLabel.equalsIgnoreCase("duper")) || (commandLabel.equalsIgnoreCase("duplicator"))) {                
                if (BDPermissions.canSummonDuplicator(requestplayer)) {
                    BDCommands.givePlayerDuplicatorTool(requestplayer);
                } else {                    
                    BDLogging.permDenyMsg(requestplayer);
                }               
            } else if ((commandLabel.equalsIgnoreCase("paintbrush")) || (commandLabel.equalsIgnoreCase("painter"))) {                
                if (BDPermissions.canSummonPaintbrush(requestplayer)) {
                    BDCommands.givePlayerPaintbrushTool(requestplayer);
                } else {                    
                    BDLogging.permDenyMsg(requestplayer);
                }               
            } else if ((commandLabel.equalsIgnoreCase("bdtools")) || (commandLabel.equalsIgnoreCase("bdt"))) {                
                if ((BDPermissions.canSummonPaintbrush(requestplayer)) && (BDPermissions.canSummonDuplicator(requestplayer))) {
                    BDCommands.givePlayerDuplicatorTool(requestplayer);
                    BDCommands.givePlayerPaintbrushTool(requestplayer);
                } else {                    
                    BDLogging.permDenyMsg(requestplayer);
                }                
            } else if (commandLabel.equalsIgnoreCase("blockduplicator")) {
                if (BDPermissions.canReload(requestplayer)) {
                    // Try to reload the config file
                    try {
                        loadConfigFile();
                        BDLogging.sendMsg(requestplayer,"BlockDuplicator config file reloaded!");
                    } catch (IOException e) {
                        BDLogging.sendMsg(requestplayer, ChatColor.RED + "IOException when attempting to reload the config file");                        
                    }
                } else {                    
                    BDLogging.permDenyMsg(requestplayer);
                }
            }
        } else {
            BDLogging.sendConsole("This command is designed for players only");            
        }
        
        return true;        
    }
    
    
    /*
     * Loads the config file data into appropriate places
     */
    public void loadConfigFile() throws IOException {
        BDCommands.denied_blocks.clear();
        configSettings.clear();
        
        FileInputStream fs = new FileInputStream(new File(this.getDataFolder(), configFileName));
    
        configSettings.load(fs);
        
        BDLogging.sendConsole("Config file loaded!");        
        
        // Default duplicator tool ID is 275
        try {
            BDCommands.DuplicatorTool = Integer.parseInt(configSettings.getProperty("duplicatortoolid","275").trim());
        } catch (Exception e) { BDLogging.sendConsole("duplicatortoolid was set to an insane value - check your config file"); }
        
        // Default paint brush tool is 341
        try {
            BDCommands.PaintBrushTool = Integer.parseInt(configSettings.getProperty("paintbrushtoolid","341").trim());
        } catch (Exception e) { BDLogging.sendConsole("paintbrushtoolid was set to an insane value - check your config file"); }
        
        /*
         * If the tools are set to the same item, use defaults instead
         */
        if (BDCommands.PaintBrushTool == BDCommands.DuplicatorTool) {
            BDLogging.sendConsole("Paintbrush tool and duplicator tool cannot be set to the same item");
            BDLogging.sendConsole(" using 275 for duplicator tool");
            BDCommands.DuplicatorTool = 275;
            BDLogging.sendConsole(" using 341 for paintbrush tool");
            BDCommands.PaintBrushTool = 341;            
        }
        
        /*
         * 7 - bedrock
         * 8,9 - water
         * 10,11 - lava
         * 51 - fire (if you can figure out a way to duplicate it in the first place)
         * 79 - ice (because I hate cleaning up after this when people break the blocks)
         */
        String defaultDeniedBlocks = "7,8,9,10,11,51,52,79";
        
        // Parse the item blacklist
        String splitMe = configSettings.getProperty("blacklist",defaultDeniedBlocks); 
        
        String workingString[] = splitMe.split(",");
        
        for (String thisString : workingString) {
            // Do some sanity checking on this to make sure it's a number
            try {
                if (Integer.parseInt(thisString.trim()) > 0) {
                    BDCommands.denied_blocks.add(Integer.parseInt(thisString.trim()));    
                }
            } catch (Exception e) { /* Do nothing - it just won't add the invalid number to the blacklist */}            
        }
        
        fs.close();
    }
    
    
    /*
     * If the config file doesn't exist already, this attempts to create it
     */
    public final void createConfigFile() throws IOException {
        
        BDLogging.sendConsole("Attempting to create config file...");
        // Check to see if the directory is there
        
        File pluginDirectory = this.getDataFolder(); 
        
        if (pluginDirectory.exists() != true) {        
            pluginDirectory.mkdir();           
        }
        
        File configFile = new File(pluginDirectory, configFileName);
        
        if (configFile.exists() != true) {
            configFile.createNewFile();
        }
        
        try {
            BDCommands.writeDefaultConfigFile(configFile);
            BDLogging.sendConsole("Successfully created new config file");
            loadConfigFile();
        } catch (Exception e) {
            BDLogging.sendConsole("Could not write to config file!");
        }
    }


}
