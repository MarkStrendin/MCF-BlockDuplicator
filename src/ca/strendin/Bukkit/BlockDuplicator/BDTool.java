package ca.strendin.Bukkit.BlockDuplicator;

import java.util.Hashtable;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BDTool {
    
    // Storage for player block data
    private static Hashtable<Player,Block> PlayerBlockStorage = new Hashtable<Player,Block>();    
    
    public final static void removePlayerFromStorageList(Player player) {
        if (PlayerBlockStorage.containsKey(player)) {
            PlayerBlockStorage.remove(player);
        }
    }
    
    public final static void dataSetterHandler(Player player, Block block) {
        
        if (BDCommands.isPaintableBlock(block)) {
        
            if (PlayerBlockStorage.containsKey(player)) {
                PlayerBlockStorage.remove(player);            
            }
            
            PlayerBlockStorage.put(player, block);
            
            BDLogging.logThis(player.getDisplayName() + " set their ink to " + block.getType() + " with data value " + block.getData());
            BDLogging.sendPlayer(player,"Ink set to: " + BDLogging.itemColor + block.getType());
        } else {
            BDLogging.sendPlayerError(player, "Sorry, that block cannot be copied");
        }
        
    
    }
    
    
    public final static void dataPasteHandler(Player player, Block block ) {
        
        if (PlayerBlockStorage.containsKey(player)) {
            
            /*
             *  If the block could not be copied, chances are things will end badly if it
             *  is possible to paste over it, so only allow pasting over block types that
             *  are copyable.
             * 
             */
            
            if (BDCommands.isPaintableBlock(block)) {            
                Block copiedBlock = PlayerBlockStorage.get(player);
                
                if (BDCommands.isPaintableBlock(copiedBlock)) {
                    /*
                    player.sendMessage("Would set this block to:");
                    player.sendMessage(" ID: " + copiedBlock.getType());
                    player.sendMessage(" Name: " + copiedBlock.getTypeId());
                    player.sendMessage(" Data: " + copiedBlock.getData());
                    player.sendMessage(" HashTable size: " + PlayerBlockStorage.size());
                    */
                    block.setType(copiedBlock.getType());
                    block.setData(copiedBlock.getData());
                }
            } else {
                BDLogging.sendPlayerError(player, "Sorry, that block cannot be overwritten with this tool");                
            }
            
        } else {
            BDLogging.sendPlayerError(player,"No data saved for you yet!");
        }


        
        
        
    }
    
    public final static void dataToolHandler(Player player, Block block) {
        dataToolHandler(player,block,false);
    }
    
    
    public final static void dataToolHandler(Player player, Block block, boolean reverse) {
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
        case 6: MaxData = 2; break;     // Saplings
        case 31: MaxData = 2; break;     // Tall Grass
        }
        
        // If the block ID was on the list, go ahead and cycle it's data
        if (MaxData >= 0) {
            int setDataTo = block.getData();
            
            if (reverse) {
                setDataTo--;                            
            } else {
                setDataTo++;                
            }
            
            if (setDataTo > MaxData) {
                setDataTo = 0;
            }
            
            if (setDataTo < 0) {
                setDataTo = MaxData;
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
            BDLogging.sendPlayerError(player,"Your inventory is full!");             
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
                BDLogging.sendPlayerError(player,"Duplicating that block is not allowed");
            }
            
        }
        
    }
    
}
