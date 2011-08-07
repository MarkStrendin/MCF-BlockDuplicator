BlockDuplicator
===============

[https://github.com/MarkStrendin/BlockDuplicator](https://github.com/MarkStrendin/BlockDuplicator)

This bukkit plugin can be used to assist a player in building something creative, without the need to gather and craft blocks, but without the chaos that can ensue by giving all players access to item spawning commands. It allows a server admin to easily run a hybrid creative / survival server, where allowing players access to creative-oriented commands and tools does not affect the players who wish to play pure survival.

Most recent CraftBukkit version tested with: 1000

**Compatibility note:** Version 0.3.2 requires CraftBukkit build 561 or above, and will not work on earlier builds (use BlockDuplicator 0.3.1 for earlier versions)

Duplicator tool
---------------

The default item for this tool is the stone axe (275), but you can change this to any item in the configuration file.

**Right clicking** a block will give you a full stack of that block (with data intact)



Data Tool
---------

The default item for this tool is the bone (352), but you can change this to any item in the configuration file.

Clicking a block will cycle it's data values. Block types that can be changed are:

 - Wool (Colors)
 - Leaves (Normal, Redwood, Birch)
 - Single steps (Stone, Wood, Cobblestone, Sandstone)
 - Logs (Normal, Redwood, Birch)
 - Steps (direction of the steps)
 - Pumpkins and Jack-o-Lanterns (direction)
 - Tall Grass (Tall Grass,Shrubs,Ferns)
 
 
**Left clicking** will increment(+) the data value by 1
 
**Right clicking** will decrement(-) the data value by 1 (effectively going in the reverse direction as left clicking)




Paintbrush tool
---------------

The default item for this tool is the slime ball (341), but you can change this to any item in the configuration file.

**Left clicking** a block will save that block to your paintbrush tool, including it's data value (color for wool, etc).

**Right clicking** a block will turn that block into a copy of whatever block the paintbrush tool has saved.




Region Tool
-----------

The default item for this tool is the wooden bowl (281), *and currently cannot be changed (this will be changeable in a future update)*

**Left clicking** a block will tell you if there are any regions set at that location

**Right clicking** a block will start creating a region. Clicking once will save the first location, clicking again will save the second, and inform you that you are ready to create a region using the /bdregion command.



Warning about WorldGuard and similar plugins
--------------------------------------------

The data-changing abilities of this plugin do not adhere to regions protected by plugins such as WorldGuard. Users with access to the data-manipulator capabilities of the duplicator and paintbrush tools will be able to modify blocks in protected areas. Only give access to `blockduplicator.tools.data` and `blockduplicator.tools.paintbrush` to those you trust.


Commands
--------
    /blockduplicator	Reloads the config file (alias: /bdreload)
    /duplicator   		Gives the player the duplicator tool (alias: /duper)
    /paintbrush			Gives the player the paintbrush tool (alias: /painter)
    /datatool			Gives the player the data tool
    /bdtools			Gives the player one of each tool that they have permission to use (alias: bdt, tools)
    /clearinv      		Clears the player's inventory (alias: /ci)
    /more          		Gives the player duplicate stacks of whatever he/she is holding (ex: "/m 4" would give you 4 additional stacks) (alias: /m)
    /pick          		Allows the player to change the data value of the item in their hand (for wool colors, single step types, etc)
    /bdregion			Allows the player to create and manage regions (See "Regions" section below for more information)


Permissions
-----------

Supports GroupManager and Permissions - tested with GroupManager 1.0-alpha-5 and Permissions 2.5.4

    blockduplicator.commands.more       		Access to the /more command (and aliases)
    blockduplicator.commands.clearinv   		Access to the /clearinv command (and aliases)
    blockduplicator.commands.pick       		Access to the /pick command (and aliases)
    blockduplicator.tools.data          		Access to the data changer portion of the tool (left click)
    blockduplicator.tools.duplicator    		Access to the duplicator portion of the tool (right click)
    blockduplicator.tools.paintbrush			Access to the not-yet-complete ink tool
    blockduplicator.reload						Access to the /blockduplicator command, used to reload the config file
    blockduplicator.region.manage				Access to the /bdregion command and the ability to create regions
    blockduplicator.region.ignore				Players with this permission set ignore any region restrictions

If you only want users to be able to use the commands:

    blockduplicator.commands.*

If you only want users to be able to use the tools:

    blockduplicator.tools.*
    
If you want users to only be able to duplicate blocks without any data-editing abilities:

    blockduplicator.tools.duplicator

Regions
-------

Regions can be used to limit the use of the tools in certain areas. 

This allows you to, for example, create a structure that contains all of the block types in the game (for easy duplicating), and not worry about accidents where blocks get painted over, or wool colors get changed around.

**Creating a region**

To create a region, you need to use the region tool (a wooden bowl by default). To start creating region, you will need to have a block located on two opposite corners of an area. When the region is created, it will be in the shape of a cube, using the two blocks as opposite corners.

Using the region tool, right click each corner block. When the plugin has saved two coordinates, it will notify you that it is ready to create a region.

If you are not happy with the coordinates that you picked, simply start over. The tool will remember a set of 2 locations, and clicking a third time will erase the 2 saved locations, and start with the first one again.

To create the region, type

    /bdregion create NAME
    
Where NAME is what you would like to call your region.

**Changing a region**

You can use the "flag" parameter to modify the properties of a region, allowing you to customize what tools can and cannot be used in the region. Typing a command to set a flag will toggle the setting - if the setting is currently "allow", it will set it to "deny", and "deny" will be set to "allow".

*Flags*

    duplicate		Toggles use of the duplicator tool (default: allow)
    datacycle		Toggles use fo the data tool (default: deny)
    setink			Toggles the ability to set ink using the paintbrush tool (default: allow)
    paint			Toggles the ability to paint using the paintbrush tool (default: deny)

Example usage:

    /bdregion flag myregionname duplicate

**Deleting a region**

To delete a region, use the command:

    /bdregion remove NAME

Where NAME is the name of the region you wish to remove.

**Listing regions**

You can list all regions by using the following command:

    /bdregion list

**Region Info**

You can get information about a region by left clicking a block using the region tool. If the block you clicked was in a region, it will give you the information about that region.

You can also use the /bdregion command:

    /bdregion info NAME

Where NAME is the name of the region.

**Datafiles**

Regions are saved as datafiles whenever they are modified. Currently, they are stored in a folder called "bdregions", under the root minecraft directory. The files are not human-editable, and attempting to edit them may cause unexpected results.

Changelog
---------

**v0.6.1**

- Fixed a bug where sapling data value was not copied if the sapling was a redwood or birch

**v0.6**

 - Updated for Minecraft 1.6
 - Can now data cycle Tall Grass/Ferns/Dead shrubs (when the shrub is placed on grass)
 

**v0.5**

 - Added support for regions
 
**v0.4**
 
 - Separated duplicator and data tool functionality into two seperate tools
 - Changed how the "bdtools" function works internally - it will now give the player all tools that they have permission to use
 - added /datatool command to summon a data tool
 - changed data tool functionality - left clicking will increase data value of a block by 1, right clicking reverses it, subtracting 1

**v0.3.3**

 - The "blockduplicator"/"bdreload" commands can now be used from the console
 - Minor fixes related to bukkit compatibility (Now works with build 600)  

**v0.3.2**

 - Updated to be compatible with CraftBukkit 561 and above

**v0.3.1**

 - Minor bug fixes
 - Minor communication and logging improvements


**v0.3**

 - Added the paintbrush tool


**v0.2**

 - Added support for a config file

**v0.1**

 - Added support for Permissions and GroupManager 

**v0.0**

 - Initial release
