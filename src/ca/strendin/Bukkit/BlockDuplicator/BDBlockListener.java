package ca.strendin.Bukkit.BlockDuplicator;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRightClickEvent;

public class BDBlockListener extends BlockListener {    
    @SuppressWarnings("unused")
    private final BlockDuplicator plugin;
    
    public BDBlockListener(final BlockDuplicator plugin) {
        this.plugin = plugin;
    }

    public void onBlockDamage(BlockDamageEvent event) {        
        Player thePlayer = event.getPlayer();
        
        if ((BDPermissions.tool(event.getPlayer())) && (BDPermissions.dataTool(event.getPlayer()))) {
            // Check if the player is using the special tool
            if (thePlayer.getItemInHand().getTypeId() == BDCommands.DuplicatorTool) {
                event.setCancelled(true);
                BDTool.dataToolHandler(event.getPlayer(),event.getBlock());
            }
        }   
    }

    public void onBlockRightClick(BlockRightClickEvent event) {
        Player thePlayer = event.getPlayer();
        
        if ((BDPermissions.tool(event.getPlayer())) && (BDPermissions.duplicatorTool(event.getPlayer()))) {
            // Check if the player is using the special tool
            if (thePlayer.getItemInHand().getTypeId() == BDCommands.DuplicatorTool) {                
                BDTool.duplicatorToolHandler(event.getPlayer(), event.getBlock());
            }
                        
        }
        
                
    }
}
