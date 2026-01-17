package com.manapart.unyieldingitems.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class OnBrokenMixin {

    @Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
    private void onHurtAndBreak(int amount, ServerPlayer serverPlayer, Consumer<Item> consumer, CallbackInfo ci) {
        ci.cancel();
    }
}
