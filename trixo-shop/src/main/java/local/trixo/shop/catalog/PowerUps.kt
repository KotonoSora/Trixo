package local.trixo.shop.catalog

import local.trixo.shop.model.PowerUpIds
import local.trixo.shop.model.PowerUpShopItem
import local.trixo.shop.model.ShopItem

object PowerUps {
    const val HINT_COST = 30
    const val UNDO_COST = 15

    fun getPowerUpItems(): List<ShopItem> {
        return listOf(
            PowerUpShopItem(
                id = PowerUpIds.POWERUP_HINT_ID,
                // Must match the engine's PowerUpType id ("hint").
                inventoryId = "hint",
                name = "Hint",
                description = "Reveals a smart move",
                price = HINT_COST,
                isUnlocked = false
            ),
            PowerUpShopItem(
                id = PowerUpIds.POWERUP_UNDO_ID,
                // Must match the engine's PowerUpType id ("undo").
                inventoryId = "undo",
                name = "Undo",
                description = "Takes back your last move",
                price = UNDO_COST,
                isUnlocked = false
            )
        )
    }
}
