# SmartSort v1.2.1 - Bug Fixes & New Commands

## Compatibility
* ✅ Minecraft Java Edition 1.21.x (tested on 1.21.10 - 1.21.11)
* ✅ Spigot, Paper, Purpur, and other Bukkit forks
* ✅ Java 8 or higher (Java 17 recommended)

## What's New in v1.2.1

### Bug Fixes
* **Fixed items randomly rearranging with each sort** - Items now sort consistently and stack neatly from left to right

### New Features

#### Inventory Sorting Commands
Sort your inventory from anywhere using commands - works in all gamemodes including Creative!

| Command | Alias | Description |
|---------|-------|-------------|
| `/smartsort inv` | `/ss inv` | Sort your main inventory |
| `/smartsort hotbar` | `/ss hotbar` | Sort your hotbar |
| `/smartsort reload` | `/ss reload` | Reload configuration |
| `/smartsort help` | `/ss help` | Show help |

#### Clickable Update Notifications
Admins now receive clickable links in chat that open directly to the Modrinth download page when updates are available.

### Notes
* **Shift+right-click** sorting works in Survival/Adventure mode inventories and all containers (chests, barrels, etc.)
* **Creative mode players:** Use commands to sort (shift+right-click doesn't work due to Minecraft limitations)

---

## Features

### Sorting Methods
* **Shift+Right-Click** - Click on an empty slot to sort (Survival/Adventure mode)
* **Commands** - Use `/ss inv` or `/ss hotbar` (all gamemodes)

### Sorting Behavior
* **Player Inventory:** Hotbar and main inventory sort separately
* **Containers:** Entire container sorts together
* **Smart Stacking:** Similar items automatically stack together
* **Category Sorting:** Items organized by type (weapons, tools, armor, food, blocks, etc.)

---

## Permissions

### Granular Permission System
Players can be given specific permissions for different types of sorting:

#### Legacy Permission (Backward Compatible):
* `smartsort.use` - Grants all sorting permissions

#### Granular Permissions:
* `smartsort.inventory.sort` - Allows sorting player inventory only
* `smartsort.containers.sort` - Allows sorting ALL container types
* `smartsort.containers.chest` - Allows sorting chests and trapped chests
* `smartsort.containers.barrel` - Allows sorting barrels
* `smartsort.containers.shulkerbox` - Allows sorting shulker boxes
* `smartsort.containers.enderchest` - Allows sorting ender chests
* `smartsort.containers.hopper` - Allows sorting hoppers
* `smartsort.containers.dispenser` - Allows sorting dispensers
* `smartsort.containers.dropper` - Allows sorting droppers

#### Admin Permissions:
* `smartsort.admin` - Access to reload command and update notifications
* `smartsort.*` - All permissions

### Permission Hierarchy
The plugin checks permissions in this order:
1. `smartsort.use` (if player has this, they can sort everything - backward compatible)
2. `smartsort.containers.sort` (grants all container types)
3. `smartsort.containers.<type>` (specific container type)
4. `smartsort.inventory.sort` (for player inventory)

### Example Permission Setups

Give a player permission to sort only their inventory:
```
/lp user PlayerName permission set smartsort.inventory.sort true
```

Give a player permission to sort all containers but not their inventory:
```
/lp user PlayerName permission set smartsort.containers.sort true
```

Give a player permission to sort only chests and barrels:
```
/lp user PlayerName permission set smartsort.containers.chest true
/lp user PlayerName permission set smartsort.containers.barrel true
```

Give a player permission to sort everything (legacy):
```
/lp user PlayerName permission set smartsort.use true
```

---

## Configuration

### Messages
Customizable message keys in `config.yml`:
* `no-permission` - General no permission message
* `no-permission-container` - Shown when player lacks permission for a container type
* `no-permission-inventory` - Shown when player lacks permission to sort inventory
* `sorted-successfully` - Shown after successful sort
* `sorting-failed` - Shown if sorting fails
* `sorting-disabled-world` - Shown in disabled worlds
* `plugin-reloaded` - Shown after config reload

### Settings
* `check-for-updates` - Enable/disable update checker
* `disabled-worlds` - List of worlds where sorting is disabled
* `sorting.stack-items` - Enable/disable auto-stacking
* `sorting.alphabetical` - Sort alphabetically within categories
* `sorting.category-order` - Customize category priority

---

## Building the Plugin

### Requirements
* Java 8 or higher
* Maven 3.6 or higher
* Internet connection (for downloading dependencies)

### Build Command
```
mvn clean package
```

The compiled JAR will be in the `target` folder.

---

## Changelog

### v1.2.1
* Fixed items randomly rearranging with each sort
* Added `/smartsort inv` and `/smartsort hotbar` commands
* Creative mode now supported via commands
* Clickable update notifications for admins

### v1.2.0
* Added automatic config updates
* Added Modrinth update checker
* Improved error handling

### v1.1.0
* Added granular permission system
* Backward compatible with v1.0.0

### v1.0.0
* Initial release
* Shift+right-click sorting
* Category-based organization
- Java 8 or higher
- Maven 3.6 or higher
- Internet connection (for downloading dependencies)
