package ca.strendin.Bukkit.BlockDuplicator;

import java.util.Hashtable;

import org.bukkit.ChatColor;
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
        	if (!BDCommands.isItemOnBlacklist(block)) {        
	            if (PlayerBlockStorage.containsKey(player)) {
	                PlayerBlockStorage.remove(player);            
	            }
	            
	            PlayerBlockStorage.put(player, block);
	            
	            BDLogging.logThis(player.getDisplayName() + " set their ink to " + block.getType() + " with data value " + block.getData());
	            BDLogging.sendPlayer(player,"Ink set to: " + BDLogging.itemColor + block.getType());
	        } else {
	            BDLogging.sendPlayerError(player, "Sorry, that block cannot be copied (blacklisted)");
	        }
        } else {
            BDLogging.sendPlayerError(player, "Sorry, that block cannot be copied (unsafe)");
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
            	if (!BDCommands.isItemOnBlacklist(block)) {
	                Block copiedBlock = PlayerBlockStorage.get(player);
	                
	                if (BDCommands.isPaintableBlock(copiedBlock)) {
	                    block.setType(copiedBlock.getType());
	                    block.setData(copiedBlock.getData());
	                }
            	} else {
            		BDLogging.sendPlayerError(player, "Sorry, that block cannot be overwritten with this tool (blacklisted)");
            	}
            } else {
            	BDLogging.sendPlayerError(player, "Sorry, that block cannot be overwritten with this tool (unsafe)");                
            }
            
        } else {
            BDLogging.sendPlayerError(player,"No data saved for you yet!");
        }        
    }
    
    private final static String boolToYesOrNo(boolean thisBool) {
    	if (thisBool) {
    		return "Yes";    		
    	} else {
    		return "No";
    	}
    }
    
    public final static void infoToolHere(Player player, Block block) {
    	try {
	    	BDLogging.sendPlayer(player, "Info for block at " + block.getX() + "," + block.getY() + "," + block.getZ());    	
	    	BDLogging.sendPlayer(player, " Block name: " + ChatColor.WHITE + block.getType().name());
	    	BDLogging.sendPlayer(player, " Block type id: " + ChatColor.WHITE + block.getTypeId());
	    	BDLogging.sendPlayer(player, " Block data value: " + ChatColor.WHITE + block.getData());
	    	BDLogging.sendPlayer(player, " Flamable: " +ChatColor.WHITE + boolToYesOrNo(block.getType().isFlammable()));
	    	BDLogging.sendPlayer(player, " Paint tool safe: " + ChatColor.WHITE + boolToYesOrNo(BDCommands.isPaintableBlock(block)));
	    	BDLogging.sendPlayer(player, " Duplicator tool safe: " + ChatColor.WHITE + boolToYesOrNo(BDCommands.isDuplicatableBlock(block)));
	    	BDLogging.sendPlayer(player, " Blacklisted: " + ChatColor.WHITE + boolToYesOrNo(BDCommands.isItemOnBlacklist(block)));
	    	
    	} catch (Exception ex) {
    		BDLogging.sendPlayerError(player, "Error retreiving block data");    		
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
        	if (BDCommands.isDuplicatableBlock(block)) {
        		if (!BDCommands.isItemOnBlacklist(block)) {
                    
                    ItemStack tobegiven = new ItemStack(giveThisItemID);
                    tobegiven.setAmount(64);
                    if (BDCommands.isItemWithDataValue(giveThisItemID)) {
                        tobegiven.setDurability(block.getData());                        
                    }
                    
                    //tobegiven.setData(blockHit.getData());
                    
                    BDCommands.giveStack(player,tobegiven);
                    player.updateInventory();                    
                } else {
                    BDLogging.sendPlayerError(player,"Duplicating that block is not allowed (blacklisted)");
                }        		
        	} else {
                BDLogging.sendPlayerError(player,"Duplicating that block is not allowed (unsafe)");
            }
            
            
        }
        
    }
    
}
