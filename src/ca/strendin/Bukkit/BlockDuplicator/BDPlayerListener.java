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
                
                // This functionality has been moved to the data tool
                
            } else if (event.getPlayer().getItemInHand().getTypeId() == BDCommands.PaintBrushTool) {
                // Paintbrush tool            
                if (BDPermissions.canUsePaintbrushTool(event.getPlayer())) {
                    if (CuboidRegionHandler.canSetInkHere(event.getPlayer(), event.getClickedBlock())) {
                        event.setCancelled(true);
                        BDTool.dataSetterHandler(event.getPlayer(), event.getClickedBlock());                
                    } else {
                        BDLogging.sendPlayerError(event.getPlayer(), "Sorry, using that tool is not allowed in this area");
                    }
                }
            } else if (event.getPlayer().getItemInHand().getTypeId() == BDCommands.DataTool) {
                // Data tool
                
                if (BDPermissions.canUseDataTool(event.getPlayer())) {
                    if (CuboidRegionHandler.canDataCycleHere(event.getPlayer(), event.getClickedBlock())) {
                        event.setCancelled(true);
                        BDTool.dataToolHandler(event.getPlayer(),event.getClickedBlock());    
                    } else {
                        BDLogging.sendPlayerError(event.getPlayer(), "Sorry, using that tool is not allowed in this area");
                    }                    
                }
            } else if (event.getPlayer().getItemInHand().getTypeId() == BDCommands.RegionTool) { 
                if (BDPermissions.canManageRegions(event.getPlayer())) {
                    event.setCancelled(true);
                    CuboidRegionHandler.getRegionInfoHere(event.getPlayer(), event.getClickedBlock());                    
                }
            }
            
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getPlayer().getItemInHand().getTypeId() == BDCommands.DuplicatorTool) {
                // Duplicator tool
                if (BDPermissions.canUseDuplicatorTool(event.getPlayer())) {
                    if (CuboidRegionHandler.canDuplicateHere(event.getPlayer(), event.getClickedBlock())) {
                        event.setCancelled(true);
                        BDTool.duplicatorToolHandler(event.getPlayer(), event.getClickedBlock());
                    } else {
                        BDLogging.sendPlayerError(event.getPlayer(), "Sorry, using that tool is not allowed in this area");
                    }
                }                            
            } else if (event.getPlayer().getItemInHand().getTypeId() == BDCommands.PaintBrushTool) {                
                if (BDPermissions.canUsePaintbrushTool(event.getPlayer())) {
                    if (CuboidRegionHandler.canPaintHere(event.getPlayer(), event.getClickedBlock())) {
                        event.setCancelled(true);
                        BDTool.dataPasteHandler(event.getPlayer(), event.getClickedBlock());                
                    } else {
                        BDLogging.sendPlayerError(event.getPlayer(), "Sorry, using that tool is not allowed in this area");
                    }
                }
            } else if (event.getPlayer().getItemInHand().getTypeId() == BDCommands.DataTool) {
                if (BDPermissions.canUseDataTool(event.getPlayer())) {
                    if (CuboidRegionHandler.canDataCycleHere(event.getPlayer(), event.getClickedBlock())) {                     
                        event.setCancelled(true);
                        BDTool.dataToolHandler(event.getPlayer(),event.getClickedBlock(),true);                 
                    } else {
                        BDLogging.sendPlayerError(event.getPlayer(), "Sorry, using that tool is not allowed in this area");
                    }
                }
            } else if (event.getPlayer().getItemInHand().getTypeId() == BDCommands.RegionTool) { 
                if (BDPermissions.canManageRegions(event.getPlayer())) {
                    event.setCancelled(true);
                    CuboidRegionHandler.inputCoordinate(event.getPlayer(), event.getClickedBlock());                    
                }
            }
        }
    }
    
}
