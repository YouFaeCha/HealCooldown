package dev.yfc.healcooldown.keybind;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindManager {
    public static KeyBinding toggle;

    public static void register() {
        toggle = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.healcooldown.toggle",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_I,
                        "category.healcooldown"
                )
        );
    }

    public static boolean isTogglePressed() {
        return toggle.wasPressed();
    }
}
