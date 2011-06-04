package ca.strendin.Bukkit.BlockDuplicator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BDCommands {
    
    // These represent default values
    // Values from the config file overwrite these
    public static int DuplicatorTool = 275;
    public static int DataTool = 352;
    public static int PaintBrushTool = 341;
    public static int RegionTool = 281;
    public static int InfoTool = 286;
    
    //static ChatColor textColor = ChatColor.DARK_GREEN;

    /*
     * These blocks play nicely with the paint tool    
     */
    private static int[] PaintSafeBlocks = {1,2,3,4,5,12,13,14,15,16,17,18,19,20,21,22,24,25,35,41,42,43,44,45,47,48,49,53,56,57,58,60,67,73,74,80,82,84,85,86,87,88,89,91,92};
    
    //public static int[] denied_blocks;
    public static ArrayList<Integer> denied_blocks = new ArrayList<Integer>();
    
    
    /*
     * Clears the player's inventory
     */
    public static boolean handleClearInvCmd(Player thisplayer) {
        thisplayer.getInventory().clear();
        BDLogging.logThis("[CI] " +thisplayer.getDisplayName() + " cleared their inventory");
        BDLogging.sendPlayer(thisplayer,"Inventory cleared!");
        return true;
    }
    

    /*
     * Handles the /more command
     * Finishes the player's current stack, and gives them the specified number more stacks
     */
    public static boolean handleMoreCmd(Player thisplayer,String[] args) {        
        
        int thisManyStacks = 1;
        
        if (args.length > 0) {
            try {
                thisManyStacks = Integer.parseInt(args[0].trim());
            } catch (Exception e) { thisManyStacks = 1;}
        }
        
        if (thisplayer.getItemInHand().getTypeId() != 0) {        
            BDLogging.logThis("[MORE] Giving " + thisplayer.getDisplayName() + " " + thisManyStacks + " stacks of " + thisplayer.getItemInHand().getTypeId());
            giveStack(thisplayer,thisplayer.getItemInHand(),thisManyStacks);
            return true;
        } else {
            return false;
        }
    }
    
    /*
     * Handles the /pick command
     * - /pick <#>
     * Attempts to set the data value of whatever the player is holding to the 
     *  specified number
     */
    public static boolean handlePickCmd(Player thisplayer,String[] args) {
        
        if (thisplayer.getItemInHand().getTypeId() != 0) {
            int newDataValue = 0;
    
            if (args.length > 0) {
                try {
                    newDataValue = Integer.parseInt(args[0].trim());
                } catch (Exception e) { newDataValue = 0;}
                
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
            case 6: MaxData = 2; break;
            }
            
            /* 
             * If everything seems sane, change the data value
             */
            if ((newDataValue <= MaxData) && (newDataValue >= 0)) {
                BDLogging.logThis("[PICK] Changing " + thisplayer.getDisplayName() + "'s " + thisplayer.getItemInHand().getType() + " to data value " + newDataValue);
                thisplayer.getItemInHand().setDurability((short) newDataValue);
            }
        }
        return true;
    }
    

    
    
    /*
     * Gives the player one stack of the item
     */
    public static boolean giveStack(Player toThisPlayer, ItemStack thisStack) {        
        toThisPlayer.getInventory().addItem(thisStack);
        BDLogging.sendPlayer(toThisPlayer,"Giving 64 " + BDLogging.itemColor + thisStack.getType());
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
            //ItemStack tobegiven = thisItemID.;
            //tobegiven.setAmount(64);
            
            ItemStack tobegiven = new ItemStack(thisItemID.getTypeId(),(short)64,(byte)0);
            tobegiven.setData(thisItemID.getData());
            tobegiven.setDurability(thisItemID.getDurability());
            
            
            toThisPlayer.getInventory().addItem(tobegiven);            
        }
        
        BDLogging.sendPlayer(toThisPlayer,"Giving (" + numStacks + " x 64) " + BDLogging.itemColor + thisItemID.getType());        
        
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
     * Translates an item ID # into it's name
     */
    public static String itemIDToName(int itemID) {
        ItemStack thisItemStack = new ItemStack(itemID);
        return thisItemStack.getType().toString();
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
        case 53: returnMe = true; break; // wood step    
        case 67: returnMe = true; break; // stone step
        case 6: returnMe = true; break;  // Saplings
        // case 31: returnMe = true; break; // Tall Grass will always be a dead shrub if placed
        
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
        BDLogging.sendPlayer(thisPlayer, "Giving Duplicator tool: " + BDLogging.itemColor + DuplicatorToolItem.getType());
        BDLogging.logThis("[TOOLS] Giving " + thisPlayer.getDisplayName() + " a duplicator tool (" + DuplicatorToolItem.getType() + ")");
        thisPlayer.getInventory().addItem(DuplicatorToolItem);
    }
    
    /*
     * Gives the player a paintbrush
     */
    public static void givePlayerPaintbrushTool(Player thisPlayer) {
        ItemStack PaintBrushToolItem = new ItemStack(PaintBrushTool,(short)1,(byte)0);
        BDLogging.sendPlayer(thisPlayer, "Giving Paintbrush tool: " + BDLogging.itemColor + PaintBrushToolItem.getType());
        BDLogging.logThis("[TOOLS] Giving " + thisPlayer.getDisplayName() + " a paintbrush tool (" + PaintBrushToolItem.getType() + ")");
        
        thisPlayer.getInventory().addItem(PaintBrushToolItem);
    }
    
    
    /*
     * Gives the player a data tool
     */
    public static void givePlayerDataTool(Player thisPlayer) {
        ItemStack DataToolItem = new ItemStack(DataTool,(short)1,(byte)0);
        BDLogging.sendPlayer(thisPlayer, "Giving Data tool: " + BDLogging.itemColor + DataToolItem.getType());
        BDLogging.logThis("[TOOLS] Giving " + thisPlayer.getDisplayName() + " a data tool (" + DataToolItem.getType() + ")");
        
        thisPlayer.getInventory().addItem(DataToolItem);
    }
    
    /*
     * Gives the player a region tool
     */
    public static void givePlayerRegionTool(Player thisPlayer) {
        ItemStack RegionToolItem = new ItemStack(RegionTool,(short)1,(byte)0);
        BDLogging.sendPlayer(thisPlayer, "Giving Data tool: " + BDLogging.itemColor + RegionToolItem.getType());
        BDLogging.logThis("[TOOLS] Giving " + thisPlayer.getDisplayName() + " a data tool (" + RegionToolItem.getType() + ")");
        
        thisPlayer.getInventory().addItem(RegionToolItem);
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
    
    
    public static boolean isPaintableBlock(Block block) {
        
        boolean returnMe = false;
        int blockid = block.getTypeId();
        
        for (int x : PaintSafeBlocks) {
            if (x == blockid) {
                returnMe = true;
            }
        }        
        return returnMe;     
    }
    
    public static boolean isPaintableBlock(int blockid) {
        boolean returnMe = false;
        
        for (int x : PaintSafeBlocks) {
            if (x == blockid) {
                returnMe = true;
            }
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
        fB.newLine();
        fB.write("duplicatortoolid = 275");
        fB.newLine();
        fB.newLine();        
        fB.write("# Data tool");
        fB.newLine(); 
        fB.write("#");
        fB.newLine(); 
        fB.write("# The item with this ID number will be used as");
        fB.newLine(); 
        fB.write("# the data scrolling tool. DEFAULT IS 352 (Bone)");
        fB.newLine(); 
        fB.write("#");
        fB.newLine(); 
        fB.write("#  Left clicking a block will increment it's data");
        fB.newLine(); 
        fB.write("#  value by 1");
        fB.newLine(); 
        fB.write("#");
        fB.newLine(); 
        fB.write("#  Right clicking a block will decrement it's data");
        fB.newLine(); 
        fB.write("#  value by 1");
        fB.newLine(); 
        fB.newLine(); 
        fB.write("datatoolid = 352");        
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
