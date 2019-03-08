package ca.strendin.Bukkit.BlockDuplicator.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.strendin.Bukkit.BlockDuplicator.BDLogging;
import ca.strendin.Bukkit.BlockDuplicator.BDPermissions;
import ca.strendin.Bukkit.BlockDuplicator.BlockDuplicator;
import ca.strendin.Bukkit.BlockDuplicator.CuboidRegion;
import ca.strendin.Bukkit.BlockDuplicator.CuboidRegionHandler;

public class bdregionCommand implements CommandExecutor {
    private final BlockDuplicator plugin;
    
    public bdregionCommand(BlockDuplicator plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player requestplayer = (Player)sender;
            
            if (BDPermissions.canManageRegions(requestplayer)) {                                    
                if (args.length > 0) {
                    String param = args[0].toLowerCase();                
                    if (param.toLowerCase().equals("create")) {
                        HandleCreateCommand(requestplayer,args);
                    } else if (param.toLowerCase().equals("flag")) {
                        HandleFlagCommand(requestplayer,args);                    
                    } else if (param.toLowerCase().equals("list")) {
                        HandleListCommand(requestplayer,args);                    
                    } else if (param.toLowerCase().equals("remove")) {
                        HandleRemoveCommand(requestplayer,args);               
                    } else if (param.toLowerCase().equals("load")) {
                        HandleLoadCommand(requestplayer,args);
                    } else if (param.toLowerCase().equals("save")) {
                        HandleSaveCommand(requestplayer,args);
                    } else if (param.toLowerCase().equals("setowner")) {
                        HandleSetOwnerCommand(requestplayer,args);
                    } else if (param.toLowerCase().equals("info")) {
                        HandleInfoCommand(requestplayer,args);
                    }
                } else {
                    BDLogging.sendPlayer(requestplayer, "/bdregion");
                    BDLogging.sendPlayerInfo(requestplayer, "    create <name>");
                    BDLogging.sendPlayerInfo(requestplayer, "    info <region name>");                    
                    BDLogging.sendPlayerInfo(requestplayer, "    remove <region name>");
                    BDLogging.sendPlayerInfo(requestplayer, "    list");                
                    BDLogging.sendPlayerInfo(requestplayer, "    save");
                    BDLogging.sendPlayerInfo(requestplayer, "    load");
                    BDLogging.sendPlayerInfo(requestplayer, "    setowner <region name> <new owner name>");
                    BDLogging.sendPlayerInfo(requestplayer, "    flag <region name> <flag>");
                    BDLogging.sendPlayerInfo(requestplayer, "    flags: duplicate, datacycle, setink, paint, break, explode");
                    BDLogging.sendPlayerInfo(requestplayer, "           players, enemies, announce");
                }
            }
        } else {
            BDLogging.sendConsoleOnly("This command is designed for players only, sorry");
        }
        return true;
    }
    
    private void HandleSaveCommand(Player player, String[] args) {
        CuboidRegionHandler.saveAllRegions();
    }
    
    private void HandleListCommand(Player player, String[] args) {
        CuboidRegionHandler.listRegions(player);        
    }
    
    private void HandleSetOwnerCommand(Player player, String[] args) {
    	
    	for (String thisArg : args) {
    		BDLogging.sendPlayerError(player, thisArg);
    	}
    	    	
    	
    	
    	// Make sure the player entered a name
        if (args.length == 3) {
            CuboidRegionHandler.setRegionOwner(player, args[1], args[2]);                            
        } else {
            BDLogging.sendPlayerError(player, "Region name and new owner required");
            BDLogging.sendPlayerError(player, "Usage: /bdregion setowner <region name> <new owner name>");
        }
        
    	
    	CuboidRegionHandler.saveAllRegions();    
    }
    
    private void HandleLoadCommand(Player play, String[] args) {
        CuboidRegionHandler.initRegions(plugin);
    }
    
    private void HandleRemoveCommand(Player player, String[] args) {
     // Make sure the player entered a name
        if (args.length > 1) {
            CuboidRegionHandler.removeRegion(player,args[1]);                            
        } else {
            BDLogging.sendPlayerError(player, "Region name required");
            BDLogging.sendPlayerError(player, "Usage: /bdregion remove <name>");
        }
    }
    
    private void HandleFlagCommand(Player player, String[] args) {
        if (args.length > 1) {
            if (args.length > 2) {
                String regionName = args[1];
                CuboidRegion specifiedRegion = CuboidRegionHandler.getRegionByName(regionName);
                
                if (specifiedRegion != null) {                    
                    String flagName = args[2];                    
                    if (flagName.equals("duplicate")) {
                        if (specifiedRegion.canDuplicate()) {
                            specifiedRegion.setCanDuplicate(false);
                            BDLogging.sendPlayerInfo(player, "Region \""+regionName+"\" will no longer allow duplication");
                        } else {
                            specifiedRegion.setCanDuplicate(true);
                            BDLogging.sendPlayerInfo(player, "Region \""+regionName+"\" will now allow duplication");                            
                        } 
                        CuboidRegionHandler.saveAllRegions();
                    } else if (flagName.equals("datacycle")) {
                        if (specifiedRegion.canDataCycle()) {
                            specifiedRegion.setCanDataCycle(false);
                            BDLogging.sendPlayerInfo(player, "Region \""+regionName+"\" will no longer allow data scrolling");
                        } else {
                            specifiedRegion.setCanDataCycle(true);
                            BDLogging.sendPlayerInfo(player, "Region \""+regionName+"\" will now allow data scrolling");                            
                        }                        
                        CuboidRegionHandler.saveAllRegions();
                    } else if (flagName.equals("setink")) {
                        if (specifiedRegion.canStorePaint()) {
                            specifiedRegion.setCanStorePaint(false);
                            BDLogging.sendPlayerInfo(player, "Region \""+regionName+"\" will no longer allow setting ink");
                        } else {
                            specifiedRegion.setCanStorePaint(true);
                            BDLogging.sendPlayerInfo(player, "Region \""+regionName+"\" will now allow setting ink");                            
                        }                        
                        CuboidRegionHandler.saveAllRegions();
                    } else if (flagName.equals("break")) {
                        if (specifiedRegion.canBreakBlocks()) {
                            specifiedRegion.setCanBreakBlocks(false);
                            BDLogging.sendPlayerInfo(player, "Region \""+regionName+"\" will no longer allow breaking blocks");
                        } else {
                            specifiedRegion.setCanBreakBlocks(true);
                            BDLogging.sendPlayerInfo(player, "Region \""+regionName+"\" will now allow breaking blocks");                            
                        }                        
                        CuboidRegionHandler.saveAllRegions();
                    } else if (flagName.equals("explode")) {
                        if (specifiedRegion.canExplode()) {
                            specifiedRegion.setCanExplode(false);
                            BDLogging.sendPlayerInfo(player, "Region \""+regionName+"\" will no longer allow explosion damage");
                        } else {
                            specifiedRegion.setCanExplode(true);
                            BDLogging.sendPlayerInfo(player, "Region \""+regionName+"\" will now allow explosion damage");                            
                        }                        
                        CuboidRegionHandler.saveAllRegions();
                        
                    } else if (flagName.equals("players")) {
                        if (specifiedRegion.canPlayersEnter()) {
                            specifiedRegion.setCanPlayersEnter(false);
                            BDLogging.sendPlayerInfo(player, "Region \""+regionName+"\" will no longer allow other players to enter");
                        } else {
                            specifiedRegion.setCanPlayersEnter(true);
                            BDLogging.sendPlayerInfo(player, "Region \""+regionName+"\" will now allow other players to enter");                            
                        }                        
                        CuboidRegionHandler.saveAllRegions();
                    } else if (flagName.equals("enemies")) {
                        if (specifiedRegion.canEnemyMobsSpawnHere()) {
                            specifiedRegion.setCanEnemyMobsSpawnHere(false);
                            BDLogging.sendPlayerInfo(player, "Region \""+regionName+"\" will no longer allow enemies to spawn");
                        } else {
                            specifiedRegion.setCanEnemyMobsSpawnHere(true);
                            BDLogging.sendPlayerInfo(player, "Region \""+regionName+"\" will now allow enemies to spawn");                            
                        }                        
                        CuboidRegionHandler.saveAllRegions();
                    } else if (flagName.equals("announce")) {
                        if (specifiedRegion.canAnnounceOnEnter()) {
                            specifiedRegion.setAnnounceOnEnter(false);
                            BDLogging.sendPlayerInfo(player, "Region \""+regionName+"\" will no longer announce upon entry");
                        } else {
                            specifiedRegion.setAnnounceOnEnter(true);
                            BDLogging.sendPlayerInfo(player, "Region \""+regionName+"\" will now announce upon entry");                            
                        }                        
                        CuboidRegionHandler.saveAllRegions();                        
                    } else if (flagName.equals("paint")) {
                        if (specifiedRegion.canApplyPaint()) {
                            specifiedRegion.setCanApplyPaint(false);
                            BDLogging.sendPlayerInfo(player, "Region \""+regionName+"\" will no longer applying ink");
                        } else {
                            specifiedRegion.setCanApplyPaint(true);
                            BDLogging.sendPlayerInfo(player, "Region \""+regionName+"\" will now allow applying ink");                            
                        }                        
                        CuboidRegionHandler.saveAllRegions();
                    } else {
                    	BDLogging.sendPlayerError(player, "Flag name not valid");
                        BDLogging.sendPlayerError(player, " Valid flags: duplicate, datacycle, setink, paint, break,");
                        BDLogging.sendPlayerError(player, "  explode, players, enemies, announce");
                    }
                } else {
                    BDLogging.sendPlayerError(player, "Region \""+regionName+"\" not found");
                }
            } else {
                BDLogging.sendPlayerError(player, "Flag name required (duplicate,datacycle,setink,paint)");                                
            }                                                        
        } else {
            BDLogging.sendPlayerError(player, "Region name required");
            BDLogging.sendPlayerError(player, "Usage: /bdregion flag <region name> <flag name>");
        }
    }   
    
    private void HandleInfoCommand(Player player, String[] args) {
        if (args.length > 1) {
            String regionName = args[1];
            CuboidRegion specifiedRegion = CuboidRegionHandler.getRegionByName(regionName);
            
            if (specifiedRegion != null) {
                CuboidRegionHandler.sendRegionInfo(player,specifiedRegion);
            } else {
                BDLogging.sendPlayerError(player, "Region \""+regionName+"\" not found");
            }
        } else {
            BDLogging.sendPlayerError(player, "Region name required");
            BDLogging.sendPlayerError(player, "Usage: /bdregion info <name>");
        }
    }
    
    private void HandleCreateCommand(Player player, String[] args) {
        if (args.length > 1) {
            CuboidRegionHandler.createRegion(player,args[1]);                            
        } else {
            BDLogging.sendPlayerError(player, "Region name required");
            BDLogging.sendPlayerError(player, "Usage: /bdregion create <name>");
        }
    }
    

}
