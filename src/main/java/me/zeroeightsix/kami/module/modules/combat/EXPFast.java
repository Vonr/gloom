 package me.zeroeightsix.kami.module.modules.combat;

 import me.zeroeightsix.kami.module.Module;
 import me.zeroeightsix.kami.module.Module.Info;







 @Info(name = "EXPFast", category = Module.Category.COMBAT, description = "Makes EXP Faster for PvP")
 public class EXPFast
   extends Module
 {
   public void onUpdate() {
     if (mc.player.inventory.getCurrentItem().getItem() instanceof net.minecraft.item.ItemExpBottle)
       mc.rightClickDelayTimer = 0;
   }
 }

