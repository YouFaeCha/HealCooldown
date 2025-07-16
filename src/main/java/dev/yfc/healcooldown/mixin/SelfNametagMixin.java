package dev.yfc.healcooldown.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntityRenderer.class})
public class SelfNametagMixin {
    @Inject(
            at = {
                    @At("HEAD")
            },
            method = {
                    "Lnet/minecraft/client/render/entity/LivingEntityRenderer;hasLabel(Lnet/minecraft/entity/LivingEntity;)Z"
            },
            cancellable = true
    )

    private void showName(LivingEntity ent, CallbackInfoReturnable<Boolean> ci) {
        if (ent == MinecraftClient.getInstance().cameraEntity) {
            ci.setReturnValue( Boolean.valueOf( MinecraftClient.isHudEnabled() ) );
        }
    }
}