package dev.girlboss.smarttoggles.mixins;

import dev.girlboss.smarttoggles.SmartTogglesOptions;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.ToggleKeyMapping;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToggleKeyMapping.class)
public abstract class ToggleKeyMappingMixin extends KeyMapping {
    private ToggleKeyMappingMixin(String name, int keysym, Category category) {
        super(name, keysym, category);
    }

    @Unique
    private final SmartTogglesOptions smartToggles$options = SmartTogglesOptions.getInstance();

    @Unique
    private long smartToggles$pressTime = 0;

    @Unique
    private boolean smartToggles$releaseOnNextPress = false;

    @Inject(
            method = "setDown",
            at = @At(
                    value = "JUMP",
                    opcode = Opcodes.IFEQ,
                    ordinal = 1
            ),
            cancellable = true
    )
    private void setDownHook(boolean down, CallbackInfo ci) {
        var window = smartToggles$options.getToggleHoldWindow();
        if (window == 0) {
            return;
        }

        ci.cancel();

        // this is a horrible mess but it doesn't work correctly otherwise
        if (down) {
            if (super.isDown() && smartToggles$releaseOnNextPress) {
                super.setDown(false);
                smartToggles$releaseOnNextPress = false;
                return;
            }

            if (!super.isDown()) {
                smartToggles$pressTime = System.currentTimeMillis();
                super.setDown(true);
            }
        } else {
            if (!super.isDown()) return;

            if (System.currentTimeMillis() - smartToggles$pressTime > window) {
                super.setDown(false);
                return;
            }

            smartToggles$releaseOnNextPress = true;
        }
    }
}
