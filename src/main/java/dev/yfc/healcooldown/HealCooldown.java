package dev.yfc.healcooldown;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dev.yfc.healcooldown.keybind.KeyBindManager;
import dev.yfc.healcooldown.util.CooldownUtils;

public class HealCooldown implements ClientModInitializer {

    public static final Logger logger = LogManager.getLogger(HealCooldown.class);

    public static boolean showCooldown = true;

    @Override
    public void onInitializeClient() {
        try {
            // Logging Shit
            logger.info("Initializing HealCooldown v5...");
            KeyBindManager.register();

            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (client.world != null) {
                    for (PlayerEntity player : client.world.getPlayers()) {
                        CooldownUtils.onPlayerTick(player);
                    }
                }
                if (KeyBindManager.isTogglePressed()) {
                    showCooldown = !showCooldown;
                }
            });
        }

        catch (Exception e) {
            logger.error("Error occurred while initializing HealCooldown :", e);
        }
    }
}
