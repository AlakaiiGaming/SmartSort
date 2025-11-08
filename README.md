# SmartSort v1.1.0 - Granular Permissions Update

## Compatibility

- ✅ Minecraft Java Edition 1.21.x (tested on 1.21.10)
- ✅ Spigot, Paper, Purpur, and other Bukkit forks
- ✅ Java 8 or higher (Java 17 recommended)

What's New in v1.1.0

This update adds **granular permission support** while maintaining full backward compatibility with v1.0.0.

New Features

#### Granular Permission System
Players can now be given specific permissions for different types of sorting:

**Legacy Permission (Backward Compatible):**
- `smartsort.use` - Grants all sorting permissions (works exactly like v1.0.0)

**New Granular Permissions:**
- `smartsort.inventory.sort` - Allows sorting player inventory only
- `smartsort.containers.sort` - Allows sorting ALL container types
- `smartsort.containers.chest` - Allows sorting chests and trapped chests
- `smartsort.containers.barrel` - Allows sorting barrels
- `smartsort.containers.shulkerbox` - Allows sorting shulker boxes
- `smartsort.containers.enderchest` - Allows sorting ender chests
- `smartsort.containers.hopper` - Allows sorting hoppers
- `smartsort.containers.dispenser` - Allows sorting dispensers
- `smartsort.containers.dropper` - Allows sorting droppers

### Permission Hierarchy

The plugin checks permissions in this order:

1. **smartsort.use** (if player has this, they can sort everything - backward compatible)
2. **smartsort.containers.sort** (grants all container types)
3. **smartsort.containers.<type>** (specific container type)
4. **smartsort.inventory.sort** (for player inventory)

### Example Permission Setups

#### Give a player permission to sort only their inventory:
```
/lp user PlayerName permission set smartsort.inventory.sort true
```

#### Give a player permission to sort all containers but not their inventory:
```
/lp user PlayerName permission set smartsort.containers.sort true
```

#### Give a player permission to sort only chests and barrels:
```
/lp user PlayerName permission set smartsort.containers.chest true
/lp user PlayerName permission set smartsort.containers.barrel true
```

#### Give a player permission to sort everything (legacy):
```
/lp user PlayerName permission set smartsort.use true
```

### Updated Messages

New message keys in config.yml:
- `no-permission-container` - Shown when player lacks permission for a container type
- `no-permission-inventory` - Shown when player lacks permission to sort inventory
- `help-permissions-list` - Additional help text explaining granular permissions

## Building the Plugin

### Requirements
- Java 8 or higher
- Maven 3.6 or higher
- Internet connection (for downloading dependencies)

### Build Instructions

1. Navigate to the project directory:
```bash
cd SmartSort
```

2. Build with Maven:
```bash
mvn clean package
```

3. The compiled JAR will be in:
```
target/SmartSort-1.1.0.jar
```

### Installation

1. Stop your Minecraft server
2. Replace the old `SmartSort-1.0.0.jar` in your `plugins` folder with `SmartSort-1.1.0.jar`
3. Start your server
4. The plugin will automatically update the config.yml with new message keys
5. Review and adjust permissions for your players/groups

## Backward Compatibility

**100% backward compatible** - If you don't set any of the new granular permissions, the plugin will work exactly like v1.0.0:
- Players with `smartsort.use` can sort everything
- Players without `smartsort.use` cannot sort anything

## Configuration

The config.yml has been updated with:
- Additional help messages explaining granular permissions
- New error messages for specific permission denials
- Comments explaining the new permission system

## Support

For issues, suggestions, or questions:
- GitHub: https://github.com/Alakaii/SmartSort
- Create an issue with [v1.1.0] in the title

## Changelog

### v1.1.0
- ✨ Added granular permission system
- ✨ Added container-specific permissions
- ✨ Added separate inventory permission
- ✨ **Added separate hotbar and main inventory sorting**
- 🔧 Hotbar (slots 0-8) and main inventory (slots 9-35) now sort independently
- 🔧 Updated help command with permission details
- 🔧 Added new config messages
- ✅ Maintained full backward compatibility with v1.0.0
- 📝 Updated documentation

### v1.0.0
- Initial release
- Shift+Right-Click sorting
- Category-based sorting
- Protection plugin integration
