# SmartSort v1.1.0 - Hotbar/Inventory Separation Fix

## Issue Fixed

**Problem:** When sorting player inventory, both the hotbar AND main inventory were sorted together, which would mess up carefully-arranged hotbar items.

**Solution:** Hotbar and main inventory now sort **completely separately**.

## How It Works Now

### Visual Guide
```
Player Inventory Layout:
┌──────────────────────────────┐
│   MAIN INVENTORY (27 slots)  │  ← Click here to sort
│   [  ][  ][  ][  ][  ][  ]   │     ONLY this section
│   [  ][  ][  ][  ][  ][  ]   │
│   [  ][  ][  ][  ][  ][  ]   │
├──────────────────────────────┤
│   HOTBAR (9 slots)            │  ← Click here to sort
│   [1][2][3][4][5][6][7][8][9] │     ONLY this section
└──────────────────────────────┘
```

### Usage

**To sort ONLY your hotbar:**
1. Open inventory (press E)
2. Shift+Right-Click on an empty slot in the HOTBAR (bottom row)
3. Only the 9 hotbar slots will be sorted
4. Main inventory stays exactly as it was

**To sort ONLY your main inventory:**
1. Open inventory (press E)
2. Shift+Right-Click on an empty slot in the MAIN INVENTORY (upper 3 rows)
3. Only the 27 main inventory slots will be sorted
4. Hotbar stays exactly as it was

## Technical Details

### Code Changes

**InventoryListener.java:**
- Added detection to determine which section (hotbar or main inventory) was clicked
- Hotbar = slots 0-8
- Main inventory = slots 9-35
- Passes this information to the SortingManager

**SortingManager.java:**
- New method: `sortPlayerInventory(Player, Inventory, boolean isHotbar)`
- Sorts only the specified range of slots
- Leaves other slots completely untouched

### Slot Ranges

```java
if (isHotbar) {
    startSlot = 0;    // First hotbar slot
    endSlot = 8;      // Last hotbar slot
} else {
    startSlot = 9;    // First main inventory slot
    endSlot = 35;     // Last main inventory slot
}
```

## Benefits

### 1. Hotbar Control
Keep your combat/building setup exactly where you want it:
- Slot 1: Sword (always)
- Slot 2: Pickaxe (always)
- Slot 3: Food (always)
- etc.

Sorting main inventory won't mess this up!

### 2. Main Inventory Organization
After mining or looting, your main inventory gets messy.
Sort it without affecting your hotbar setup!

### 3. No Accidents
Can't accidentally reorganize your hotbar when you just wanted to sort main inventory.

## Examples

### Example 1: Keep Hotbar, Sort Main

**Before:**
```
Hotbar:     [Sword][Pick][Axe][Food][Torch][  ][  ][  ][  ]
Main Inv:   [Dirt][Coal][Stone][Iron][Wood][Diamond][...]
            (everything mixed up)
```

**Action:** Shift+Right-Click empty slot in main inventory

**After:**
```
Hotbar:     [Sword][Pick][Axe][Food][Torch][  ][  ][  ][  ]  ✓ Unchanged!
Main Inv:   [Sword][Pick][Axe][Food][Coal][Diamond][Dirt][Iron][Stone][Wood][...]
            ↑ Sorted by category and alphabetically
```

### Example 2: Sort Hotbar Only

**Before:**
```
Hotbar:     [Dirt][  ][Sword][Apple][  ][Pick][  ][Torch][Coal]
Main Inv:   [Organized][Items][From][Previous][Sort][...]
```

**Action:** Shift+Right-Click empty slot in hotbar

**After:**
```
Hotbar:     [Sword][Pick][Apple][Coal][Dirt][Torch][  ][  ][  ]  ✓ Sorted!
Main Inv:   [Organized][Items][From][Previous][Sort][...]  ✓ Unchanged!
```

## Testing Checklist

After building and installing:

- [ ] Open player inventory
- [ ] Put items in hotbar in specific order
- [ ] Put random items in main inventory
- [ ] Shift+Right-Click empty slot in MAIN inventory
- [ ] Verify: Main inventory sorted, hotbar unchanged
- [ ] Mess up hotbar order
- [ ] Shift+Right-Click empty slot in HOTBAR
- [ ] Verify: Hotbar sorted, main inventory unchanged

## Files Modified

1. **InventoryListener.java** - Added slot detection logic
2. **SortingManager.java** - Added `sortPlayerInventory()` method
3. **config.yml** - Added explanation of separate sorting
4. **README.md** - Documented feature
5. **INVENTORY_SORTING.md** - New comprehensive guide

## Backward Compatibility

✅ **Fully backward compatible** - no breaking changes
✅ Works with all existing permissions
✅ Containers still sort as one unit (no change)
✅ Only affects player inventory sorting behavior

## Summary

This fix gives players **precise control** over their inventory organization by treating hotbar and main inventory as completely separate sections.

**No more accidentally reorganizing your hotbar when you just wanted to sort your main inventory!**
