package dev.yfc.healcooldown.util;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownUtils {
    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final Map<UUID, Float> lastHealths = new HashMap<>();


    public static void onPlayerTick(PlayerEntity player) {
        UUID id = player.getUuid();
        float curHealth = player.getHealth();
        float last = getLastHealth(id);

        if (last >= 0 && curHealth > last) {
            float healAmount = curHealth - last;
            if (healAmount >= 10 && getRemainingCooldownMillis(id) <= 0) {
                startCooldown(id);
            }
        }
        setLastHealth(id, curHealth);
    }


    public static void startCooldown(UUID id) {
        cooldowns.put(id, System.currentTimeMillis());
    }


    public static long getRemainingCooldownMillis(UUID id) {
        Long startTime = cooldowns.get(id);
        if (startTime == null) return 0L;

        long elapsed = System.currentTimeMillis() - startTime;
        long remaining = getCooldownDurationMillis() - elapsed;
        return Math.max(remaining, 0L);
    }


    private static long getCooldownDurationMillis() {
        // 60초 (크래프트기준)
        return 60 * 1000L;
    }


    public static float getLastHealth(UUID id) {
        return lastHealths.getOrDefault(id, -1.0f);
    }

    public static void setLastHealth(UUID id, float health) {
        lastHealths.put(id, health);
    }
}

