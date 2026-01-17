package com.manapart.unyieldingitems

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

//TODO - additional events
//On destroy, fall, hurt, pickup
object UnyieldingItemsClient : ClientModInitializer {
    override fun onInitializeClient() {
        AttackBlockCallback.EVENT.register(AttackBlockCallback { playerEntity, world, hand, blockPos, direction ->
            repairItemInHand(playerEntity, hand)
            return@AttackBlockCallback InteractionResult.PASS
        })

        AttackEntityCallback.EVENT.register(AttackEntityCallback { playerEntity, world, hand, entity, entityHitResult ->
            repairItemInHand(playerEntity, hand)
            return@AttackEntityCallback InteractionResult.PASS
        })

        UseItemCallback.EVENT.register(UseItemCallback { playerEntity, world, hand ->
            repairItemInHand(playerEntity, hand)
            return@UseItemCallback InteractionResult.PASS
        })

        ClientTickEvents.START_WORLD_TICK.register { level -> repairOnTick(level) }
    }

    private var tickCount = 0
    private fun repairOnTick(level: ClientLevel) {
        tickCount++
        if (tickCount > 500) {
            tickCount = 0
			level.players().forEach { repairAll(it) }
        }
    }

    private fun repairAll(player: Player) {
        println("Repairing ${player.name.string}")
        EquipmentSlot.entries.forEach { repairItem(player.livingEntity.getItemBySlot(it)) }
    }

    private fun repairItemInHand(player: Player, hand: InteractionHand) {
        repairItem(player.getItemInHand(hand))
    }

    private fun repairItem(stack: ItemStack) {
        if (shouldRepair(stack)) {
            stack.damageValue = 1
        }
    }

    private fun shouldRepair(stack: ItemStack?): Boolean {
        return stack != null && stack.isDamageableItem
    }
}
