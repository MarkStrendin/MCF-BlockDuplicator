package ca.strendin.Bukkit.BlockDuplicator;

/* import org.bukkit.block.Block; */
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


    /*
     * Left clicking a block 
     */
    public void onBlockDamage(BlockDamageEvent event) {        
        Player thePlayer = event.getPlayer();        
        

        if (thePlayer.getItemInHand().getTypeId() == BDCommands.DuplicatorTool) {
            // Duplicator tool
        
            // Check permissions before doing anything
            if (BDPermissions.canUseDataTool(event.getPlayer())) {
                event.setCancelled(true);
                BDTool.dataToolHandler(event.getPlayer(),event.getBlock());
            }
            
            
        } else if (thePlayer.getItemInHand().getTypeId() == BDCommands.PaintBrushTool) {
            // Paintbrush tool
            
            if (BDPermissions.canUsePaintbrushTool(thePlayer)) {
                event.setCancelled(true);
                BDTool.dataSetterHandler(event.getPlayer(), event.getBlock());                
            }
            
            
        }
        
        
        
        
    }

    /*
     * Right clicking a block (obviously)
     */
    public void onBlockRightClick(BlockRightClickEvent event) {
        Player thePlayer = event.getPlayer();
        
        
        if (thePlayer.getItemInHand().getTypeId() == BDCommands.DuplicatorTool) {
            // Duplicator tool
            
            if (BDPermissions.canUseDuplicatorTool(event.getPlayer())) {
                BDTool.duplicatorToolHandler(event.getPlayer(), event.getBlock());
            }
                        
        } else if (thePlayer.getItemInHand().getTypeId() == BDCommands.PaintBrushTool) {
            
            if (BDPermissions.canUsePaintbrushTool(thePlayer)) {
                BDTool.dataPasteHandler(event.getPlayer(), event.getBlock());                
            }
            
        }
        
                
    }
}
