/*     */ package me.zeroeightsix.kami.module.modules.combat;
/*     */ 
/*     */ import me.zeroeightsix.kami.command.Command;
/*     */ import me.zeroeightsix.kami.module.Module;
/*     */ import me.zeroeightsix.kami.module.Module.Info;
/*     */ import me.zeroeightsix.kami.setting.Setting;
/*     */ import me.zeroeightsix.kami.setting.Settings;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.NonNullList;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Info(name = "InvPreview", category = Module.Category.COMBAT, description = "View Inventory")
/*     */ public class InvPreview
/*     */   extends Module
/*     */ {
/*  27 */   private Setting<Integer> optionX = register(Settings.i("X", 198));
/*  28 */   private Setting<Integer> optionY = register(Settings.i("Y", 469));
/*     */ 
/*     */   
/*     */   private static void preboxrender() {
/*  32 */     GL11.glPushMatrix();
/*  33 */     GlStateManager.pushMatrix();
/*  34 */     GlStateManager.disableAlpha();
/*  35 */     GlStateManager.clear(256);
/*  36 */     GlStateManager.enableBlend();
/*     */   }
/*     */   
/*     */   private static void postboxrender() {
/*  40 */     GlStateManager.disableBlend();
/*  41 */     GlStateManager.disableDepth();
/*  42 */     GlStateManager.disableLighting();
/*  43 */     GlStateManager.enableDepth();
/*  44 */     GlStateManager.enableAlpha();
/*  45 */     GlStateManager.popMatrix();
/*  46 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   private static void preitemrender() {
/*  50 */     GL11.glPushMatrix();
/*  51 */     GL11.glDepthMask(true);
/*  52 */     GlStateManager.clear(256);
/*  53 */     GlStateManager.disableDepth();
/*  54 */     GlStateManager.enableDepth();
/*  55 */     RenderHelper.enableStandardItemLighting();
/*  56 */     GlStateManager.scale(1.0F, 1.0F, 0.01F);
/*     */   }
/*     */   
/*     */   private static void postitemrender() {
/*  60 */     GlStateManager.scale(1.0F, 1.0F, 1.0F);
/*  61 */     RenderHelper.disableStandardItemLighting();
/*  62 */     GlStateManager.enableAlpha();
/*  63 */     GlStateManager.disableBlend();
/*  64 */     GlStateManager.disableLighting();
/*  65 */     GlStateManager.scale(0.5D, 0.5D, 0.5D);
/*  66 */     GlStateManager.disableDepth();
/*  67 */     GlStateManager.enableDepth();
/*  68 */     GlStateManager.scale(2.0F, 2.0F, 2.0F);
/*  69 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   public void onEnable() {
/*  73 */     if (mc.player != null) {
/*  74 */       Command.sendChatMessage("[InvPreview] Right click the module to move it around");
/*     */     }
/*  76 */     else if (mc.player == null) {
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender() {
/*  83 */     NonNullList<ItemStack> items = mc.player.inventory.mainInventory;
/*  84 */     boxrender(((Integer)this.optionX.getValue()).intValue(), ((Integer)this.optionY.getValue()).intValue());
/*  85 */     itemrender(items, ((Integer)this.optionX.getValue()).intValue(), ((Integer)this.optionY.getValue()).intValue());
/*     */   }
/*     */   
/*     */   private void boxrender(int x, int y) {
/*  89 */     preboxrender();
/*  90 */     mc.renderEngine.bindTexture(box);
/*  91 */     mc.ingameGUI.drawTexturedModalRect(x, y, 7, 17, 162, 54);
/*  92 */     postboxrender();
/*     */   }
/*     */   
/*     */   private void itemrender(NonNullList<ItemStack> items, int x, int y) {
/*  96 */     for (int size = items.size(), item = 9; item < size; item++) {
/*  97 */       int slotx = x + 1 + item % 9 * 18;
/*  98 */       int sloty = y + 1 + (item / 9 - 1) * 18;
/*  99 */       preitemrender();
/* 100 */       mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)items.get(item), slotx, sloty);
/* 101 */       mc.getRenderItem().renderItemOverlays(mc.fontRenderer, (ItemStack)items.get(item), slotx, sloty);
/* 102 */       postitemrender();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 107 */   private static final ResourceLocation box = new ResourceLocation("textures/gui/container/generic_54.png");
/*     */ }


/* Location:              C:\Users\frank\Desktop\elementars.com_b4.jar!\me\zeroeightsix\kami\module\modules\render\InvPreview.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.2
 */