// 
// Decompiled by Procyon v0.5.36
// 

package me.zeroeightsix.kami.module.modules.render;

import net.minecraft.util.math.Vec3d;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import me.zeroeightsix.kami.util.MathUtil;
import me.zeroeightsix.kami.util.KamiTessellator;
import me.zeroeightsix.kami.event.events.RenderEvent;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import me.zeroeightsix.kami.setting.Settings;
import me.zero.alpine.listener.EventHandler;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.setting.Setting;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.Module.Category;
@Module.Info(name = "BreakESP", category = Category.COMBAT)
public class BreakESP extends Module
{
    private ArrayList<BlockPos> breaking;
    private ArrayList<BlockPos> lastrender;
    BlockPos pos;
    private Setting<BreakESPMode> mode;
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    private Setting<Integer> alpha;
    @EventHandler
    public Listener<PacketEvent.Receive> listener;
    
    public BreakESP() {
        this.breaking = new ArrayList<BlockPos>();
        this.lastrender = new ArrayList<BlockPos>();
        this.mode = this.register(Settings.e("Mode", BreakESPMode.SOLID));
        this.red = this.register((Setting<Integer>)Settings.integerBuilder("Red").withRange(0, 255).withValue(255).build());
        this.green = this.register((Setting<Integer>)Settings.integerBuilder("Green").withRange(0, 255).withValue(0).build());
        this.blue = this.register((Setting<Integer>)Settings.integerBuilder("Blue").withRange(0, 255).withValue(0).build());
        this.alpha = this.register((Setting<Integer>)Settings.integerBuilder("Transparency").withRange(0, 255).withValue(70).build());
        this.listener = new Listener<PacketEvent.Receive>(event -> {
            if (event.getPacket() instanceof SPacketBlockBreakAnim) {
                (this.breaking = new ArrayList<BlockPos>()).add(((SPacketBlockBreakAnim)event.getPacket()).getPosition());
            }
            if (event.getPacket() instanceof CPacketPlayerDigging) {
                (this.breaking = new ArrayList<BlockPos>()).add(((CPacketPlayerDigging)event.getPacket()).getPosition());
            }
            return;
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
        this.breaking = new ArrayList<BlockPos>();
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        final IBlockState[] iBlockState3 = new IBlockState[1];
        final Vec3d[] interp3 = new Vec3d[1];
        final IBlockState[] iBlockState4 = new IBlockState[1];
        final Vec3d[] interp4 = new Vec3d[1];
        this.breaking.forEach(blockPos -> {
            this.lastrender.add(blockPos);
            switch (this.mode.getValue()) {
                case SOLID: {
KamiTessellator.prepare(7);
KamiTessellator.drawBox(blockPos, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue(), 63);
KamiTessellator.release();
break;
}
                case FULL: {
                    iBlockState3[0] = BreakESP.mc.world.getBlockState(blockPos);
                    interp3[0] = MathUtil.interpolateEntity((Entity)BreakESP.mc.player, BreakESP.mc.getRenderPartialTicks());
                    KamiTessellator.drawFullBox(iBlockState3[0].getSelectedBoundingBox((World)BreakESP.mc.world, blockPos).grow(0.0020000000949949026).offset(-interp3[0].x, -interp3[0].y, -interp3[0].z), blockPos, 1.5f, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
                    break;
                }
                case OUTLINE: {
                    iBlockState4[0] = BreakESP.mc.world.getBlockState(blockPos);
                    interp4[0] = MathUtil.interpolateEntity((Entity)BreakESP.mc.player, BreakESP.mc.getRenderPartialTicks());
                    KamiTessellator.drawBoundingBox(iBlockState4[0].getSelectedBoundingBox((World)BreakESP.mc.world, blockPos).grow(0.0020000000949949026).offset(-interp4[0].x, -interp4[0].y, -interp4[0].z), 1.5f, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
                    break;
                }
                default: {
                    KamiTessellator.prepare(7);
                    KamiTessellator.drawBox(blockPos, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue(), 63);
                    KamiTessellator.release();
                    break;
                }
            }
        });
    }
    
    private enum BreakESPMode
    {
        SOLID, 
        FULL, 
        OUTLINE;
    }
}
