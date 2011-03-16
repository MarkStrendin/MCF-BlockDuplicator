package ca.strendin.Bukkit.BlockDuplicator;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BDLogging {
    
    public static final Logger log = Logger.getLogger("Minecraft");
    
    private static ChatColor normalColor = ChatColor.AQUA;
    private static ChatColor errorColor = ChatColor.RED;
    
   

    public static void sendMsg(Player tothisplayer, String message) {
        tothisplayer.sendMessage(normalColor + message);        
    }
    
    public static void sendConsole(String message) {        
        System.out.println("[BlockDuplicator] " + message);
        log.info(message);
    }
    
    public static void errorMsg(Player tothisplayer, String message) {
        tothisplayer.sendMessage(errorColor  + message);
    }
    
    public static void permDenyMsg(Player tothisplayer) {
        tothisplayer.sendMessage(errorColor + "You do not have permission to use that command");        
    }
    
}
