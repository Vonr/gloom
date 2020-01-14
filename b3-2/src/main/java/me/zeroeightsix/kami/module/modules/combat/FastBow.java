 package me.zeroeightsix.kami.module.modules.combat;
 
 import me.zeroeightsix.kami.module.Module;
 import me.zeroeightsix.kami.module.Module.Info;
 import net.minecraft.network.Packet;
 import net.minecraft.network.play.client.CPacketPlayerDigging;
 import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
 import net.minecraft.util.math.BlockPos;
 
 
 
 
 @Info(name = "FastBow", description = "Fast Bow Release", category = Module.Category.COMBAT)
 public class FastBow
   extends Module
 {
   public void onUpdate() {
     if (mc.player.inventory.getCurrentItem().getItem() instanceof net.minecraft.item.ItemBow && mc.player
       .isHandActive() && mc.player.getItemInUseMaxCount() >= 3) {
       mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
       mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
       mc.player.stopActiveHand();
     } 
   }
 }


