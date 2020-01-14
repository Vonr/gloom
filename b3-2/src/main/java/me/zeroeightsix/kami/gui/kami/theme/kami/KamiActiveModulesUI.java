/*     */ package me.zeroeightsix.kami.gui.kami.theme.kami;
/*     */
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import me.zeroeightsix.kami.command.Command;
/*     */ import me.zeroeightsix.kami.gui.font.CFontRenderer;
/*     */ import me.zeroeightsix.kami.gui.kami.component.ActiveModules;
/*     */ import me.zeroeightsix.kami.gui.rgui.component.AlignedComponent;
/*     */ import me.zeroeightsix.kami.gui.rgui.component.Component;
/*     */ import me.zeroeightsix.kami.gui.rgui.render.AbstractComponentUI;
/*     */ import me.zeroeightsix.kami.gui.rgui.render.font.FontRenderer;
/*     */ import me.zeroeightsix.kami.module.Module;
/*     */ import me.zeroeightsix.kami.module.ModuleManager;
/*     */ import me.zeroeightsix.kami.setting.Setting;
/*     */ import me.zeroeightsix.kami.util.ColourUtils;
/*     */ import me.zeroeightsix.kami.util.Wrapper;
/*     */ import org.lwjgl.opengl.GL11;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ public class KamiActiveModulesUI
        /*     */   extends AbstractComponentUI<ActiveModules>
        /*     */ {
    /*  32 */   CFontRenderer cFontRenderer = new CFontRenderer(new Font("Verdana", 0, 18), true, false);
    /*     */   public boolean rainbowBG;
    /*     */   public int redForBG;
    /*     */   public int greenForBG;
    /*     */   public int blueForBG;
    /*     */
    /*     */   private void checkSettingGuiColour(Setting setting) {
        /*  39 */     String name = setting.getName();
        /*  40 */     switch (name) {
            /*     */       case "Red":
                /*  42 */         this.redForBG = ((Integer)setting.getValue()).intValue();
                /*     */         break;
            /*     */
            /*     */       case "Green":
                /*  46 */         this.greenForBG = ((Integer)setting.getValue()).intValue();
                /*     */         break;
            /*     */
            /*     */       case "Blue":
                /*  50 */         this.blueForBG = ((Integer)setting.getValue()).intValue();
                /*     */         break;
            /*     */     }
        /*     */   }
    /*     */
    /*     */   private void checkRainbowSetting(Setting setting) {
        /*  56 */     String name = setting.getName();
        /*  57 */     switch (name) {
            /*     */       case "Rainbow":
                /*  59 */         this.rainbowBG = ((Boolean)setting.getValue()).booleanValue();
                /*     */         break;
            /*     */     }
        /*     */   }
    /*     */   public void renderComponent(ActiveModules component, FontRenderer f) {
        /*     */     Function<Integer, Integer> xFunc;
        /*  65 */     GL11.glDisable(2884);
        /*  66 */     GL11.glEnable(3042);
        /*  67 */     GL11.glEnable(3553);
        /*     */
        /*  69 */     FontRenderer renderer = Wrapper.getFontRenderer();
        /*     */
        /*     */
        /*     */
        /*     */
        /*  74 */     List<Module> mods = (List<Module>)ModuleManager.getModules().stream().filter(Module::isEnabled).sorted(Comparator.comparing(module -> Integer.valueOf(this.cFontRenderer.getStringWidth(module.getName() + ((module.getHudInfo() == null) ? "" : (module.getHudInfo() + " "))) * (component.sort_up ? -1 : 1)))).collect(Collectors.toList());
        /*     */
        /*  76 */     int[] y = { 2 };
        /*     */
        /*  78 */     if (component.getParent().getY() < 26 && Wrapper.getPlayer().getActivePotionEffects().size() > 0 && component.getParent().getOpacity() == 0.0F) {
            /*  79 */       y[0] = Math.max(component.getParent().getY(), 26 - component.getParent().getY());
            /*     */     }
        /*  81 */     float[] hue = { (float)(System.currentTimeMillis() % 11520L) / 11520.0F };
        /*     */
        /*  83 */     boolean lAlign = (component.getAlignment() == AlignedComponent.Alignment.LEFT);
        /*     */
        /*  85 */     switch (component.getAlignment()) {
            /*     */       case RIGHT:
                /*  87 */         xFunc = (i -> Integer.valueOf(component.getWidth() - i.intValue()));
                /*     */         break;
            /*     */       case CENTER:
                /*  90 */         xFunc = (i -> Integer.valueOf(component.getWidth() / 2 - i.intValue() / 2));
                /*     */         break;
            /*     */
            /*     */       default:
                /*  94 */         xFunc = (i -> Integer.valueOf(0));
                /*     */         break;
            /*     */     }
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /*     */
        /* 117 */     for (Module module : mods) {
            /* 118 */       (ModuleManager.getModuleByName("Gui")).settingList.forEach(setting -> checkSettingGuiColour(setting));
            /* 119 */       (ModuleManager.getModuleByName("Gui")).settingList.forEach(setting -> checkRainbowSetting(setting));
            /* 120 */       int rgb = Color.HSBtoRGB(hue[0], 1.0F, 1.0F);
            /* 121 */       String s = module.getHudInfo();
            /* 122 */       String text = module.getName() + ((s == null) ? "" : (" " + Command.SECTIONSIGN() + "7" + s));
            /*     */
            /* 124 */       int textwidth = this.cFontRenderer.getStringWidth(text);
            /* 125 */       int textheight = renderer.getFontHeight() + 1;
            /* 126 */       int red = rgb >> 16 & 0xFF;
            /* 127 */       int green = rgb >> 8 & 0xFF;
            /* 128 */       int blue = rgb & 0xFF;
            /* 129 */       int trgb = ColourUtils.toRGBA(red, green, blue, 255);
            /*     */
            /* 131 */       if (this.rainbowBG) {
                /* 132 */         this.cFontRenderer.drawStringWithShadow(text, ((Integer)xFunc.apply(Integer.valueOf(textwidth))).intValue(), y[0], trgb);
                /*     */       } else {
                /* 134 */         this.cFontRenderer.drawStringWithShadow(text, ((Integer)xFunc.apply(Integer.valueOf(textwidth))).intValue(), y[0], ColourUtils.toRGBA(this.redForBG, this.greenForBG, this.blueForBG, 255));
                /*     */       }
            /*     */
            /* 137 */       hue[0] = hue[0] + 0.02F;
            /* 138 */       y[0] = y[0] + textheight;
            /*     */     }
        /* 140 */     component.setHeight(y[0]);
        /*     */
        /* 142 */     GL11.glEnable(2884);
        /* 143 */     GL11.glDisable(3042);
        /*     */   }
    /*     */
    /*     */   public void handleSizeComponent(ActiveModules component) {
        /* 147 */     component.setWidth(100);
        /* 148 */     component.setHeight(100);
        /*     */   }
    /*     */ }


/* Location:              C:\Users\frank\Desktop\MODS\elementars.com_b4.jar!\me\zeroeightsix\kami\gui\kami\theme\kami\KamiActiveModulesUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.2
 */