package dev.girlboss.smarttoggles;

import com.google.gson.*;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class SmartTogglesOptions {
    private static SmartTogglesOptions instance = null;

    private int toggleHoldWindow = 150;

    private final OptionInstance<Integer> toggleHoldWindowOption = new OptionInstance<>(
            "options.smarttoggles.toggleholdwindow",
            OptionInstance.cachedConstantTooltip(Component.translatable("options.smarttoggles.toggleholdwindow.tooltip")),
            (caption, value) -> value == 0
                ? Options.genericValueLabel(caption, Component.translatable("options.off"))
                : Options.genericValueLabel(caption, Component.translatable("options.value", value)),
            new OptionInstance.IntRange(0, 10),
            3,
            value -> toggleHoldWindow = value * 50
    );

    private final Logger logger = LoggerFactory.getLogger("smart-toggles");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private SmartTogglesOptions() {

    }

    public OptionInstance<Integer> getToggleHoldWindowOption() {
        return toggleHoldWindowOption;
    }

    public int getToggleHoldWindow() {
        return toggleHoldWindow;
    }

    public void load(File file) {
        if (!file.exists()) {
            return;
        }

        try {
            try (var reader = new FileReader(file)) {
                var object = JsonParser.parseReader(reader).getAsJsonObject();
                toggleHoldWindowOption.set(object.getAsJsonPrimitive("toggleHoldWindow").getAsInt());
            }
        } catch (Exception e) {
            logger.warn("Failed to load configuration file, using defaults: ", e);
        }
    }

    public void save(File file) {
        try {
            file.createNewFile();

            var object = new JsonObject();
            object.add("toggleHoldWindow", new JsonPrimitive(toggleHoldWindowOption.get()));
            try (var writer = new FileWriter(file)) {
                writer.write(gson.toJson(object));
            }
        } catch (Exception e) {
            logger.warn("Failed to save configuration file: ", e);
        }
    }

    static {
        instance = new SmartTogglesOptions();
    }

    public static SmartTogglesOptions getInstance() {
        return instance;
    }
}
