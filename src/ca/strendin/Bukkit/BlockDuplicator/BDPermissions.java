package ca.strendin.Bukkit.BlockDuplicator;

import org.anjocaido.groupmanager.GroupManager;
import com.nijikokun.bukkit.Permissions.Permissions;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/*
 * This implementation of permissions is heavily influenced by the
 * implementation I found in the MyHome plugin, by tkelly910
 * 
 *  https://github.com/tkelly910
 */

public class BDPermissions {
    
    private enum PermissionHandler {
        PERMISSIONS, GROUP_MANAGER, NONE    
    }
    
    private static PermissionHandler handler;
    private static Plugin permissionPlugin;
    
    
    public static void initPermissions(Server server) {
        Plugin groupManager = server.getPluginManager().getPlugin("GroupManager");
        Plugin permissions = server.getPluginManager().getPlugin("Permissions");
        
        if (groupManager != null) {
            permissionPlugin = groupManager;
            handler = PermissionHandler.GROUP_MANAGER;
            BDLogging.sendConsole("Using GroupManager v" + groupManager.getDescription().getVersion() + " for permissions");            
        } else if (permissions != null) {
            permissionPlugin = permissions;
            handler = PermissionHandler.PERMISSIONS;
            BDLogging.sendConsole("Using Permissions v" + permissions.getDescription().getVersion() + " for permissions");
        } else {
            handler = PermissionHandler.NONE;
            BDLogging.sendConsole("Not using permissions");
        }
    }
    
    private static boolean hasPermission(Player thisplayer, String permName, boolean defaultPermission) {
        
        switch (handler) {
        case PERMISSIONS:
            return ((Permissions) permissionPlugin).getHandler().has(thisplayer, permName);
        case GROUP_MANAGER:
            return ((GroupManager) permissionPlugin).getWorldsHolder().getWorldPermissions(thisplayer).has(thisplayer, permName);
        case NONE:
            return defaultPermission;
        default:
            return defaultPermission;
        }
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
     * Access to the data-cycler capabilities of the duplicator tool
     */
    public static boolean canUseDataTool(Player player) {
        return hasPermission(player,"blockduplicator.tools.data",true);
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
