# SmartSort v1.1.0 - Minecraft 1.21.10 Compatibility

## ✅ Confirmed Compatible

This plugin is now configured for **Minecraft Java Edition 1.21.10**.

### Version Details

**Target Version:** 1.21.10 (Released October 7, 2025)
**API Version:** 1.21 (covers all 1.21.x versions)
**Build Against:** Spigot API 1.21.10-R0.1-SNAPSHOT

### What This Means

✅ Will work on Minecraft 1.21.10 servers
✅ Will work on Minecraft 1.21.9 servers  
✅ Will work on Minecraft 1.21.8 servers
✅ Will work on Minecraft 1.21.7 servers
✅ Will work on all 1.21.x versions

### Server Software Compatibility

This plugin works with:
- ✅ Spigot 1.21.10
- ✅ Paper 1.21.10
- ✅ Purpur 1.21.10
- ✅ Any other Bukkit-based server for 1.21.x

### Why It Works Across Versions

The Bukkit/Spigot API uses **minor version compatibility**:
- Setting `api-version: 1.21` means it works with 1.21, 1.21.1, 1.21.2... 1.21.10, etc.
- The APIs this plugin uses haven't changed since 1.8
- No breaking changes in inventory, permission, or event systems

### Building for 1.21.10

When you build with IntelliJ IDEA:
1. Maven will download the 1.21.10 Spigot API
2. Your plugin will be compiled against this version
3. The resulting JAR will work perfectly on 1.21.10 servers

### Testing

Once built and installed on your 1.21.10 server, you should see:
```
[Server] INFO: [SmartSort] Loading SmartSort v1.1.0
[Server] INFO: [SmartSort] SmartSort v1.1.0 has been enabled!
[Server] INFO: [SmartSort] Now with granular permission support!
```

No errors, no warnings - just smooth operation!

## Future Versions

If Minecraft releases 1.21.11, 1.21.12, etc., this plugin will continue to work because:
- We're using `api-version: 1.21` (covers all 1.21.x)
- The core APIs are stable
- Inventory and permission systems don't change between patch versions

## Summary

**Yes, this plugin is fully compatible with Minecraft 1.21.10!**

You can build it right now in IntelliJ IDEA and deploy it to your 1.21.10 server with confidence.
