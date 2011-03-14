package ca.strendin.Bukkit.BlockDuplicator;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRightClickEvent;
import org.bukkit.inventory.ItemStack;


public class BDBlockListener extends BlockListener {
    private final BlockDuplicator plugin;
    
    public BDBlockListener(final BlockDuplicator plugin) {
        this.plugin = plugin;
    }
    
    
    // Modify block data, but only for certain blocks, so we don't bugger anything up
    public void onBlockDamage(BlockDamageEvent event) {        
        Player thePlayer = event.getPlayer();
        
        if ((BDPermissions.tool(event.getPlayer())) && (BDPermissions.dataTool(event.getPlayer()))) {
            // Check if the player is using the special tool
            if (thePlayer.getItemInHand().getTypeId() == plugin.DuplicatorTool) {
                event.setCancelled(true);
                
                Block blockHit = event.getBlock();
                
                           
                // Check if this is a block that we want to modify
                
                int MaxData = -1;
                switch (blockHit.getTypeId()) {
                case 44: MaxData = 3; break;
                case 43: MaxData = 3; break;
                case 17: MaxData = 2; break;
                case 35: MaxData = 15; break;   
                case 53: MaxData = 3; break;   
                case 67: MaxData = 3; break;
                case 18: MaxData = 2; break;
                }
                
                // If the block ID was on the list, go ahead and cycle it's data
                if (MaxData > -1) {
                    int setDataTo = blockHit.getData();
                    setDataTo++;
                    if (setDataTo > MaxData) {
                        setDataTo = 0;
                    }
                    byte setDataToByte = (byte) (setDataTo & 0xFF);         
                    
                    //thePlayer.sendMessage("Setting data to " + setDataTo + "(" + setDataToByte + "/" + MaxData  + ")");
                    blockHit.setData(setDataToByte);
                }
            } 
            
        }
        
           
    }

    
    @SuppressWarnings("deprecation")
    public void onBlockRightClick(BlockRightClickEvent event) {
        Player thePlayer = event.getPlayer();
        
        if ((BDPermissions.tool(event.getPlayer())) && (BDPermissions.duplicatorTool(event.getPlayer()))) {
            // Check if the player is using the special tool
            if (thePlayer.getItemInHand().getTypeId() == plugin.DuplicatorTool) {
                
                Block blockHit = event.getBlock();
                
                // Translate the ID of the block that was hit into the item number that we want to give the player
                int giveThisItemID = plugin.translateBlockToItemID(blockHit.getTypeId());            
                
                
                if (thePlayer.getInventory().firstEmpty() < 0) {
                    thePlayer.sendMessage("Your inventory is full!");             
                } else {
                    // Check if this item is allowed to be duplicated
                    if (plugin.ThisItemAllowed(blockHit.getTypeId())) {
                        
                        ItemStack tobegiven = new ItemStack(giveThisItemID);
                        tobegiven.setAmount(64);
                        if (plugin.isItemWithDataValue(giveThisItemID)) {
                            tobegiven.setDurability(blockHit.getData());                        
                        }
                        
                        //tobegiven.setData(blockHit.getData());
                        
                        plugin.giveStack(thePlayer,tobegiven);
                        thePlayer.updateInventory();                    
                    } else {
                        thePlayer.sendMessage("Duplicating that block is not allowed");
                    }
                    
                }
            }
                        
        }
        
                
    }
}
