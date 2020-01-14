/*    */ package me.zeroeightsix.kami.module.modules.combat;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
/*    */ import java.util.WeakHashMap;
/*    */ import me.zeroeightsix.kami.command.Command;
/*    */ import me.zeroeightsix.kami.module.Module;
/*    */ import me.zeroeightsix.kami.module.Module.Info;
/*    */ import me.zeroeightsix.kami.setting.Setting;
/*    */ import me.zeroeightsix.kami.setting.Settings;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.MobEffects;
/*    */ 
/*    */ @Info(name = "StrengthDetect", category = Module.Category.COMBAT, description = "Detects when players have Strength 2")
/*    */ public class StrengthDetect extends Module {
/* 17 */   private Setting<Boolean> watermark = register(Settings.b("Watermark", true));
/* 18 */   private Setting<Boolean> color = register(Settings.b("Color", false));
/*    */   
/* 20 */   private Set<EntityPlayer> str = Collections.newSetFromMap(new WeakHashMap<>());
/* 21 */   public static final Minecraft mc = Minecraft.getMinecraft();
/*    */   
/*    */   public void onUpdate() {
/* 24 */     for (EntityPlayer player : mc.world.playerEntities) {
/* 25 */       if (player.equals(mc.player))
/* 26 */         continue;  if (player.isPotionActive(MobEffects.STRENGTH) && !this.str.contains(player)) {
/* 27 */         if (((Boolean)this.watermark.getValue()).booleanValue()) {
/* 28 */           if (((Boolean)this.color.getValue()).booleanValue()) {
/* 29 */             Command.sendChatMessage("&a" + player.getDisplayNameString() + "has drank strength thanks to Gloom Client!");
/*    */           } else {
/* 31 */             Command.sendChatMessage(player.getDisplayNameString() + " has drank strength thanks to Gloom Client!");
/*    */           } 
/* 33 */         } else if (((Boolean)this.color.getValue()).booleanValue()) {
/* 34 */           Command.sendRawChatMessage("&a" + player.getDisplayNameString() + " has drank strength thanks to Gloom Client!");
/*    */         } else {
/* 36 */           Command.sendRawChatMessage(player.getDisplayNameString() + " has drank strength thanks to Gloom Client!");
/* 37 */         }  this.str.add(player);
/*    */       } 
/* 39 */       if (!this.str.contains(player) || player.isPotionActive(MobEffects.STRENGTH))
/* 40 */         continue;  if (((Boolean)this.watermark.getValue()).booleanValue()) {
/* 41 */         if (((Boolean)this.color.getValue()).booleanValue()) {
/* 42 */           Command.sendChatMessage("&c" + player.getDisplayNameString() + " ran out of strength thanks to Gloom Client!");
/*    */         } else {
/* 44 */           Command.sendChatMessage(player.getDisplayNameString() + " ran out of strength thanks to Gloom Client!");
/*    */         } 
/* 46 */       } else if (((Boolean)this.color.getValue()).booleanValue()) {
/* 47 */         Command.sendRawChatMessage("&c" + player.getDisplayNameString() + " ran out of strength thanks to Gloom Client!");
/*    */       } else {
/* 49 */         Command.sendRawChatMessage(player.getDisplayNameString() + " ran out of strength thanks to Gloom Client!");
/* 50 */       }  this.str.remove(player);
/*    */     } 
/*    */   }
/*    */ }


