BlockDuplicator
===============

This bukkit plugin can be used to easily duplicate blocks.

Most recent CraftBukkit version tested with: 556

Duplicator tool
---------------

While wielding the duplicator tool:

**Left clicking** a block will cycle it's data values. Block types that can be changed are:

 - Wool (Colors)
 - Leaves (Normal, Redwood, Birch)
 - Single steps (Stone, Wood, Cobblestone, Sandstone)
 - Logs (Normal, Redwood, Birch)
 - Steps (direction of the steps)
 - Pumpkins and Jack-o-Lanterns (direction)

**Right clicking** a block will give you a full stack of that block (with data intact)

The default item for this tool is the stone axe (275), but you can change this to any item in the configuration file.


Paintbrush tool
---------------

While wielding the paintbrush tool:

**Left clicking** a block will save that block to your paintbrush tool, including it's data value (color for wool, etc).

**Right clicking** a block will turn that block into a copy of whatever block the paintbrush tool has saved.

The default item for this tool is the slime ball (341), but you can change this to any item in the configuration file.

Warning about WorldGuard and similar plugins
--------------------------------------------

The data-changing abilities of this plugin do not adhere to regions protected by plugins such as WorldGuard. Users with access to the data-manipulator capabilities of the duplicator and paintbrush tools will be able to modify blocks in protected areas. Only give access to `blockduplicator.tools.data` and `blockduplicator.tools.paintbrush` to those you trust.


Commands
--------
    /blockduplicator	Reloads the config file
    /duplicator   		Gives the player the duplicator tool (alias: /duper)
    /paintbrush			Gives the player the paintbrush tool (alias: /painter)
    /bdtools			Gives the player the duplicator and paintbrush tools
    					 Note: Requires permission to summon both tools
    /clearinv      		Clears the player's inventory (alias: cli)    
    /more          		Gives the player duplicate stacks of whatever he/she is holding (ex: "/m 4" would give you 4 additional stacks) (alias: /m)    
    /pick          		Allows the player to change the data value of the item in their hand (for wool colors, single step types, etc)


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

If you only want users to be able to use the commands:

    blockduplicator.commands.*

If you only want users to be able to use the tool:

    blockduplicator.tools.*
    
If you want users to only be able to duplicate blocks without any data-editing abilities:

    blockduplicator.tools.duplicator

Isn't this the same as VoxelDoop / VoxelMore?
---------------------------------------------

Yes, this plugin's functionality is the same as VoxelDoop and VoxelMore, however this plugin supports permissions. When Bukkit's built-in permissions become available, and the VoxelBox team updates their plugins to support it, this plugin will no longer be necessary.

If your server is 100% creative, you should probably be using VoxelMore and VoxelDoop instead, as they are more powerful, and made by people who know more about what they're doing than I do.


Changelog
---------

**v0.3**

 - Added the paintbrush tool


**v0.2**

 - Added support for a config file

**v0.1**

 - Added support for Permissions and GroupManager 

**v0.0**

 - Initial release
