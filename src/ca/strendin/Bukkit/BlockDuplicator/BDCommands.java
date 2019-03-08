package ca.strendin.Bukkit.BlockDuplicator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BDCommands {
    
    // These represent default values
    // Values from the config file overwrite these
    public static int duplicatorToolID = 284; // Golden Shovel
    public static int paintBrushToolID = 286; // Golden Axe
    public static int regionToolID = 294;   // Golden Hoe
    public static String duplicatorToolName = "Duplicator / info tool";
    public static String paintbrushToolName = "Paintbrush tool";
    public static String regionToolName = "Region tool";    
    
    //static ChatColor textColor = ChatColor.DARK_GREEN;

    /*
     * These blocks play nicely with the paint tool    
     */
    private static int[] PaintSafeBlocks = {
    	1,2,3,4,5,12,13,14,15,16,17,18,19,20,21,22,24,25,35,41,42,43,44,45,47,48,49,53,56,57,58,60,67,73,74,
    	80,82,84,85,86,87,88,89,91,92,98,99,100,101,102,103,108,109,110,112,113,114,118,121,123,124,128,125,
    	126,129,133,134,135,136,139,151,152,153,155,156};
    
    private static int[] DuplicatableBlocks = {
    	1,2,3,4,5,12,13,14,15,16,17,18,19,20,21,22,24,25,29,33,35,41,42,43,44,45,46,47,48,49,53,54,56,57,58,59,61,62,
    	63,68,64,65,66,69,70,71,60,67,73,74,80,82,84,85,86,87,88,89,91,92,98,99,100,101,102,103,108,109,110,112,
    	113,114,118,121,123,72,75,76,77,78,79,93,94,96,97,106,107,115,116,117,120,124,125,126,127,128,129,131,132,133,
    	134,135,136,137,138,139,140,143,145,146,147,148,149,150,151,152,153,154,155,156,157,158,130,83,30};
    
    public static ArrayList<Integer> denied_blocks = new ArrayList<Integer>();   
    
    
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
        case 62: returnMe = 61; break; // Furnace (on)
        case 74: returnMe = 73; break; // Redstone ore
        case 26: returnMe = 355; break; // Bed
        case 68: returnMe = 323; break; // Sign
        case 63: returnMe = 323; break; // Sign post
        case 93: returnMe = 356; break; // Redstone repeater (off)
        case 94: returnMe = 356; break; // Redstone repeater (on)
        case 75: returnMe = 76; break; // Redstone torch (off)
        case 92: returnMe = 354; break; // Cake
        case 59: returnMe = 295; break; // Seeds
        case 55: returnMe = 331; break; // Redstone
        case 43: returnMe = 44; break; // Convert double steps to single steps (only pick up single steps)
        case 34: returnMe = 33; break; // Piston extension
        case 149: returnMe = 404; break; // Comparator (off)
        case 150: returnMe = 404; break; // Comparator (on)
        case 97: returnMe = 98; break; // Sivlerfish egg -> stone bricks
        case 115: returnMe = 372; break; // Nether Wart
        case 124: returnMe = 123; break; // Redstone lamp (on)
        case 125: returnMe = 126; break; // Wooden slab
        case 127: returnMe = 351; break; // Cocoa Pod
        case 132: returnMe = 287; break; // Tripwire / string
        case 140: returnMe = 390; break; // Flower pot
        case 144: returnMe = 397; break; // Mob head
        case 117: returnMe = 379; break; // Brewing stand
        case 118: returnMe = 380; break; // Cauldron
        }        
        return returnMe;
    }
    
    
    
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
            case 44: MaxData = 7; break; /* Stone Slabs */
            case 43: MaxData = 7; break; /* Double Stone slabs */
            case 17: MaxData = 2; break; /* Logs */
            case 35: MaxData = 15; break; /* Wool */ 
            case 18: MaxData = 3; break; /* Leaves */
            case 6: MaxData = 3; break; /* Saplings */
            case 98: MaxData = 3; break; /* Stone Brick */
            case 125: MaxData = 3; break; /* Wood slab (single) */
            case 126: MaxData = 3; break; /* Wood slab (double) */
            case 155: MaxData = 4; break; /* Quartz */
            
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
    public static boolean isItemOnBlacklist(int itemID) {
        boolean returnMe = false;
        
        for (int checked_block : denied_blocks) {
            if (checked_block == itemID) {
                returnMe = true;
            }
        }
        return returnMe;
    }  
    
    public static boolean isItemOnBlacklist(Block thisBlock) {
        boolean returnMe = false;

        int itemID = thisBlock.getTypeId();
        
        for (int checked_block : denied_blocks) {
            if (checked_block == itemID) {
                returnMe = true;
            }
        }
        return returnMe;
    }  
    
    /*
     * Checks to see if this item is one that has a special data value, to
     *  determine if we should be copying its data field
     * 
     *  Used for duplicator tool, not for pick command
     */
    public static boolean isItemWithDataValue(int checkThisItemID) {
        boolean returnMe = false;
        switch (checkThisItemID) {
        case 44: returnMe = true; break; // Stone slab (single) (0-7)
        case 43: returnMe = true; break; // Stone slab (double) (0-7)
        case 17: returnMe = true; break; // logs
        case 35: returnMe = true; break; // wool
        case 53: returnMe = true; break; // wood step    
        case 67: returnMe = true; break; // stone step
        case 6: returnMe = true; break;  // Saplings
        case 108: returnMe = true; break;  // Brick Stairs
        case 109: returnMe = true; break;  // Stone Brick Stairs
        case 18: returnMe = true; break; // leaves
        case 99: returnMe = true; break; // Huge brown mushroom
        case 100: returnMe = true; break; // Huge red mushroom
        case 155: returnMe = true; break; // Block of quartz (0-4)
        case 125: returnMe = true; break; // Wood slab (single) (0-3)
        case 126: returnMe = true; break; // Wood slab (double) (0-3)
        
        }
        return returnMe;
    }
    
      
    
    /*
     * Gives the player a duplicator tool
     */
    public static void givePlayerDuplicatorTool (Player thisPlayer) {
        ItemStack DuplicatorToolItem = new ItemStack(duplicatorToolID,(short)1,(byte)0);
        
        // Modify the item before sending it to the player
        ItemMeta meta = DuplicatorToolItem.getItemMeta(); 
        
        // Rename the item
        meta.setDisplayName(duplicatorToolName);        
        
        // Set the item's lore
        ArrayList<String> newLore = new ArrayList<String>();
        newLore.add("Left clicking gives block info.");
        newLore.add("");
        newLore.add("Right clicking gives you a stack");
        newLore.add("of the block you clicked on.");
        meta.setLore(newLore);
        
        // Commit changes to the item
        DuplicatorToolItem.setItemMeta(meta);
        
        BDLogging.sendPlayer(thisPlayer, "Giving Duplicator tool: " + BDLogging.itemColor + DuplicatorToolItem.getType());
        BDLogging.logThis("[TOOLS] Giving " + thisPlayer.getDisplayName() + " a duplicator tool (" + DuplicatorToolItem.getType() + ")");
        thisPlayer.getInventory().addItem(DuplicatorToolItem);
    }
    
    /*
     * Gives the player a paintbrush
     */
    public static void givePlayerPaintbrushTool(Player thisPlayer) {
        ItemStack PaintBrushToolItem = new ItemStack(paintBrushToolID,(short)1,(byte)0);
        
        // Modify the item before sending it to the player
        ItemMeta meta = PaintBrushToolItem.getItemMeta(); 
        
        // Rename the item
        meta.setDisplayName(paintbrushToolName);        
        
        // Set the item's lore
        ArrayList<String> newLore = new ArrayList<String>();
        newLore.add("Left click to store a block.");
        newLore.add("Right click to replace blocks with");
        newLore.add("the block you've stored.");
        meta.setLore(newLore);
        
        // Commit changes to the item
        PaintBrushToolItem.setItemMeta(meta);
        
        BDLogging.sendPlayer(thisPlayer, "Giving Paintbrush tool: " + BDLogging.itemColor + PaintBrushToolItem.getType());
        BDLogging.logThis("[TOOLS] Giving " + thisPlayer.getDisplayName() + " a paintbrush tool (" + PaintBrushToolItem.getType() + ")");
        
        thisPlayer.getInventory().addItem(PaintBrushToolItem);
    }
    
    
    /*
     * Gives the player a region tool
     */
    public static void givePlayerRegionTool(Player thisPlayer) {
        ItemStack RegionToolItem = new ItemStack(regionToolID,(short)1,(byte)0);
        
        // Modify the item before sending it to the player
        ItemMeta meta = RegionToolItem.getItemMeta(); 
        
        // Rename the item
        meta.setDisplayName(regionToolName);        
        
        // Set the item's lore
        ArrayList<String> newLore = new ArrayList<String>();
        newLore.add("Left click a block to display");
        newLore.add("region information at a block's");
        newLore.add("location.");
        newLore.add("Right click to begin creating");
        newLore.add("a cuboid region.");
        meta.setLore(newLore);
        
        // Commit changes to the item
        RegionToolItem.setItemMeta(meta);
        
        BDLogging.sendPlayer(thisPlayer, "Giving Data tool: " + BDLogging.itemColor + RegionToolItem.getType());
        BDLogging.logThis("[TOOLS] Giving " + thisPlayer.getDisplayName() + " a data tool (" + RegionToolItem.getType() + ")");
        
        thisPlayer.getInventory().addItem(RegionToolItem);
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

    
    public static boolean isDuplicatableBlock(Block block) {
        
        boolean returnMe = false;
        int blockid = block.getTypeId();
        
        for (int x : DuplicatableBlocks) {
            if (x == blockid) {
                returnMe = true;
            }
        }        
        return returnMe;     
    }
    
    public static boolean isDuplicatableBlock(int blockid) {
        boolean returnMe = false;
        
        for (int x : DuplicatableBlocks) {
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
        fB.write("# the duplicator tool. DEFAULT IS " + duplicatorToolID);
        fB.newLine();
        fB.write("#");
        fB.newLine();
        fB.write("#  Left clicking will display block information on the clicked block");
        fB.newLine();
        fB.write("#");
        fB.newLine();
        fB.write("#  Right clicking will give the player a stack of the");
        fB.newLine();
        fB.write("#   type of block clicked");
        fB.newLine();
        fB.newLine();
        fB.write("duplicatortoolid = " + duplicatorToolID);
        fB.newLine();
        fB.newLine();        
        
        fB.write("# Paintbrush tool");
        fB.newLine();
        fB.write("#");
        fB.newLine();
        fB.write("# The item with this ID number will be used as");
        fB.newLine();
        fB.write("# the paintbrush tool. DEFAULT IS " + paintBrushToolID);
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
        fB.write("paintbrushtoolid = " + paintBrushToolID);
        fB.newLine();
        fB.newLine();
        fB.newLine();
        
        fB.write("# Region tool");
        fB.newLine();
        fB.write("#");
        fB.newLine();
        fB.write("# The item with this ID number will be used as");
        fB.newLine();
        fB.write("# the region tool. DEFAULT IS " + regionToolID);
        fB.newLine();
        fB.write("#");
        fB.newLine();
        fB.write("#  Right clicking will display region info about the block clicked");
        fB.newLine();
        fB.write("#");
        fB.newLine();
        fB.write("#  Left clicking will start creating a cuboid region");
        fB.newLine();
        fB.write("#   the clicked block");
        fB.newLine();
        fB.newLine();
        fB.write("regiontoolid = " + regionToolID);
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
