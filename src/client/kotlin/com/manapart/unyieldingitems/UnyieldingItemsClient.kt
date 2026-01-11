package com.manapart.unyieldingitems

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
//TODO - additional events
//On destroy, fall, hurt, pickup
object UnyieldingItemsClient : ClientModInitializer {
	override fun onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		AttackBlockCallback.EVENT.register(AttackBlockCallback { playerEntity, world, hand, blockPos, direction ->
//			println("Attack Block Callback")
			repairItemInHand(playerEntity, hand)
			return@AttackBlockCallback InteractionResult.PASS
		})

		AttackEntityCallback.EVENT.register(AttackEntityCallback { playerEntity, world, hand, entity, entityHitResult ->
//			println("Attack Entity")
			repairItemInHand(playerEntity, hand)
			return@AttackEntityCallback InteractionResult.PASS
		})

		UseItemCallback.EVENT.register(UseItemCallback { playerEntity, world, hand ->
			repairItemInHand(playerEntity, hand)
			return@UseItemCallback InteractionResult.PASS
		})

	}

	private fun repairAll(player: Player) {
		repairItemInHand(player, InteractionHand.MAIN_HAND)
		repairItemInHand(player, InteractionHand.OFF_HAND)
//		for (stack in player.armorSlots) {
//			repairItem(stack)
//		}
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
