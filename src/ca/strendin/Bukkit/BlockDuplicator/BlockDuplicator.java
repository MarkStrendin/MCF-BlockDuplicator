package ca.strendin.Bukkit.BlockDuplicator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;



public class BlockDuplicator extends JavaPlugin {
    private final BDBlockListener blockListener = new BDBlockListener(this);
    

    
       
    
    @Override
    public void onDisable() {        
        System.out.println(this.getDescription().getName() + " disabled");
        
    }

    @Override
    public void onEnable() {  

        
        // Permissions
        BDPermissions.initPermissions(getServer());

        System.out.println(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " enabled");

        // Register events
        PluginManager pm = getServer().getPluginManager();        
        pm.registerEvent(Event.Type.BLOCK_RIGHTCLICKED, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_DAMAGED, blockListener, Priority.Normal, this);    
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args ) {
        if (sender instanceof Player) {
            
            // If the sender is a player
            Player requestplayer = (Player)sender;
        
            if ((commandLabel.equalsIgnoreCase("clearinv")) || (commandLabel.equalsIgnoreCase("ci"))) {
                if (BDPermissions.clearinv(requestplayer)) {
                    BDCommands.handleClearInvCmd(requestplayer);
                } else {                    
                    BDLogging.permDenyMsg(requestplayer);
                }
            } else if ((commandLabel.equalsIgnoreCase("more")) || (commandLabel.equalsIgnoreCase("m"))) {                
                if (BDPermissions.more(requestplayer)) {
                    BDCommands.handleMoreCmd(requestplayer,args);
                } else {                    
                    BDLogging.permDenyMsg(requestplayer);
                }
            } else if ((commandLabel.equalsIgnoreCase("pick")) || (commandLabel.equalsIgnoreCase("p"))) {                
                if (BDPermissions.pick(requestplayer)) {
                    BDCommands.handlePickCmd(requestplayer,args);
                } else {                    
                    BDLogging.permDenyMsg(requestplayer);
                }               
            } else if (commandLabel.equalsIgnoreCase("duper")) {                
                if (BDPermissions.tool(requestplayer)) {
                    BDCommands.givePlayerDuplicatorTool(requestplayer);
                } else {                    
                    BDLogging.permDenyMsg(requestplayer);
                }
            }
        } else {
            BDLogging.sendConsole("This command is designed for players only");            
        }
        
        return true;        
    }   
}
