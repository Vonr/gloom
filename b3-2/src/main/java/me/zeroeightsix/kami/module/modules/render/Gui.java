/*    */ package me.zeroeightsix.kami.module.modules.render;
/*    */ 
/*    */ import me.zeroeightsix.kami.module.Module;
/*    */ import me.zeroeightsix.kami.module.Module.Info;
/*    */ import me.zeroeightsix.kami.setting.Setting;
/*    */ import me.zeroeightsix.kami.setting.Settings;
/*    */ import me.zeroeightsix.kami.util.ColourUtils;
/*    */ 
/*    */ @Info(name = "Gui", category = Module.Category.RENDER, description = "Changes options with the gui")
/*    */ public class Gui
/*    */   extends Module {
/* 12 */   public Setting<Boolean> Rainbow = register(Settings.booleanBuilder("Rainbow").withValue(Boolean.valueOf(false)).build());
/* 13 */   public Setting<Boolean> RainbowWatermark = register(Settings.booleanBuilder("Rainbow Watermark").withValue(Boolean.valueOf(false)).build());
/* 14 */   public Setting<Integer> Ared = register((Setting)Settings.integerBuilder("Red").withRange(Integer.valueOf(0), Integer.valueOf(255)).withValue(Integer.valueOf(0)).build());
/* 15 */   public Setting<Integer> Agreen = register((Setting)Settings.integerBuilder("Green").withRange(Integer.valueOf(0), Integer.valueOf(255)).withValue(Integer.valueOf(0)).build());
/* 16 */   public Setting<Integer> Ablue = register((Setting)Settings.integerBuilder("Blue").withRange(Integer.valueOf(0), Integer.valueOf(255)).withValue(Integer.valueOf(255)).build());
/* 17 */   public Setting<Float> Bred = register((Setting)Settings.floatBuilder("Border Red").withRange(Float.valueOf(0.0F), Float.valueOf(1.0F)).withValue(Float.valueOf(0.0F)).build());
/* 18 */   public Setting<Float> Bgreen = register((Setting)Settings.floatBuilder("Border Green").withRange(Float.valueOf(0.0F), Float.valueOf(1.0F)).withValue(Float.valueOf(0.0F)).build());
/* 19 */   public Setting<Float> Bblue = register((Setting)Settings.floatBuilder("Border Blue").withRange(Float.valueOf(0.0F), Float.valueOf(1.0F)).withValue(Float.valueOf(1.0F)).build());
/*    */ 
/*    */   
/*    */   public int getArgb() {
/* 23 */     int argb = ColourUtils.toRGBA(((Integer)this.Ared.getValue()).intValue(), ((Integer)this.Agreen.getValue()).intValue(), ((Integer)this.Ablue.getValue()).intValue(), 255);
/* 24 */     return argb;
/*    */   }
/*    */ }


