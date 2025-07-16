package dev.yfc.healcooldown.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import dev.yfc.healcooldown.HealCooldown;
import dev.yfc.healcooldown.util.CooldownUtils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(EntityRenderer.class)
public class HealCooldownNametagMixin {
    @ModifyArgs(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/EntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
            )
    )
    private void modifyLabelArgs(Args args) {
        if (!HealCooldown.showCooldown) return;

        Entity entity = args.get(0);
        if (!(entity instanceof AbstractClientPlayerEntity player)) return;

        Text origText = (Text) args.get(1);
        UUID uuid = player.getUuid();

        Pattern healthPattern = Pattern.compile("\\b(\\d{1,3})\\s*♥");
        Matcher matcher = healthPattern.matcher(origText.getString());
        if (matcher.find()) {
            int parsedHealth = Integer.parseInt(matcher.group(1));
            float last = CooldownUtils.getLastHealth(uuid);
            long remaining = CooldownUtils.getRemainingCooldownMillis(uuid);

            if (last >= 0 && parsedHealth > last) {
                float healAmount = parsedHealth - last;
                if (healAmount >= 10 && remaining <= 0) {
                    CooldownUtils.startCooldown(uuid);
                }
            }
            CooldownUtils.setLastHealth(uuid, parsedHealth);
        }
        long ms = CooldownUtils.getRemainingCooldownMillis(uuid);

        MutableText tagText = origText.copy();
        tagText.append(Text.literal(" | ").formatted(Formatting.GRAY));
        if (ms > 0) {

            long seconds = (ms + 999) / 1000;
            int sec = (int) Math.min(60, Math.max(0, seconds));
            int r, g, b;

            if (sec >= 30) {
                float t = (60 - sec) / 30.0f;
                r = (int)(0 + 255 * t);
                g = 255;
                b = 0;
            }
            else {
                float t = (30 - sec) / 30.0f;
                r = 255;
                g = (int)(255 - 255 * t);
                b = 0;
            }

            int rgb = (r << 16) | (g << 8) | b;
            tagText.append(Text.literal("" + seconds).styled(s -> s.withColor(rgb)));
        }
        else {
            tagText.append(Text.literal("✔").styled(s -> s.withColor(0x1EFFFA)));
        }
        args.set(1, tagText);
    }
}
