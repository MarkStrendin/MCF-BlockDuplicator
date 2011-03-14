BlockDuplicator
===============

This bukkit plugin can be used to easily duplicate blocks.

Most recent CraftBukkit version tested with: 531

Usage
-----

While wielding the duplicator tool:

**Left clicking** a block will cycle it's data values. Block types that can be changed are:

 - Wool (Colors)
 - Leaves (Normal, Redwood, Birch)
 - Single steps (Stone, Wood, Cobblestone, Sandstone)
 - Logs (Normal, Redwood, Birch)
 - Steps (direction of the steps)

**Right clicking** a block will give you a full stack of that block (with data intact)

Warning about WorldGuard and similar plugins
--------------------------------------------

The data-changing abilities of this plugin do not adhere to regions protected by plugins such as WorldGuard. Users with access to the data-manipulator capabilities of the duplicator tool will be able to modofiy blocks in protected areas. Only give access to `blockduplicator.tool.data` to those you trust.


Commands
--------
    /duper         Gives the player the duplicator tool
    /clearinv      Clears the player's inventory
    /ci            Same as /clearinv
    /more          Gives the player duplicate stacks of whatever he/she is holding (ex: "/m 4" would give you 4 additional stacks)
    /m             Same as /more
    /pick          Allows the player to change the data value of the item in their hand (for wool colors, single step types, etc)


Permissions
-----------

Supports GroupManager and Permissions - tested with GroupManager 1.0-alpha-5 and Permissions 2.5.4

    blockduplicator.commands.more       Access to the /more command (and aliases)
    blockduplicator.commands.clearinv   Access to the /clearinv command (and aliases)
    blockduplicator.commands.pick       Access to the /pick command (and aliases)
    blockduplicator.tool.tool           Access to the /duper command to summon the tool
    blockduplicator.tool.data           Access to the data changer portion of the tool (left click)
    blockduplicator.tool.duplicator     Access to the duplicator portion of the tool (right click)

If you only want users to be able to use the commands:

    blockduplicator.commands.*

If you only want users to be able to use the tool:

    blockduplicator.tool.*

Isn't this the same as VoxelDoop / VoxelMore?
---------------------------------------------

Yes, this plugin's functionality is the same as VoxelDoop and VoxelMore, however this plugin supports permissions.

The server that I run is a hybrid creative/survival server - some players prefer to play survival over creative, but still want to play with friends who prefer to play creative. This plugin gives players the capabilities of the awesome VoxelDoop and VoxelMore plugins, but with the ability to limit it to certain players based on permissions.

If your server is 100% creative, you should probably be using VoxelMore and VoxelDoop instead, as they are more powerful, and made by people who know more about what they're doing than I do. I have the highest respect for the developers of the VoxelBox plugins, and I don't mean this to be just a blatant ripoff of their work - I simply wanted the functionality of their awesome plugins in the context of server that isn't 100% creative.