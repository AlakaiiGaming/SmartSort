# SmartSort - Hotbar vs Main Inventory Sorting Guide

## How Player Inventory Sorting Works

SmartSort treats your hotbar and main inventory as **separate sections** that sort independently.

### Visual Layout

```
┌─────────────────────────────────────────────────┐
│         PLAYER INVENTORY WINDOW                 │
├─────────────────────────────────────────────────┤
│  Crafting    │                                  │
│  Area        │     MAIN INVENTORY               │
│  [2x2]       │     (27 slots - 3 rows x 9)      │
│              │                                  │
│  Result →    │  [▢][▢][▢][▢][▢][▢][▢][▢][▢]     │
│              │  [▢][▢][▢][▢][▢][▢][▢][▢][▢]     │
│  [Armor]     │  [▢][▢][▢][▢][▢][▢][▢][▢][▢]     │
│  Helmet      │                                  │
│  Chest       │  ← Shift+Right-Click empty slot  │
│  Legs        │     here to sort main inventory  │
│  Boots       │                                  │
├─────────────────────────────────────────────────┤
│              HOTBAR (9 slots - 1 row)           │
│  [▢][▢][▢][▢][▢][▢][▢][▢][▢]                   │
│   1  2  3  4  5  6  7  8  9                    │
│                                                 │
│  ← Shift+Right-Click empty slot here            │
│     to sort ONLY the hotbar                     │
└─────────────────────────────────────────────────┘
```

## Examples

### Example 1: Sorting Only Your Hotbar

**Before:**
```
Hotbar:    [Sword][  ][Apple][Coal][  ][Pickaxe][  ][Dirt][Torch]
Main Inv:  [Stone][Wood][Iron][Gold][Diamond][...lots of items...]
```

**Action:** Shift+Right-Click an empty slot in the HOTBAR

**After:**
```
Hotbar:    [Sword][Pickaxe][Apple][Coal][Dirt][Torch][  ][  ][  ]
                 ↑ Sorted by category and alphabetically
Main Inv:  [Stone][Wood][Iron][Gold][Diamond][...unchanged...]
                 ↑ Main inventory stays exactly the same
```

### Example 2: Sorting Only Your Main Inventory

**Before:**
```
Hotbar:    [Sword][Pickaxe][Food][  ][  ][  ][  ][  ][  ]
Main Inv:  [Dirt][Apple][Stone][Coal][Wood][Iron][...]
           [Random][Messy][Items][Everywhere][...][...]
           [More][Stuff][Here][There][...][...]
```

**Action:** Shift+Right-Click an empty slot in the MAIN INVENTORY area

**After:**
```
Hotbar:    [Sword][Pickaxe][Food][  ][  ][  ][  ][  ][  ]
                 ↑ Hotbar stays exactly the same
Main Inv:  [Sword][Pickaxe][Apple][Dirt][Stone][Wood][Coal][Iron][...]
           [...sorted by category...]
           [...then alphabetically...]
                 ↑ Main inventory is now organized
```

## Why Separate Sorting?

### 1. **Hotbar Control**
Your hotbar contains your most-used items that you've carefully arranged:
- Sword in slot 1
- Pickaxe in slot 2
- Food in slot 3
- Etc.

You **don't want** main inventory sorting to mess up your hotbar arrangement!

### 2. **Main Inventory Organization**
Your main inventory often gets messy from mining, looting, etc.
You **want to sort it** without affecting your carefully-arranged hotbar.

### 3. **Flexibility**
- Sort your messy main inventory whenever you want
- Keep your hotbar exactly how you like it
- Or sort both separately when needed

## Usage Tips

### Tip 1: Sort Main Inventory Frequently
```
After mining, looting, or gathering:
→ Shift+Right-Click empty slot in main inventory
→ Everything organized, hotbar untouched!
```

### Tip 2: Sort Hotbar Occasionally
```
When you change activities (mining → building):
→ Rearrange hotbar items manually for new task
→ Shift+Right-Click empty hotbar slot to organize
→ Main inventory stays organized from before
```

### Tip 3: Sort Both When Needed
```
After major cleanup:
1. Shift+Right-Click in main inventory → sorts main
2. Shift+Right-Click in hotbar → sorts hotbar
3. Both sections now organized independently
```

## Slot Numbers (Technical)

For developers and curious users:

**Hotbar:**
- Slot 0-8 (9 slots)
- Bottom row of inventory
- Corresponds to number keys 1-9

**Main Inventory:**
- Slot 9-35 (27 slots)
- Three rows above hotbar
- 9 columns × 3 rows = 27 slots

**Other Slots (NOT sorted by SmartSort):**
- Armor slots (36-39)
- Offhand slot (40)
- Crafting grid (not affected)

## Common Questions

**Q: Can I sort my entire inventory at once?**
A: No, and this is intentional! Keeping them separate gives you more control.

**Q: What if I click in the armor or crafting area?**
A: Nothing happens - those areas are not sortable.

**Q: Does this work in containers too?**
A: Containers (chests, barrels, etc.) sort the entire container since they don't have a "hotbar" concept.

**Q: Can I disable separate sorting?**
A: Not in v1.1.0, but this may be a config option in future versions if requested.

## Summary

✅ **Hotbar = Separate** (slots 0-8)
✅ **Main Inventory = Separate** (slots 9-35)
✅ **Armor = Not affected**
✅ **Crafting = Not affected**
✅ **Containers = Entire container sorts as one unit**

This design gives you **maximum control** over your inventory organization!
