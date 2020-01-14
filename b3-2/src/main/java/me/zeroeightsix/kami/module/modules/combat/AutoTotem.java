 package me.zeroeightsix.kami.module.modules.combat;
 
 import me.zeroeightsix.kami.module.Module;
 import me.zeroeightsix.kami.module.Module.Info;
 import me.zeroeightsix.kami.setting.Setting;
 import me.zeroeightsix.kami.setting.Settings;
 import net.minecraft.entity.player.EntityPlayer;
 import net.minecraft.init.Items;
 import net.minecraft.inventory.ClickType;
 import net.minecraft.item.ItemStack;
 
 
 
 @Info(name = "AutoTotem", category = Module.Category.COMBAT)
 public class AutoTotem
   extends Module
 {
   int totems;
   boolean moving = false;
   boolean returnI = false;
   private Setting<Boolean> soft = register(Settings.b("Soft"));
 
   
   public void onUpdate() {
     if (mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiContainer) {
       return;
     }
     if (this.returnI) {
       int t = -1;
       for (int i = 0; i < 45; i++) {
         if ((mc.player.inventory.getStackInSlot(i)).isEmpty) {
           t = i;
           break;
         } 
       } 
       if (t == -1) {
         return;
       }
       mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
       this.returnI = false;
     } 
     this.totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.TOTEM_OF_UNDYING)).mapToInt(ItemStack::getCount).sum();
     if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
       this.totems++;
     } else {
       if (((Boolean)this.soft.getValue()).booleanValue() && !(mc.player.getHeldItemOffhand()).isEmpty) {
         return;
       }
       if (this.moving) {
         mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
         this.moving = false;
         if (!mc.player.inventory.itemStack.isEmpty()) {
           this.returnI = true;
         }
         return;
       } 
       if (mc.player.inventory.itemStack.isEmpty()) {
         if (this.totems == 0) {
           return;
         }
         int t = -1;
         for (int i = 0; i < 45; i++) {
           if (mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
             t = i;
             break;
           } 
         } 
         if (t == -1) {
           return;
         }
         mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
         this.moving = true;
       } else if (!((Boolean)this.soft.getValue()).booleanValue()) {
         int t = -1;
         for (int i = 0; i < 45; i++) {
           if ((mc.player.inventory.getStackInSlot(i)).isEmpty) {
             t = i;
             break;
           } 
         } 
         if (t == -1) {
           return;
         }
         mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
       } 
     } 
   }
 
   
   public void disableSoft() { this.soft.setValue(Boolean.valueOf(false)); }
 
 
 
   
   public String getHudInfo() { return String.valueOf(this.totems); }
 }


