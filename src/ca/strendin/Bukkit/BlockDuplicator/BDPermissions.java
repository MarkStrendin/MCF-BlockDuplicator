package ca.strendin.Bukkit.BlockDuplicator;

import org.bukkit.entity.Player;


public class BDPermissions {
        
    private static boolean hasPermission(Player thisplayer, String permName, boolean defaultPermission) {
        return thisplayer.hasPermission(permName);
    }
    
    public static boolean canUseMoreCommand(Player player) {
        return hasPermission(player,"blockduplicator.commands.more",true);         
    }
    
    public static boolean canUseClearInvCommand(Player player) {
        return hasPermission(player,"blockduplicator.commands.clearinv",true);
    }
    
    public static boolean canUsePickCommand(Player player) {
        return hasPermission(player,"blockduplicator.commands.pick",true);
    }

    /*
     * Access to the duplicator tool
     */
    public static boolean canUseDuplicatorTool(Player player) {
        return hasPermission(player,"blockduplicator.tools.duplicator",true);
    }
        
    /*
     * Access to the paintbrush tool
     */
    public static boolean canUsePaintbrushTool(Player player) {
        return hasPermission(player,"blockduplicator.tools.paintbrush",true);
    }
    
    /*
     * Access to the info tool
     */
    public static boolean canUseInfoTool(Player player) {
        return hasPermission(player,"blockduplicator.tools.info",true);
    }
    
    /*
     * Ability to reload the configuration file from the server
     */
    public static boolean canReload(Player player) {
        return hasPermission(player,"blockduplicator.reload",player.isOp());
    }
    
    /*
     * Ability to manage regions
     */
    public static boolean canManageRegions(Player player) {
        return hasPermission(player,"blockduplicator.region.manage",player.isOp());
    }
    
    /*
     * Ignore regions
     */
    public static boolean ignoresRegions(Player player) {
        return hasPermission(player,"blockduplicator.region.ignore",player.isOp());
    }
    
    
    
    
    
    
}
