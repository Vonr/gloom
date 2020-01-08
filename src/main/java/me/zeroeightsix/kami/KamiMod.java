//
// Decompiled by Procyon v0.5.36
//

package me.zeroeightsix.kami;

import me.zero.alpine.EventManager;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import com.google.gson.JsonPrimitive;
import me.zeroeightsix.kami.gui.rgui.component.Component;
import java.util.Optional;
import java.util.Iterator;
import me.zeroeightsix.kami.gui.rgui.component.container.Container;
import me.zeroeightsix.kami.gui.rgui.util.ContainerHelper;
import me.zeroeightsix.kami.gui.rgui.component.AlignedComponent;
import me.zeroeightsix.kami.gui.rgui.util.Docking;
import me.zeroeightsix.kami.gui.rgui.component.container.use.Frame;
import com.google.gson.JsonElement;
import java.util.Map;
import me.zeroeightsix.kami.setting.config.Configuration;
import java.nio.file.LinkOption;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.nio.file.Path;
import java.nio.file.NoSuchFileException;
import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.SettingsRegister;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.util.Friends;
import me.zeroeightsix.kami.util.Wrapper;
import me.zeroeightsix.kami.util.LagCompensator;
import me.zeroeightsix.kami.event.ForgeEventProcessor;
import net.minecraftforge.common.MinecraftForge;
import java.util.function.Consumer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.zeroeightsix.kami.util.ColourUtils;
import java.awt.Color;
import me.zeroeightsix.kami.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import java.awt.Font;
import me.zeroeightsix.kami.setting.Settings;
import com.google.common.base.Converter;
import me.zeroeightsix.kami.gui.font.CFontRenderer;
import com.google.gson.JsonObject;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.command.CommandManager;
import me.zeroeightsix.kami.gui.kami.KamiGUI;
import me.zero.alpine.EventBus;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "kami", name = "Gloom Client", version = "B1.2")
public class KamiMod
{
    public static final String MODID = "kami";
    public static final String MODNAME = "Gloom Client";
    public static final String MODVER = "B1.2";
    public static final String KAMI_HIRAGANA = "\u304b\u307f";
    public static final String KAMI_KATAKANA = "\u30ab\u30df";
    public static final String KAMI_KANJI = "Gloom Client";
    private static final String KAMI_CONFIG_NAME_DEFAULT = "KAMIConfig.json";
    public static final Logger log;
    public static final EventBus EVENT_BUS;
    public int redForBG;
    public int greenForBG;
    public int blueForBG;
    public boolean rainbowBG;
    @Mod.Instance
    private static KamiMod INSTANCE;
    public KamiGUI guiManager;
    public CommandManager commandManager;
    private Setting<JsonObject> guiStateSetting;
    CFontRenderer cFontRenderer;

    public KamiMod() {
        this.guiStateSetting = Settings.custom("gui", new JsonObject(), new Converter<JsonObject, JsonObject>() {
            protected JsonObject doForward(final JsonObject jsonObject) {
                return jsonObject;
            }

            protected JsonObject doBackward(final JsonObject jsonObject) {
                return jsonObject;
            }
        }).buildAndRegister("");
        this.cFontRenderer = new CFontRenderer(new Font("Verdana", 0, 18), true, false);
    }

    private void checkSettingGuiColour(final Setting setting) {
        final String name2;
        final String name = name2 = setting.getName();
        switch (name2) {
            case "Red": {
                this.redForBG = (int) setting.getValue();
                break;
            }
            case "Green": {
                this.greenForBG = (int) setting.getValue();
                break;
            }
            case "Blue": {
                this.blueForBG = (int) setting.getValue();
                break;
            }
        }
    }

    private void checkRainbowSetting(final Setting setting) {
        final String name2;
        final String name = name2 = setting.getName();
        switch (name2) {
            case "Rainbow Watermark": {
                this.rainbowBG = (boolean) setting.getValue();
                break;
            }
        }
    }

    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
    }

    @SubscribeEvent
    public void onRenderGui(final RenderGameOverlayEvent.Post event) {
        final Minecraft mc = Minecraft.getMinecraft();
        final float[] hue = { System.currentTimeMillis() % 11520L / 11520.0f };
        ModuleManager.getModuleByName("Gui").settingList.forEach(setting -> this.checkSettingGuiColour(setting));
        ModuleManager.getModuleByName("Gui").settingList.forEach(setting -> this.checkRainbowSetting(setting));
        final int rgb = Color.HSBtoRGB(hue[0], 1.0f, 1.0f);
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR) {
            return;
        }
        final String playername = mc.player.getName();
        if (this.rainbowBG) {
            this.cFontRenderer.drawStringWithShadow("Welcome " + playername + "", 1.0, 10.0, rgb);
            this.cFontRenderer.drawStringWithShadow("Gloom Client B1.2", 1.0, 1.0, rgb);
            final float[] array = hue;
            final int n = 0;
            array[n] += 0.02f;
        }
        else {
            this.cFontRenderer.drawStringWithShadow("Welcome " + playername + "", 1.0, 10.0, ColourUtils.toRGBA(this.redForBG, this.greenForBG, this.blueForBG, 255));
            this.cFontRenderer.drawStringWithShadow("Gloom Client B1.2", 1.0, 1.0, ColourUtils.toRGBA(this.redForBG, this.greenForBG, this.blueForBG, 255));
        }
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        KamiMod.log.info("\n\nInitializing KAMI b4");
        ModuleManager.initialize();
        ModuleManager.getModules().stream().filter(module -> module.alwaysListening).forEach(KamiMod.EVENT_BUS::subscribe);
        MinecraftForge.EVENT_BUS.register((Object)new ForgeEventProcessor());
        MinecraftForge.EVENT_BUS.register((Object)this);
        LagCompensator.INSTANCE = new LagCompensator();
        Wrapper.init();
        (this.guiManager = new KamiGUI()).initializeGUI();
        this.commandManager = new CommandManager();
        Friends.initFriends();
        SettingsRegister.register("commandPrefix", Command.commandPrefix);
        loadConfiguration();
        KamiMod.log.info("Settings loaded");
        ModuleManager.updateLookup();
        ModuleManager.getModules().stream().filter(Module::isEnabled).forEach(Module::enable);
        KamiMod.log.info("KAMI Mod initialized!\n");
    }

    public static String getConfigName() {
        Path config = Paths.get("KAMILastConfig.txt");
        String kamiConfigName = KAMI_CONFIG_NAME_DEFAULT;
        try(BufferedReader reader = Files.newBufferedReader(config)) {
            kamiConfigName = reader.readLine();
            if (!isFilenameValid(kamiConfigName)) kamiConfigName = KAMI_CONFIG_NAME_DEFAULT;
        } catch (NoSuchFileException e) {
            try(BufferedWriter writer = Files.newBufferedWriter(config)) {
                writer.write(KAMI_CONFIG_NAME_DEFAULT);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return kamiConfigName;
    }

    public static void loadConfiguration() {
        try {
            loadConfigurationUnsafe();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadConfigurationUnsafe() throws IOException {
        final String kamiConfigName = getConfigName();
        final Path kamiConfig = Paths.get(kamiConfigName, new String[0]);
        if (!Files.exists(kamiConfig, new LinkOption[0])) {
            return;
        }
        Configuration.loadConfiguration(kamiConfig);
        final JsonObject gui = KamiMod.INSTANCE.guiStateSetting.getValue();
        for (final Map.Entry<String, JsonElement> entry : gui.entrySet()) {
            final Optional<Component> optional = KamiMod.INSTANCE.guiManager.getChildren().stream().filter(component -> component instanceof Frame).filter(component -> component.getTitle().equals(entry.getKey())).findFirst();
            if (optional.isPresent()) {
                final JsonObject object = entry.getValue().getAsJsonObject();
                final Frame frame = (Frame) optional.get();
                frame.setX(object.get("x").getAsInt());
                frame.setY(object.get("y").getAsInt());
                final Docking docking = Docking.values()[object.get("docking").getAsInt()];
                if (docking.isLeft()) {
                    ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.LEFT);
                }
                else if (docking.isRight()) {
                    ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.RIGHT);
                }
                else if (docking.isCenterVertical()) {
                    ContainerHelper.setAlignment(frame, AlignedComponent.Alignment.CENTER);
                }
                frame.setDocking(docking);
                frame.setMinimized(object.get("minimized").getAsBoolean());
                frame.setPinned(object.get("pinned").getAsBoolean());
            }
            else {
                System.err.println("Found GUI config entry for " + entry.getKey() + ", but found no frame with that name");
            }
        }
        getInstance().getGuiManager().getChildren().stream().filter(component -> component instanceof Frame && ((Frame) component).isPinneable() && component.isVisible()).forEach(component -> component.setOpacity(0.0f));
    }

    public static void saveConfiguration() {
        try {
            saveConfigurationUnsafe();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfigurationUnsafe() throws IOException {
        final JsonObject object = new JsonObject();
        final JsonObject[] frameObject = new JsonObject[1];
        final JsonObject jsonObject = null;
        KamiMod.INSTANCE.guiManager.getChildren().stream().filter(component -> component instanceof Frame).map(component -> component).forEach(frame -> {
            frameObject[0] = new JsonObject();
            frameObject[0].add("x", (JsonElement)new JsonPrimitive((Number)frame.getX()));
            frameObject[0].add("y", (JsonElement)new JsonPrimitive((Number)frame.getY()));
            frameObject[0].add("docking", (JsonElement)new JsonPrimitive((Number)Arrays.asList(Docking.values()).indexOf(frame.getDocking())));
            frameObject[0].add("minimized", (JsonElement)new JsonPrimitive(Boolean.valueOf(frame.isMinimized())));
            frameObject[0].add("pinned", (JsonElement)new JsonPrimitive(Boolean.valueOf(frame.isPinned())));
            jsonObject.add(frame.getTitle(), (JsonElement) frameObject[0]);
            return;
        });
        KamiMod.INSTANCE.guiStateSetting.setValue(object);
        final Path outputFile = Paths.get(getConfigName(), new String[0]);
        if (!Files.exists(outputFile, new LinkOption[0])) {
            Files.createFile(outputFile, (FileAttribute<?>[])new FileAttribute[0]);
        }
        Configuration.saveConfiguration(outputFile);
        ModuleManager.getModules().forEach(Module::destroy);
    }

    public static boolean isFilenameValid(final String file) {
        final File f = new File(file);
        try {
            f.getCanonicalPath();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    public static KamiMod getInstance() {
        return KamiMod.INSTANCE;
    }

    public KamiGUI getGuiManager() {
        return this.guiManager;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    static {
        log = LogManager.getLogger("KAMI");
        EVENT_BUS = new EventManager();
    }
}
