// 
// Decompiled by Procyon v0.5.36
// 

package me.zeroeightsix.kami.module.modules.combat;

import java.util.Objects;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import me.zeroeightsix.kami.util.EntityUtil;
import net.minecraft.world.World;
import net.minecraft.network.play.client.CPacketUseEntity;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import me.zero.alpine.listener.EventHandler;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.setting.Setting;
import java.util.concurrent.ConcurrentHashMap;
import me.zeroeightsix.kami.module.Module;

@Module.Info(name = "AutoGG", category = Module.Category.COMBAT, description = "Announce killed Players")
public class AutoGG extends Module
{
    private ConcurrentHashMap<String, Integer> targetedPlayers;
    private Setting<Boolean> toxicMode;
    private Setting<Boolean> clientName;
    private Setting<Integer> timeoutTicks;
    @EventHandler
    public Listener<PacketEvent.Send> sendListener;
    @EventHandler
    public Listener<LivingDeathEvent> livingDeathEventListener;
    
    public AutoGG() {
        this.targetedPlayers = null;
        this.toxicMode = this.register(Settings.b("ToxicMode", false));
        this.clientName = this.register(Settings.b("ClientName", true));
        this.timeoutTicks = this.register(Settings.i("TimeoutTicks", 20));
        final CPacketUseEntity[] cPacketUseEntity = new CPacketUseEntity[1];
        final Entity[] targetEntity = new Entity[1];
        this.sendListener = new Listener<PacketEvent.Send>(event -> {
            if (AutoGG.mc.player == null) {
                return;
            }
            else {
                if (this.targetedPlayers == null) {
                    this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
                }
                if (!(event.getPacket() instanceof CPacketUseEntity)) {
                    return;
                }
                else {
                    cPacketUseEntity[0] = (CPacketUseEntity)event.getPacket();
                    if (!cPacketUseEntity[0].getAction().equals((Object)CPacketUseEntity.Action.ATTACK)) {
                        return;
                    }
                    else {
                        targetEntity[0] = cPacketUseEntity[0].getEntityFromWorld((World)AutoGG.mc.world);
                        if (!EntityUtil.isPlayer(targetEntity[0])) {
                            return;
                        }
                        else {
                            this.addTargetedPlayer(targetEntity[0].getName());
                            return;
                        }
                    }
                }
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
        final EntityLivingBase[] entity = new EntityLivingBase[1];
        final AtomicReference<EntityPlayer>[] player = new AtomicReference[]{null};
        final AtomicReference<String>[] name = new AtomicReference[]{null};
        this.livingDeathEventListener = new Listener<LivingDeathEvent>(event -> {
            if (AutoGG.mc.player != null) {
                if (this.targetedPlayers == null) {
                    this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
                }
                entity[0] = event.getEntityLiving();
                if (entity[0] != null) {
                    if (!(!EntityUtil.isPlayer((Entity) entity[0]))) {
                        player[0].set((EntityPlayer) entity[0]);
                        if (player[0].get().getHealth() <= 0.0f) {
                            name[0].set(player[0].get().getName());
                            if (this.shouldAnnounce(name[0].get())) {
                                this.doAnnounce(name[0].get());
                            }
                        }
                    }
                }
            }
        }, (Predicate<LivingDeathEvent>[])new Predicate[0]);
    }
    
    public void onEnable() {
        this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
    }
    
    public void onDisable() {
        this.targetedPlayers = null;
    }
    
    @Override
    public void onUpdate() {
        if (this.isDisabled() || AutoGG.mc.player == null) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
        }
        for (final Entity entity : AutoGG.mc.world.getLoadedEntityList()) {
            if (!EntityUtil.isPlayer(entity)) {
                continue;
            }
            final EntityPlayer player = (EntityPlayer)entity;
            if (player.getHealth() > 0.0f) {
                continue;
            }
            final String name2 = player.getName();
            if (this.shouldAnnounce(name2)) {
                this.doAnnounce(name2);
                break;
            }
        }
        this.targetedPlayers.forEach((name, timeout) -> {
            if (timeout <= 0) {
                this.targetedPlayers.remove(name);
            }
            else {
                this.targetedPlayers.put(name, timeout - 1);
            }
        });
    }
    
    private boolean shouldAnnounce(final String name) {
        return this.targetedPlayers.containsKey(name);
    }
    
    private void doAnnounce(final String name) {
        this.targetedPlayers.remove(name);
        final StringBuilder message = new StringBuilder();
        if (this.toxicMode.getValue()) {
            message.append("EZZZ");
        }
        else {
            message.append("Good Fight ");
        }
        message.append(name);
        message.append("!");
        if (this.clientName.getValue()) {
            message.append(" ");
            message.append("Gloom Client");
            message.append(" owns me and all");
        }
        String messageSanitized = message.toString().replaceAll("\u00A7", "");
        if (messageSanitized.length() > 255) {
            messageSanitized = messageSanitized.substring(0, 255);
        }
        AutoGG.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(messageSanitized));
    }
    
    public void addTargetedPlayer(final String name) {
        if (Objects.equals(name, AutoGG.mc.player.getName())) {
            return;
        }
        if (this.targetedPlayers == null) {
            this.targetedPlayers = new ConcurrentHashMap<String, Integer>();
        }
        this.targetedPlayers.put(name, this.timeoutTicks.getValue());
    }
}
