BlockDuplicator
===============

This bukkit plugin can be used to easily duplicate blocks.



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


Commands
--------
    /duper         Gives the player the duplicator tool
    /clearinv      Clears the player's inventory
    /ci            Same as /clearinv
    /more          Gives the player duplicate stacks of whatever he/she is holding (ex: "/m 4" would give you 4 additional stacks)
    /m             Same as /more
    /pick          Allows the player to change the data value of the item in their hand (for wool colors, single step types, etc)



Isn't this the same as VoxelDoop / VoxelMore?
---------------------------------------------

Yes, this plugin's functionality is the same as VoxelDoop and VoxelMore, however this plugin supports permissions (currently in the form of GroupManager).

The server that I run is a hybrid creative/survival server - some players prefer to play survival over creative, but still want to play with friends who prefer to play creative. This plugins gives players the capabilities of the awesome VoxelDoop and VoxelMore plugins, but with the ability to limit it to certain players based on permissions.

If your server is 100% creative, you should probably be using VoxelMore and VoxelDoop instead, as they are more powerful, and made by people who know more about what they're doing than I do.