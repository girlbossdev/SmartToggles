package dev.girlboss.smarttoggles;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.Minecraft;

import java.io.File;

public class SmartToggles implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        var file = new File(Minecraft.getInstance().gameDirectory, "config/smarttoggles.json");

        SmartTogglesOptions.getInstance().load(file);
        ClientLifecycleEvents.CLIENT_STOPPING.register(_ -> SmartTogglesOptions.getInstance().save(file));
    }
}
