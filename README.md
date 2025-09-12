# SmartSort

A Minecraft plugin for smart inventory and chest sorting with Shift+Right-Click functionality.

## Features

- **Simple Controls**: Shift+Right-Click on empty slots to sort inventories
- **Permission System**: Requires explicit permission grant (smartsort.use)
- **Per-Player Control**: Players can enable/disable sorting individually
- **Smart Categorization**: Sorts items by category (weapons, tools, armor, food, blocks, items)
- **Performance Optimized**: Rate limiting prevents spam and lag
- **Protection Plugin Support**: Respects WorldGuard, LWC, and other protection plugins
- **World-Specific Controls**: Disable sorting in specific worlds

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/smartsort` | Show help and current status | smartsort.use |
| `/smartsort on` | Enable sorting for yourself | smartsort.use |
| `/smartsort off` | Disable sorting for yourself | smartsort.use |
| `/smartsort toggle` | Toggle sorting on/off | smartsort.use |
| `/smartsort info` | Show plugin information | smartsort.use |
| `/smartsort reload` | Reload configuration | smartsort.admin |

## Permissions

- `smartsort.use` - Basic sorting permission (default: false)
- `smartsort.admin` - Admin commands (default: op)
- `smartsort.*` - All permissions (default: op)

## Installation

1. Download the latest release from the [Releases](../../releases) page
2. Place `SmartSort-X.X.X.jar` in your server's `plugins` folder
3. Restart your server
4. Grant the `smartsort.use` permission to players who should have access

## Configuration

The plugin automatically creates a `config.yml` with customizable options:

- Container types that can be sorted
- Sorting behavior (stacking, alphabetical)
- Performance limits
- World restrictions
- Custom messages

## Compatibility

- **Minecraft Versions**: 1.18.2 - 1.21.x
- **Server Software**: Spigot, Paper, Purpur
- **Java**: 8+

## Building

This project uses Maven. To build from source:
```bash
git clone https://github.com/AlakaiIGaming/SmartSort.git
cd SmartSort
mvn clean package
