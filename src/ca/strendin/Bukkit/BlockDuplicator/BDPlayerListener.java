package ca.strendin.Bukkit.BlockDuplicator;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class BDPlayerListener implements Listener {
    public static BlockDuplicator plugin;

    public BDPlayerListener(BlockDuplicator thisplugin) {
        plugin = thisplugin;
    }
    
    private boolean isEntityHostile(EntityType thisEntity) {    	
    	if (
    			(thisEntity == EntityType.BLAZE) ||
    			(thisEntity == EntityType.CAVE_SPIDER) ||
    			(thisEntity == EntityType.CREEPER) ||
    			(thisEntity == EntityType.ENDERMAN) ||
    			(thisEntity == EntityType.GHAST) ||
    			(thisEntity == EntityType.GIANT) ||
    			(thisEntity == EntityType.PIG_ZOMBIE) ||
    			(thisEntity == EntityType.SILVERFISH) ||
    			(thisEntity == EntityType.SKELETON) ||
    			(thisEntity == EntityType.SLIME) ||
    			(thisEntity == EntityType.SPIDER) ||
    			(thisEntity == EntityType.WITCH) ||    			
    			(thisEntity == EntityType.ZOMBIE)    			
    			){
    		return true;
    	} else {
    		return false;
    	}
    }
    
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {    	
    	CuboidRegion thisRegion = CuboidRegionHandler.getRegionHere(event.getTo());
    	if (thisRegion != null) {
    		Player player = event.getPlayer();
    		
 		
    		// For announcing upon entry
    		
    		// Check to see if the player is just entering the region, so we don't spam them with messages while they
    		// are in the region
    		if (CuboidRegionHandler.getRegionHere(event.getFrom()) == null) {
    		
				if (thisRegion.canAnnounceOnEnter()) {
					BDLogging.sendPlayerInfo(player, "You are now entering the region \""+thisRegion.getName()+"\"");
				}
    		}
    		
    		// For checking to see if they are allowed in the region
    		
    		// Check to see if they are the owner of the region
    		
    		if (!thisRegion.canPlayersEnter()) {
				if (!thisRegion.getOwner().toLowerCase().contentEquals((player.getName().toLowerCase()))) {
					if (CuboidRegionHandler.getRegionHere(event.getFrom()) == thisRegion) {
						player.teleport(player.getLocation().getWorld().getSpawnLocation());
					} else {
						player.teleport(event.getFrom());
					}
					BDLogging.sendPlayerError(player, "Sorry, you are not allowed in the region \""+thisRegion.getName()+"\"");
				}
    		}    		
    	}
    }
    
    @EventHandler        
    public void onCreatureSpawnEvent(CreatureSpawnEvent event) {
        if (CuboidRegionHandler.getRegionHere(event.getLocation()) != null) {
        	if (isEntityHostile(event.getEntityType())) {
        		event.setCancelled(true);        		
        	}        		
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        BDTool.removePlayerFromStorageList(event.getPlayer()); 
    }
    
    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {    	
    	if (!CuboidRegionHandler.canBreakBlocksHere(event.getPlayer(), event.getBlock())) {
    		event.setCancelled(true);
    		BDLogging.sendPlayerError(event.getPlayer(), "Sorry, you can't place or break blocks here");
    	}    
    }
    
    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
    	if (!CuboidRegionHandler.canBreakBlocksHere(event.getPlayer(), event.getBlock())) {
    		event.setCancelled(true);
    		BDLogging.sendPlayerError(event.getPlayer(), "Sorry, you can't place or break blocks here");
    	}       
    }
    
    @EventHandler
    public void onEntityExplodeEvent(EntityExplodeEvent event) {
    	for (Block thisBlock : event.blockList()) {
    		CuboidRegion regionHere = CuboidRegionHandler.getRegionHere(thisBlock);
    		if (regionHere != null) {
    			if (!regionHere.canExplode()) {
    				event.setCancelled(true);
    			}
    		}
    	}
    }    
    
    @EventHandler        
    public void onPlayerInteract(PlayerInteractEvent event) { 
    	Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
        	if (player.getItemInHand().getTypeId() > 0) {
	        	ItemMeta itemInHandMeta = player.getItemInHand().getItemMeta();        	
	        	if (itemInHandMeta.hasDisplayName()) {
	        		if (itemInHandMeta.getDisplayName().contentEquals(BDCommands.paintbrushToolName)) {        		
	    				if (player.getItemInHand().getTypeId() == BDCommands.paintBrushToolID) {
	    					if (BDPermissions.canUsePaintbrushTool(player)) {
	                            if (CuboidRegionHandler.canSetInkHere(player, event.getClickedBlock())) {
	                                event.setCancelled(true);
	                                BDTool.dataSetterHandler(player, event.getClickedBlock());                
	                            } else {
	                                BDLogging.sendPlayerError(player, "Sorry, using that tool is not allowed in this area");
	                            }
	                        }    					
	    				}
	        		} else if (itemInHandMeta.getDisplayName().contentEquals(BDCommands.duplicatorToolName)) {
	        			if (player.getItemInHand().getTypeId() == BDCommands.duplicatorToolID) {
	        				if (BDPermissions.canUseDuplicatorTool(player)) {	                            
                                event.setCancelled(true);
                                BDTool.infoToolHere(player, event.getClickedBlock());
	                        }
	        			}
	        		} else if (itemInHandMeta.getDisplayName().contains(BDCommands.regionToolName)) {
	        			if (player.getItemInHand().getTypeId() == BDCommands.regionToolID) {
	       				 if (BDPermissions.canManageRegions(player)) {
		                        event.setCancelled(true);
		                        CuboidRegionHandler.getRegionInfoHere(player, event.getClickedBlock());                    
		                    }        				 
	       			 }
	        		} // tool name selection
	            } // does the item have a name?
        	} // is the type id > 0
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
        	if (player.getItemInHand().getTypeId() > 0) {
	        	ItemMeta itemInHandMeta = event.getPlayer().getItemInHand().getItemMeta();        	
	        	if (itemInHandMeta.hasDisplayName()) {
	        		if (itemInHandMeta.getDisplayName().contentEquals(BDCommands.duplicatorToolName)) {
	        			if (player.getItemInHand().getTypeId() == BDCommands.duplicatorToolID) {
	        				if (BDPermissions.canUseDuplicatorTool(player)) {
	                            if (CuboidRegionHandler.canDuplicateHere(player, event.getClickedBlock())) {
	                                event.setCancelled(true);
	                                BDTool.duplicatorToolHandler(player, event.getClickedBlock());
	                            } else {
	                                BDLogging.sendPlayerError(player, "Sorry, using that tool is not allowed in this area");
	                            }
	                        }
	        			}
	        		} else if (itemInHandMeta.getDisplayName().contains(BDCommands.paintbrushToolName)) {
	        			if (player.getItemInHand().getTypeId() == BDCommands.paintBrushToolID) {
	        				if (BDPermissions.canUsePaintbrushTool(player)) {
		                        if (CuboidRegionHandler.canPaintHere(player, event.getClickedBlock())) {
		                            event.setCancelled(true);
		                            BDTool.dataPasteHandler(player, event.getClickedBlock());                
		                        } else {
		                            BDLogging.sendPlayerError(player, "Sorry, using that tool is not allowed in this area");
		                        }
		                    }
	        			}	        		
	        		} else if (itemInHandMeta.getDisplayName().contentEquals(BDCommands.regionToolName)) {
	        			if (BDPermissions.canManageRegions(player)) {
	                        event.setCancelled(true);
	                        CuboidRegionHandler.inputCoordinate(player, event.getClickedBlock());                    
	                    }        			
	        		} // Checking for names        		
	        	} // Does the item have a name?        	
        	} // is the typeid > 0 (air?)
        } // right click or left click
    }
}
