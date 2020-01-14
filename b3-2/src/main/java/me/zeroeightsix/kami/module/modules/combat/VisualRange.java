/*    */ package me.zeroeightsix.kami.module.modules.combat;
/*    */ 
/*    */ import com.mojang.realmsclient.gui.ChatFormatting;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import me.zeroeightsix.kami.command.Command;
/*    */ import me.zeroeightsix.kami.module.Module;
/*    */ import me.zeroeightsix.kami.module.Module.Info;
/*    */ import me.zeroeightsix.kami.setting.Setting;
/*    */ import me.zeroeightsix.kami.setting.Settings;
/*    */ import me.zeroeightsix.kami.util.Friends;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.CPacketChatMessage;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Info(name = "VisualRange", description = "Reports Players in VisualRange", category = Module.Category.COMBAT)
/*    */ public class VisualRange
/*    */   extends Module
/*    */ {
/* 24 */   private Setting<Boolean> publicChat = register(Settings.b("PublicChat", false));
/* 25 */   private Setting<Boolean> leaving = register(Settings.b("Leaving", false));
/*    */ 
/*    */   
/*    */   private List<String> knownPlayers;
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 32 */     if (mc.player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 36 */     List<String> tickPlayerList = new ArrayList<>();
/*    */     
/* 38 */     for (Entity entity : mc.world.getLoadedEntityList()) {
/* 39 */       if (entity instanceof net.minecraft.entity.player.EntityPlayer) {
/* 40 */         tickPlayerList.add(entity.getName());
/*    */       }
/*    */     } 
/*    */     
/* 44 */     if (tickPlayerList.size() > 0) {
/* 45 */       for (String playerName : tickPlayerList) {
/* 46 */         if (playerName.equals(mc.player.getName())) {
/*    */           continue;
/*    */         }
/* 49 */         if (!this.knownPlayers.contains(playerName)) {
/* 50 */           this.knownPlayers.add(playerName);
/* 51 */           if (((Boolean)this.publicChat.getValue()).booleanValue()) {
/* 52 */             mc.player.connection.sendPacket((Packet)new CPacketChatMessage("Oh hey, there is " + playerName + " in my range! This announcement was presented by: " + "\u00A72\u0262\u029F\u1D0F\u1D0F\u1D0D \u1D04\u029F\u026A\u1D07\u0274\u1D1B"));
/*    */           }
/* 54 */           else if (Friends.isFriend(playerName)) {
/* 55 */             sendNotification("[VisualRange] " + ChatFormatting.GREEN.toString() + playerName + ChatFormatting.RESET.toString() + " entered the Battlefield!");
/*    */           } else {
/* 57 */             sendNotification("[VisualRange] " + ChatFormatting.RED.toString() + playerName + ChatFormatting.RESET.toString() + " entered the Battlefield!");
/*    */           } 
/*    */           
/*    */           return;
/*    */         } 
/*    */       } 
/*    */     }
/*    */     
/* 65 */     if (this.knownPlayers.size() > 0) {
/* 66 */       for (String playerName : this.knownPlayers) {
/* 67 */         if (!tickPlayerList.contains(playerName)) {
/* 68 */           this.knownPlayers.remove(playerName);
/* 69 */           if (((Boolean)this.leaving.getValue()).booleanValue()) {
/* 70 */             if (((Boolean)this.publicChat.getValue()).booleanValue()) {
/* 71 */               mc.player.connection.sendPacket((Packet)new CPacketChatMessage("I cant see " + playerName + " anymore! This announcement was presented by: " + "\u00A72\u0262\u029F\u1D0F\u1D0F\u1D0D \u1D04\u029F\u026A\u1D07\u0274\u1D1B"));
/*    */             }
/* 73 */             else if (Friends.isFriend(playerName)) {
/* 74 */               sendNotification("[VisualRange] " + ChatFormatting.GREEN.toString() + playerName + ChatFormatting.RESET.toString() + " left the Battlefield!");
/*    */             } else {
/* 76 */               sendNotification("[VisualRange] " + ChatFormatting.RED.toString() + playerName + ChatFormatting.RESET.toString() + " left the Battlefield!");
/*    */             } 
/*    */           }
/*    */           return;
/*    */         } 
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 88 */   private void sendNotification(String s) { Command.sendChatMessage(s); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 93 */   public void onEnable() { this.knownPlayers = new ArrayList<>(); }
/*    */ }


