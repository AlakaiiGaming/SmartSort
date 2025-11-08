# SmartSort v1.1.0 - Complete Update Package

## 📦 What's Included

This package contains everything you need to build and deploy SmartSort v1.1.0:

### Source Code
- ✅ All Java source files with granular permission system
- ✅ Updated plugin.yml with 11 new permission nodes
- ✅ Updated config.yml with new messages
- ✅ Maven POM configuration

### Documentation
- ✅ **README.md** - Overview and features
- ✅ **UPGRADE_GUIDE.md** - Detailed upgrade instructions and use cases
- ✅ **PERMISSIONS.md** - Quick permission reference
- ✅ **build.sh** - Automated build script

### Project Structure
```
SmartSort/
├── src/
│   └── main/
│       ├── java/com/Alakaii/smartsort/
│       │   ├── SmartSortPlugin.java (main class)
│       │   ├── commands/
│       │   │   └── SmartSortCommand.java
│       │   ├── listeners/
│       │   │   └── InventoryListener.java (with granular permissions)
│       │   ├── sorting/
│       │   │   ├── ItemCategorizer.java
│       │   │   └── SortingManager.java
│       │   └── utils/
│       │       ├── ConfigManager.java
│       │       └── MessageUtil.java
│       └── resources/
│           ├── plugin.yml (updated with new permissions)
│           └── config.yml (updated with new messages)
├── pom.xml
├── build.sh (build automation script)
├── README.md
├── UPGRADE_GUIDE.md
└── PERMISSIONS.md
```

## 🚀 Quick Start

### 1. Extract the Package
```bash
unzip SmartSort-1.1.0-src.zip
cd SmartSort
```

### 2. Build the Plugin
```bash
# Option A: Use the build script (recommended)
./build.sh

# Option B: Manual Maven build
mvn clean package
```

### 3. Install
```bash
# Stop your server
./stop.sh

# Copy the JAR
cp target/SmartSort-1.1.0.jar /path/to/server/plugins/

# Start your server
./start.sh
```

## 🎯 Key Features of v1.1.0

### Separate Hotbar and Main Inventory Sorting
- Hotbar (bottom 9 slots) sorts independently
- Main inventory (upper 27 slots) sorts independently  
- Never accidentally mess up your hotbar arrangement
- Full control over each section

### Granular Permissions
Instead of one `smartsort.use` permission, you now have:

**11 Permission Nodes:**
1. `smartsort.use` - Everything (backward compatible)
2. `smartsort.inventory.sort` - Player inventory
3. `smartsort.containers.sort` - All containers
4. `smartsort.containers.chest` - Chests
5. `smartsort.containers.barrel` - Barrels
6. `smartsort.containers.shulkerbox` - Shulker boxes
7. `smartsort.containers.enderchest` - Ender chests
8. `smartsort.containers.hopper` - Hoppers
9. `smartsort.containers.dispenser` - Dispensers
10. `smartsort.containers.dropper` - Droppers
11. `smartsort.admin` - Admin commands

### Permission Hierarchy
```
smartsort.use (grants all)
    ↓
smartsort.containers.sort (grants all containers)
    ↓
smartsort.containers.<type> (specific container)
```

### Backward Compatible
- Existing `smartsort.use` permissions work exactly as before
- No breaking changes
- Optional granular permissions

## 📋 Example Permission Setups

### Setup 1: Progressive Ranks
```bash
# Free - Inventory only
/lp group free permission set smartsort.inventory.sort true

# Member - + Chests
/lp group member permission set smartsort.inventory.sort true
/lp group member permission set smartsort.containers.chest true

# VIP - Everything
/lp group vip permission set smartsort.use true
```

### Setup 2: Donor Perks
```bash
# Regular players
/lp group default permission set smartsort.inventory.sort true

# Donors get special containers
/lp group donor permission set smartsort.inventory.sort true
/lp group donor permission set smartsort.containers.chest true
/lp group donor permission set smartsort.containers.enderchest true
/lp group donor permission set smartsort.containers.shulkerbox true
```

### Setup 3: Role-Based
```bash
# Storage managers
/lp group storage permission set smartsort.containers.sort true

# Regular players
/lp group players permission set smartsort.inventory.sort true

# Builders (need hoppers/dispensers)
/lp group builder permission set smartsort.inventory.sort true
/lp group builder permission set smartsort.containers.chest true
/lp group builder permission set smartsort.containers.hopper true
/lp group builder permission set smartsort.containers.dispenser true
```

## 🔧 Configuration

### New Config Messages
```yaml
messages:
  no-permission-container: "&cYou don't have permission to sort this container type!"
  no-permission-inventory: "&cYou don't have permission to sort your inventory!"
  help-permissions-list: "&eGranular permissions: &fcontainers.sort, inventory.sort, containers.<type>"
```

### Customization
Edit `plugins/SmartSort/config.yml` to:
- Change messages
- Adjust sorting behavior
- Configure disabled worlds
- Set allowed containers
- Modify category order

## 📚 Documentation

### README.md
- Feature overview
- Building instructions
- Installation guide
- Version changelog

### UPGRADE_GUIDE.md
- Detailed upgrade process
- Use cases and examples
- Troubleshooting guide
- Rollback instructions
- Performance notes

### PERMISSIONS.md
- Quick reference table
- Common setups
- LuckPerms examples
- Testing commands
- Compatibility notes

## ✅ Testing Checklist

After installation:

- [ ] Plugin loads without errors
- [ ] `/smartsort help` shows updated help
- [ ] Inventory sorting works with `smartsort.inventory.sort`
- [ ] Container sorting works with `smartsort.containers.sort`
- [ ] Specific container permissions work
- [ ] Legacy `smartsort.use` still works
- [ ] Permission denied messages show correctly
- [ ] `/smartsort reload` works for admins

## 🆘 Troubleshooting

### Build Issues
**Problem:** Maven can't download dependencies
**Solution:** Check internet connection, try `mvn clean install -U`

**Problem:** Java version error
**Solution:** Install Java 8 or higher

### Runtime Issues
**Problem:** Permissions not working
**Solution:** Check `use-permissions: true` in config.yml

**Problem:** Wrong messages displayed
**Solution:** Delete config.yml, restart to regenerate

**Problem:** Plugin not loading
**Solution:** Check server console for errors, verify Spigot/Paper version

## 📊 Version Comparison

| Feature | v1.0.0 | v1.1.0 |
|---------|--------|--------|
| Basic sorting | ✅ | ✅ |
| Single permission | ✅ | ✅ |
| Inventory permission | ❌ | ✅ |
| Container permission | ❌ | ✅ |
| Type-specific perms | ❌ | ✅ |
| Backward compatible | N/A | ✅ |

## 🎓 Learning Resources

1. **Start here:** README.md
2. **For upgrading:** UPGRADE_GUIDE.md
3. **For permissions:** PERMISSIONS.md
4. **For building:** Run `./build.sh`

## 📞 Support

- **GitHub:** https://github.com/Alakaii/SmartSort
- **Issues:** Create with [v1.1.0] tag
- **Questions:** Check UPGRADE_GUIDE.md first

## 🏆 Credits

- **Original Plugin:** Alakaii
- **v1.1.0 Update:** Granular permissions system
- **License:** Check repository

## 📝 Notes

- Compatible with Spigot 1.21+
- Works with Paper, Purpur, and other forks
- Tested with LuckPerms
- Zero performance impact
- No external dependencies

---

**Ready to build?** Run `./build.sh` and follow the prompts!

**Need help?** Check UPGRADE_GUIDE.md for detailed instructions and use cases.

**Want quick reference?** See PERMISSIONS.md for permission examples.
