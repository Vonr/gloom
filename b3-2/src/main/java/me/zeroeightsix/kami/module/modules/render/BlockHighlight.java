 package me.zeroeightsix.kami.module.modules.render;

 import me.zeroeightsix.kami.event.events.RenderEvent;
 import me.zeroeightsix.kami.module.Module;
 import me.zeroeightsix.kami.module.Module.Info;
 import me.zeroeightsix.kami.setting.Setting;
 import me.zeroeightsix.kami.setting.Settings;
 import me.zeroeightsix.kami.util.KamiTessellator;
 import me.zeroeightsix.kami.util.MathUtil;
 import net.minecraft.block.material.Material;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.client.Minecraft;
 import net.minecraft.entity.Entity;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.RayTraceResult;
 import net.minecraft.util.math.Vec3d;
 import net.minecraft.world.GameType;
 import net.minecraft.world.World;
 import net.minecraftforge.client.event.DrawBlockHighlightEvent;
 import net.minecraftforge.common.MinecraftForge;
 import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
 @Info(name = "BlockHighlight", category = Module.Category.RENDER, description = "Make selected block bounding box more visible")
 public class BlockHighlight
   extends Module
 {
   private static BlockPos position;
   private Setting<Integer> red = register((Setting)Settings.integerBuilder("Red").withRange(Integer.valueOf(0), Integer.valueOf(255)).withValue(Integer.valueOf(255)).build());
   private Setting<Integer> green = register((Setting)Settings.integerBuilder("Green").withRange(Integer.valueOf(0), Integer.valueOf(255)).withValue(Integer.valueOf(0)).build());
   private Setting<Integer> blue = register((Setting)Settings.integerBuilder("Blue").withRange(Integer.valueOf(0), Integer.valueOf(255)).withValue(Integer.valueOf(0)).build());
   private Setting<Integer> alpha = register((Setting)Settings.integerBuilder("Transparency").withRange(Integer.valueOf(0), Integer.valueOf(255)).withValue(Integer.valueOf(70)).build());
   private Setting<Float> width = register((Setting)Settings.floatBuilder("Thickness").withRange(Float.valueOf(1.0F), Float.valueOf(10.0F)).withValue(Float.valueOf(1.0F)).build());
   protected void onEnable() { MinecraftForge.EVENT_BUS.register(this); }
   protected void onDisable() {
     MinecraftForge.EVENT_BUS.unregister(this);
     position = null;
   }
  public void onWorldRender(RenderEvent event) {
     Minecraft mc = Minecraft.getMinecraft();
     RayTraceResult ray = mc.objectMouseOver;
     if (ray.typeOfHit == RayTraceResult.Type.BLOCK) {

       BlockPos blockpos = ray.getBlockPos();
       IBlockState iblockstate = mc.world.getBlockState(blockpos);

       if (iblockstate.getMaterial() != Material.AIR && mc.world.getWorldBorder().contains(blockpos)) {
         Vec3d interp = MathUtil.interpolateEntity((Entity)mc.player, mc.getRenderPartialTicks());
         KamiTessellator.drawBoundingBox(iblockstate.getSelectedBoundingBox((World)mc.world, blockpos).grow(0.0020000000949949026D).offset(-interp.x, -interp.y, -interp.z), ((Float)this.width.getValue()).floatValue(), ((Integer)this.red.getValue()).intValue(), ((Integer)this.green.getValue()).intValue(), ((Integer)this.blue.getValue()).intValue(), ((Integer)this.alpha.getValue()).intValue());
       }
     }
   }








   @SubscribeEvent
   public void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
     if (mc.player == null || mc.world == null || (
       !mc.playerController.getCurrentGameType().equals(GameType.SURVIVAL) &&
       !mc.playerController.getCurrentGameType().equals(GameType.CREATIVE))) {
       return;
     }
     event.setCanceled(true);
   }
 }


