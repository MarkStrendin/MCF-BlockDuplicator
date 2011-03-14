package ca.strendin.Bukkit.BlockDuplicator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;



public class BlockDuplicator extends JavaPlugin {
    private final BDBlockListener blockListener = new BDBlockListener(this);
    
    

    // Load these from a config file at some point
    public int DuplicatorTool = 281;    
    public int[] denied_blocks = {7,8,9,10,11,14,15,16,21,56,51,52,73,74};
    
    private ChatColor BDChatColor = ChatColor.AQUA;
       
    
    @Override
    public void onDisable() {        
        System.out.println(this.getDescription().getName() + " disabled");
        
    }

    @Override
    public void onEnable() {  

        
        // Permissions
        BDPermissions.initPermissions(getServer());

        System.out.println(this.getDescription().getName() + " v" + this.getDescription().getVersion() + " enabled");

        // Register events
        PluginManager pm = getServer().getPluginManager();        
        pm.registerEvent(Event.Type.BLOCK_RIGHTCLICKED, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_DAMAGED, blockListener, Priority.Normal, this);    
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args ) {
        if (sender instanceof Player) {
            
            // If the sender is a player
            Player requestplayer = (Player)sender;
        
            if ((commandLabel.equalsIgnoreCase("clearinv")) || (commandLabel.equalsIgnoreCase("ci"))) {
                if (BDPermissions.clearinv(requestplayer)) {
                    handleClearInvCmd(requestplayer);
                } else {                    
                    BDLogging.permDenyMsg(requestplayer);
                }
            } else if ((commandLabel.equalsIgnoreCase("more")) || (commandLabel.equalsIgnoreCase("m"))) {                
                if (BDPermissions.more(requestplayer)) {
                    handleMoreCmd(requestplayer,args);
                } else {                    
                    BDLogging.permDenyMsg(requestplayer);
                }
            } else if ((commandLabel.equalsIgnoreCase("pick")) || (commandLabel.equalsIgnoreCase("p"))) {                
                if (BDPermissions.pick(requestplayer)) {
                    handlePickCmd(requestplayer,args);
                } else {                    
                    BDLogging.permDenyMsg(requestplayer);
                }               
            } else if (commandLabel.equalsIgnoreCase("duper")) {                
                if (BDPermissions.tool(requestplayer)) {
                    givePlayerDuplicatorTool(requestplayer);
                } else {                    
                    BDLogging.permDenyMsg(requestplayer);
                }
            }
        } else {
            BDLogging.sendConsole("This command is designed for players only");            
        }
        
        return true;        
    }
        
    
    
    
    /*
     * Checks to see if the item is on the allowed list
     */
    public boolean ThisItemAllowed(int itemID) {
        boolean returnMe = true;
        
        for (int checked_block : denied_blocks) {
            if (checked_block == itemID) {
                returnMe = false;
            }
        }
        
        return returnMe;
    }
    
    
    /*
     * Translates an item ID # into it's name
     */
    public String itemIDToName(int itemID) {
        ItemStack thisItemStack = new ItemStack(itemID);
        return thisItemStack.getType().toString();
    }
    
    
    
    /*
     * Gives the player one stack of the item
     */
    public boolean giveStack(Player toThisPlayer, ItemStack thisStack) {        
        toThisPlayer.getInventory().addItem(thisStack);
        return true;
    }
    
    
    /*
     * Gives the player the specified number of stacks of a given item
     */
    public boolean giveStack(Player toThisPlayer, ItemStack thisItemID,int numStacks) {
        //number of stacks should never exceed 36, because thats all the inventory space a player has
        if (numStacks > 36) {
            numStacks = 36;
        }
        
        for (int x = 0; x < numStacks; x++) {
            ItemStack tobegiven = thisItemID;
            tobegiven.setAmount(64);
            toThisPlayer.getInventory().addItem(tobegiven);            
        }
        
        toThisPlayer.sendMessage(BDChatColor + "Giving (" + numStacks + " x 64) of " + thisItemID.getType());        
        
        return true;
    }
    
    
    /*
     * Gives the player a duplicator tool
     */
    public void givePlayerDuplicatorTool (Player thisPlayer) {
        ItemStack DuplicatorToolItem = new ItemStack(DuplicatorTool,(short)1,(byte)0);
        
                
        thisPlayer.getInventory().addItem(DuplicatorToolItem);
    }

    
    /* Translates a block ID into an item ID for certain
     *  block types.
     *  
     * For example, a wooden door block's ID is 64, but we 
     *  want to give the player the item 324 instead
     */
    public int translateBlockToItemID(int checkThisItemID) {
        int returnMe = checkThisItemID;
        
        switch (checkThisItemID) {
        case 64: returnMe = 324; break; // Wooden door 
        case 71: returnMe = 330; break; // Iron door
        case 26: returnMe = 355; break; // Bed
        case 68: returnMe = 323; break; // Sign
        case 93: returnMe = 356; break; // Redstone repeater (off)
        case 94: returnMe = 356; break; // Redstone repeater (on)
        case 75: returnMe = 76; break; // Redstone torch (off)
        case 92: returnMe = 354; break; // Cake
        case 59: returnMe = 295; break; // Seeds
        case 55: returnMe = 331; break; // Redstone
        case 43: returnMe = 44; break; // Convert double steps to single steps (only pick up single steps)
        }        
        return returnMe;
    }
    
    
    /*
     * Clears the player's inventory
     */
    public boolean handleClearInvCmd(Player thisplayer) {
        thisplayer.getInventory().clear();
        thisplayer.sendMessage(BDChatColor + "Inventory cleared!");
        return true;
    }
    
    
    /*
     * Checks to see if this item is one that has a special data value
     * 
     * TODO: Check to see if this is even necesary anymore...
     *  We should probably always copy the data value of whatever we're copying 
     */
    public boolean isItemWithDataValue(int checkThisItemID) {
        boolean returnMe = false;
        switch (checkThisItemID) {
        case 44: returnMe = true; break; // single step
        case 43: returnMe = true; break; // double step
        case 17: returnMe = true; break; // logs
        case 35: returnMe = true; break; // wool
        case 53: returnMe = true; break; //wood step    
        case 67: returnMe = true; break; // stone step
        // Leaves don't work so well
        //case 18: returnMe = true; break; // leaves   
        }
        return returnMe;
    }
    
    
    
    
    /*
     * Handles the /more command
     * Finishes the player's current stack, and gives them the specified number more stacks
     */
    public boolean handleMoreCmd(Player thisplayer,String[] args) {
        
        
        int thisManyStacks = 1;
        
        if (args.length > 0) {
            thisManyStacks = Integer.parseInt(args[0].trim());
        }
        
        giveStack(thisplayer,thisplayer.getItemInHand(),thisManyStacks);
        return true;
    }
    
    
    /*
     * Handles the /pick command
     * - /pick <#>
     * Attempts to set the data value of whatever the player is holding to the 
     *  specified number
     */
    public boolean handlePickCmd(Player thisplayer,String[] args) {
        
        int newDataValue = 0;

        if (args.length > 0) {
            newDataValue = Integer.parseInt(args[0].trim());
        }
        
                
        /* Check to see if we allow this block's data to be modified
         * and figure out the appropriate data values for it
         */
        
        int MaxData = -1;
        switch (thisplayer.getItemInHand().getTypeId()) {
        case 44: MaxData = 3; break;
        case 43: MaxData = 3; break;
        case 17: MaxData = 2; break;
        case 35: MaxData = 15; break;   
        case 53: MaxData = 3; break;   
        case 67: MaxData = 3; break;
        case 18: MaxData = 2; break;
        }
        
        /* 
         * If everything seems sane, change the data value
         */
        if ((newDataValue <= MaxData) && (newDataValue >= 0)) {
            thisplayer.getItemInHand().setDurability((short) newDataValue);
        }
        return true;
    }
    


    
}
