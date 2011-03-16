package ca.strendin.Bukkit.BlockDuplicator;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BDLogging {
    
    public static final Logger log = Logger.getLogger("Minecraft");
    
    private static ChatColor normalColor = ChatColor.DARK_GREEN;
    private static ChatColor errorColor = ChatColor.RED;
    
   
    public static void sendPlayer(Player tothisplayer, String message) {
        tothisplayer.sendMessage(normalColor + message);        
    }
    
    public static void logThis(String message) {
        log.info("[BlockDuplicator] " + message);
    }
    
    
    public static void sendConsole(String message) {
        logThis(message);        
    }
    
    public static void sendConsoleOnly(String message) {
        System.out.println(message);
    }
    
    
    public static void sendPlayerError(Player tothisplayer, String message) {
        tothisplayer.sendMessage(errorColor  + message);
    }
    
    public static void permDenyMsg(Player tothisplayer) {
        tothisplayer.sendMessage(errorColor + "You do not have permission to use that command");        
    }
    
}
