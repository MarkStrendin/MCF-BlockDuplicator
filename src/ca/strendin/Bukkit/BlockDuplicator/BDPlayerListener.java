package ca.strendin.Bukkit.BlockDuplicator;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerEvent;


public class BDPlayerListener extends PlayerListener {
    public static BlockDuplicator plugin;
    
    public BDPlayerListener(BlockDuplicator thisplugin) {
        plugin = thisplugin;
    }
    
    
    public void onPlayerQuit(PlayerEvent event) {
        BDTool.removePlayerFromStorageList(event.getPlayer()); 
    }

    
}
