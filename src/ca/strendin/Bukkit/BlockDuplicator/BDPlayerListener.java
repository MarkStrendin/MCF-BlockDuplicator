package ca.strendin.Bukkit.BlockDuplicator;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class BDPlayerListener extends PlayerListener {
    public static BlockDuplicator plugin;
    
    public BDPlayerListener(BlockDuplicator thisplugin) {
        plugin = thisplugin;
    }
    
    
    public void onPlayerQuit(PlayerQuitEvent event) {
        BDTool.removePlayerFromStorageList(event.getPlayer()); 
    }
            
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getPlayer().getItemInHand().getTypeId() == BDCommands.DuplicatorTool) {
                // Duplicator tool
            
                if (BDPermissions.canUseDataTool(event.getPlayer())) {
                    event.setCancelled(true);
                    BDTool.dataToolHandler(event.getPlayer(),event.getClickedBlock());
                }
                
            } else if (event.getPlayer().getItemInHand().getTypeId() == BDCommands.PaintBrushTool) {
                // Paintbrush tool
                
                if (BDPermissions.canUsePaintbrushTool(event.getPlayer())) {
                    event.setCancelled(true);
                    BDTool.dataSetterHandler(event.getPlayer(), event.getClickedBlock());                
                }
            }
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getPlayer().getItemInHand().getTypeId() == BDCommands.DuplicatorTool) {
                // Duplicator tool
                
                if (BDPermissions.canUseDuplicatorTool(event.getPlayer())) {
                    BDTool.duplicatorToolHandler(event.getPlayer(), event.getClickedBlock());
                }
                            
            } else if (event.getPlayer().getItemInHand().getTypeId() == BDCommands.PaintBrushTool) {
                
                if (BDPermissions.canUsePaintbrushTool(event.getPlayer())) {
                    BDTool.dataPasteHandler(event.getPlayer(), event.getClickedBlock());                
                }
            }  
        }
    }
    
}
