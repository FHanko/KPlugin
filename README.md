# KPlugin
A papermc general purpose API.

[![](https://jitpack.io/v/FHanko/KPlugin.svg)](https://jitpack.io/#FHanko/KPlugin)

```gradle
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
  }
}
```
```gradle
dependencies {
  implementation 'com.github.FHanko:KPlugin:Tag'
}
```

# Usage
## Items

Extend ItemBase for custom items. Implement ItemHandler interfaces like `ClickHandler`, `DropHandler`, `EquipHandler` for extended functionality.
```kotlin
object TestItem: ItemBase(0, Material.DIAMOND, "Test"), EquipHandler, DropHandler, ClickHandler, Cooldownable {
    override fun equip(p: Player, e: EquipHandler.EquipType) {
        p.sendMessage("Equipped test")
    }

    override fun unequip(p: Player, e: EquipHandler.EquipType) {
        p.sendMessage("Unequipped test")
    }

    override fun drop(e: PlayerDropItemEvent) {
        e.player.sendMessage("Dropped test")
    }

    override fun pickup(e: EntityPickupItemEvent) {
        val p = e.entity as Player
        p.sendMessage("Picked up test")
    }

    override fun getCooldown() = 3000L
    override fun rightClick(e: PlayerInteractEvent) {
        if (useCooldown(e.player)) {
            e.player.velocity = e.player.location.getDirection().setY(0).normalize().multiply(2).setY(0.3)
        }
    }
}
```

## Blocks

BlockBase can be extended like ItemBase and is itself an extension of ItemBase, so it can implement ItemHandlers.
In addition it has handlers for block placement, removal and breaking.

Textured blocks load an ItemDisplay with given texture covering the implemented block:
```kotlin
private const val TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmMzMTNhOGE1MzE4NjM4OGI5YjVmMDdhOGRhZTg4NThhYTI0YmE4Njk4YzgyZTdlZjdiYTg3NTg4MDlhYWIzNyJ9fX0="
object TestBlock: TexturedBlock(TEXTURE, 4, Material.IRON_BLOCK, Component.text("Test")), ClickHandler {
    override fun broke(e: BlockBreakEvent) {
        e.player.sendMessage("Destroyed test")
    }

    override fun rightClickBlock(e: PlayerInteractEvent) {
        e.player.sendMessage("Clicked test")
    }
}
```

