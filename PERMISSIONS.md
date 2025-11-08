# SmartSort v1.1.0 - Quick Permission Reference

## Quick Setup Commands

### LuckPerms Examples

#### Give Everyone Inventory Sorting
```bash
/lp group default permission set smartsort.inventory.sort true
```

#### Give VIP All Sorting
```bash
/lp group vip permission set smartsort.use true
```

#### Give Specific Player Container Sorting
```bash
/lp user PlayerName permission set smartsort.containers.sort true
```

## Permission Nodes

| Permission | Description | Recommended For |
|------------|-------------|-----------------|
| `smartsort.use` | **Everything** (legacy) | Admins, VIPs |
| `smartsort.*` | All permissions | Server Staff |
| `smartsort.admin` | Reload command | Moderators+ |
| `smartsort.inventory.sort` | Own inventory only | All players |
| `smartsort.containers.sort` | All containers | Trusted players |
| `smartsort.containers.chest` | Chests only | Members |
| `smartsort.containers.barrel` | Barrels only | Members |
| `smartsort.containers.shulkerbox` | Shulker boxes | VIP/Donors |
| `smartsort.containers.enderchest` | Ender chests | VIP/Donors |
| `smartsort.containers.hopper` | Hoppers | Redstone builders |
| `smartsort.containers.dispenser` | Dispensers | Redstone builders |
| `smartsort.containers.dropper` | Droppers | Redstone builders |

## Common Setups

### Minimal Access (Free)
```bash
/lp group free permission set smartsort.inventory.sort true
```
*Can sort: Their inventory*

### Basic Access (Member)
```bash
/lp group member permission set smartsort.inventory.sort true
/lp group member permission set smartsort.containers.chest true
/lp group member permission set smartsort.containers.barrel true
```
*Can sort: Inventory, chests, barrels*

### Enhanced Access (VIP)
```bash
/lp group vip permission set smartsort.inventory.sort true
/lp group vip permission set smartsort.containers.sort true
```
*Can sort: Everything*

### Staff Access
```bash
/lp group mod permission set smartsort.* true
```
*Can sort: Everything + reload command*

## Permission Priority

```
smartsort.use
    ↓ (if not present, check:)
smartsort.containers.sort
    ↓ (if not present, check:)
smartsort.containers.<specific_type>
```

## Testing Permissions

```bash
# Check what permissions a player has
/lp user PlayerName permission info

# Test a specific permission
/lp user PlayerName permission check smartsort.containers.chest

# See effective permissions
/lp user PlayerName permission list
```

## Compatibility Notes

- ✅ Works with LuckPerms
- ✅ Works with PermissionsEx
- ✅ Works with GroupManager
- ✅ Works with any Vault-compatible permission plugin
- ✅ 100% backward compatible with v1.0.0

## Default Behavior

**If no granular permissions are set:**
- Plugin falls back to `smartsort.use`
- Works exactly like v1.0.0
- No breaking changes

## Config Messages

New messages in config.yml:
```yaml
no-permission-container: "&cYou don't have permission to sort this container type!"
no-permission-inventory: "&cYou don't have permission to sort your inventory!"
```

Customize these in `plugins/SmartSort/config.yml`

## Build Instructions

```bash
cd SmartSort
mvn clean package
# Output: target/SmartSort-1.1.0.jar
```

## Installation

1. Stop server
2. Remove old JAR
3. Add SmartSort-1.1.0.jar to plugins/
4. Start server
5. Configure permissions (optional)

## Commands

| Command | Permission | Description |
|---------|-----------|-------------|
| `/smartsort` | `smartsort.use` | Show help |
| `/smartsort help` | `smartsort.use` | Show help |
| `/smartsort reload` | `smartsort.admin` | Reload config |
| `/ss` | `smartsort.use` | Alias for /smartsort |
| `/ssort` | `smartsort.use` | Alias for /smartsort |

## Usage

1. Open inventory or container
2. Hold **Shift**
3. **Right-Click** on empty slot
4. Inventory sorts automatically!

## Support

- GitHub: https://github.com/Alakaii/SmartSort
- Issues: Create with [v1.1.0] tag
