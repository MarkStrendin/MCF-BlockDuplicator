package ca.strendin.Bukkit.BlockDuplicator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BDCommands {
    
    // Load these from a config file at some point
    public static int DuplicatorTool = 275;
    public static int PaintBrushTool = 341;
    //public static int[] denied_blocks;
    public static ArrayList<Integer> denied_blocks = new ArrayList<Integer>();
    
    
    /*
     * Clears the player's inventory
     */
    public static boolean handleClearInvCmd(Player thisplayer) {
        thisplayer.getInventory().clear();
        BDLogging.sendMsg(thisplayer,"Inventory cleared!");
        return true;
    }
    
    /*
     * Checks to see if the item is on the allowed list
     */
    public static boolean ThisItemAllowed(int itemID) {
        boolean returnMe = true;
        
        for (int checked_block : denied_blocks) {
            if (checked_block == itemID) {
                returnMe = false;
            }
        }
        return returnMe;
    }
    
    
    /*
     * Gives the player one stack of the item
     */
    public static boolean giveStack(Player toThisPlayer, ItemStack thisStack) {        
        toThisPlayer.getInventory().addItem(thisStack);
        BDLogging.sendMsg(toThisPlayer,"Giving 64 " + thisStack.getType());
        return true;
    }
    
    
    /*
     * Gives the player the specified number of stacks of a given item
     */
    public static boolean giveStack(Player toThisPlayer, ItemStack thisItemID,int numStacks) {
        //number of stacks should never exceed 36, because thats all the inventory space a player has
        if (numStacks > 36) {
            numStacks = 36;
        }
        
        for (int x = 0; x < numStacks; x++) {
            ItemStack tobegiven = thisItemID;
            tobegiven.setAmount(64);
            toThisPlayer.getInventory().addItem(tobegiven);            
        }
        
        BDLogging.sendMsg(toThisPlayer,"Giving (" + numStacks + " x 64) " + thisItemID.getType());        
        
        return true;
    }
    
    
    /*
     * Translates an item ID # into it's name
     */
    public static String itemIDToName(int itemID) {
        ItemStack thisItemStack = new ItemStack(itemID);
        return thisItemStack.getType().toString();
    }
    
    /*
     * Handles the /more command
     * Finishes the player's current stack, and gives them the specified number more stacks
     */
    public static boolean handleMoreCmd(Player thisplayer,String[] args) {
        
        
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
    public static boolean handlePickCmd(Player thisplayer,String[] args) {
        
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
    
    /*
     * Checks to see if this item is one that has a special data value, to
     *  determine if we should be copying its data field
     * 
     */
    public static boolean isItemWithDataValue(int checkThisItemID) {
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
     * Gives the player a duplicator tool
     */
    public static void givePlayerDuplicatorTool (Player thisPlayer) {
        ItemStack DuplicatorToolItem = new ItemStack(DuplicatorTool,(short)1,(byte)0);
        thisPlayer.getInventory().addItem(DuplicatorToolItem);
    }

    
    /* Translates a block ID into an item ID for certain
     *  block types.
     *  
     * For example, a wooden door block's ID is 64, but we 
     *  want to give the player the item 324 instead
     */
    public static int translateBlockToItemID(int checkThisItemID) {
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
     * This probably shouldn't be in the code itself, but until I find a 
     *  better way of doing this, it will have to do
     */
    public static void writeDefaultConfigFile(File thisfile) throws IOException {
        
        BufferedWriter fB = new BufferedWriter(new FileWriter(thisfile));        
        fB.write("#");
        fB.newLine();
        fB.write("# BlockDuplicator configuration file");
        fB.newLine();
        fB.write("#"); 
        fB.newLine();
        fB.newLine();
        fB.newLine();
        fB.write("# Duplicator tool");
        fB.newLine();
        fB.write("#");
        fB.newLine();
        fB.write("# The item with this ID number will be used as");
        fB.newLine();
        fB.write("# the duplicator tool. DEFAULT IS 275 (Stone Axe)");
        fB.newLine();
        fB.write("#");
        fB.newLine();
        fB.write("#  Right clicking will give the player a stack of the");
        fB.newLine();
        fB.write("#   type of block clicked");
        fB.newLine();
        fB.write("#");
        fB.newLine();
        fB.write("#  Left clicking will cycle the block's data field");
        fB.newLine();
        fB.newLine();
        fB.write("duplicatortoolid = 275");
        fB.newLine();
        fB.newLine();
        fB.newLine();
        fB.write("# Paintbrush tool");
        fB.newLine();
        fB.write("#");
        fB.newLine();
        fB.write("# The item with this ID number will be used as");
        fB.newLine();
        fB.write("# the paintbrush tool. DEFAULT IS 341 (Slime Ball)");
        fB.newLine();
        fB.write("#");
        fB.newLine();
        fB.write("#  Right clicking will \"copy\" the block hit with the tool");
        fB.newLine();
        fB.write("#");
        fB.newLine();
        fB.write("#  Left clicking will \"paste\" the copied block onto");
        fB.newLine();
        fB.write("#   the clicked block");
        fB.newLine();
        fB.newLine();
        fB.write("paintbrushtoolid = 341");
        fB.newLine();
        fB.newLine();
        fB.newLine();
        fB.write("# Denied Items");
        fB.newLine();
        fB.write("#");
        fB.newLine();
        fB.write("# These items are not allowed to be duplicated");
        fB.newLine();
        fB.newLine();
        fB.write("blacklist = 7,8,9,10,11,51,52,79");
        fB.close();
        
    }
    
    

}
