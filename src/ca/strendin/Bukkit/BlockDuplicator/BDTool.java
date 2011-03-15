package ca.strendin.Bukkit.BlockDuplicator;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BDTool {

    public final static void dataToolHandler(Player player, Block block) {
        //cBlock blockHit = event.getBlock();
        
        // Check if this is a block that we want to modify
        
        int MaxData = -1;
        switch (block.getTypeId()) {
        case 44: MaxData = 3; break;    // Double steps
        case 43: MaxData = 3; break;    // Single steps
        case 17: MaxData = 2; break;    // Logs
        case 35: MaxData = 15; break;   // Wool
        case 53: MaxData = 3; break;    // Wooden stairs
        case 67: MaxData = 3; break;    // Cobblestone stairs
        case 18: MaxData = 2; break;    // Leaves
        case 86: MaxData = 3; break;    // Pumpkins (changes direction)
        case 91: MaxData = 3; break;    // Jack-o-lanterns (changes direction)
        }
        
        // If the block ID was on the list, go ahead and cycle it's data
        if (MaxData > 0) {
            int setDataTo = block.getData();
            setDataTo++;
            if (setDataTo > MaxData) {
                setDataTo = 0;
            }
            byte setDataToByte = (byte) (setDataTo & 0xFF);         
            
            //thePlayer.sendMessage("Setting data to " + setDataTo + "(" + setDataToByte + "/" + MaxData  + ")");
            block.setData(setDataToByte);
        }
    }
    
    
    @SuppressWarnings("deprecation")
    public final static void duplicatorToolHandler(Player player, Block block) {
        //Block blockHit = event.getBlock();
        
        // Translate the ID of the block that was hit into the item number that we want to give the player
        int giveThisItemID = BDCommands.translateBlockToItemID(block.getTypeId());            
        
        
        if (player.getInventory().firstEmpty() < 0) {
            player.sendMessage("Your inventory is full!");             
        } else {
            // Check if this item is allowed to be duplicated
            if (BDCommands.ThisItemAllowed(block.getTypeId())) {
                
                ItemStack tobegiven = new ItemStack(giveThisItemID);
                tobegiven.setAmount(64);
                if (BDCommands.isItemWithDataValue(giveThisItemID)) {
                    tobegiven.setDurability(block.getData());                        
                }
                
                //tobegiven.setData(blockHit.getData());
                
                BDCommands.giveStack(player,tobegiven);
                player.updateInventory();                    
            } else {
                player.sendMessage("Duplicating that block is not allowed");
            }
            
        }
        
    }
    
}
