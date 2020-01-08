/*     */ package me.zeroeightsix.kami.module.modules.combat;
/*     */ 
/*     */ import java.util.List;
/*     */ import me.zeroeightsix.kami.command.Command;
/*     */ import me.zeroeightsix.kami.module.Module;
/*     */ import me.zeroeightsix.kami.module.Module.Info;
/*     */ import me.zeroeightsix.kami.module.ModuleManager;
/*     */ import me.zeroeightsix.kami.setting.Setting;
/*     */ import me.zeroeightsix.kami.setting.Settings;
/*     */ import me.zeroeightsix.kami.util.BlockInteractionHelper;
/*     */ import me.zeroeightsix.kami.util.EntityUtil;
/*     */ import me.zeroeightsix.kami.util.Friends;
/*     */ import me.zeroeightsix.kami.util.Wrapper;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.CPacketAnimation;
/*     */ import net.minecraft.network.play.client.CPacketEntityAction;
/*     */ import net.minecraft.network.play.client.CPacketHeldItemChange;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.util.math.Vec3i;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Info(name = "WebAura", category = Module.Category.COMBAT)
/*     */ public class WebAuraMin
/*     */   extends Module
/*     */ {
/*  46 */   private Setting<Double> range = register(Settings.d("Range", 5.5D));
/*  47 */   private Setting<Double> blockPerTick = register(Settings.d("Blocks per Tick", 8.0D));
/*  48 */   private Setting<Boolean> spoofRotations = register(Settings.b("Spoof Rotations", false));
/*  49 */   private Setting<Boolean> spoofHotbar = register(Settings.b("Spoof Hotbar", false));
/*  50 */   private Setting<Boolean> debugMessages = register(Settings.b("Debug Messages", false));
/*  51 */   private final Vec3d[] offsetList = new Vec3d[] { new Vec3d(0.0D, 2.0D, 0.0D), new Vec3d(0.0D, 1.0D, 0.0D), new Vec3d(0.0D, 0.0D, 0.0D) };
/*     */   private boolean slowModeSwitch = false;
/*  53 */   private int playerHotbarSlot = -1; private EntityPlayer closestTarget;
/*  54 */   private int lastHotbarSlot = -1;
/*  55 */   private int offsetStep = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  60 */     if (isDisabled() || mc.player == null || ModuleManager.isModuleEnabled("Freecam")) {
/*     */       return;
/*     */     }
/*  63 */     if (this.closestTarget == null) {
/*     */       return;
/*     */     }
/*  66 */     if (this.slowModeSwitch) {
/*  67 */       this.slowModeSwitch = false;
/*     */       return;
/*     */     } 
/*  70 */     for (int i = 0; i < (int)Math.floor(((Double)this.blockPerTick.getValue()).doubleValue()); i++) {
/*  71 */       if (((Boolean)this.debugMessages.getValue()).booleanValue()) {
/*  72 */         Command.sendChatMessage("[WebAuraMin] Loop iteration: " + this.offsetStep);
/*     */       }
/*  74 */       if (this.offsetStep >= this.offsetList.length) {
/*  75 */         endLoop();
/*     */         return;
/*     */       } 
/*  78 */       Vec3d offset = this.offsetList[this.offsetStep];
/*  79 */       placeBlock((new BlockPos(this.closestTarget.getPositionVector())).down().add(offset.x, offset.y, offset.z));
/*  80 */       this.offsetStep++;
/*     */     } 
/*  82 */     this.slowModeSwitch = true;
/*     */   }
/*     */   
/*     */   private void placeBlock(BlockPos blockPos) {
/*  86 */     if (!Wrapper.getWorld().getBlockState(blockPos).getMaterial().isReplaceable()) {
/*  87 */       if (((Boolean)this.debugMessages.getValue()).booleanValue()) {
/*  88 */         Command.sendChatMessage("[WebAuraMin] Block is already placed, skipping");
/*     */       }
/*     */       return;
/*     */     } 
/*  92 */     if (!BlockInteractionHelper.checkForNeighbours(blockPos)) {
/*     */       return;
/*     */     }
/*  95 */     placeBlockExecute(blockPos);
/*     */   }
/*     */   
/*     */   public void placeBlockExecute(BlockPos pos) {
/*  99 */     Vec3d eyesPos = new Vec3d((Wrapper.getPlayer()).posX, (Wrapper.getPlayer()).posY + Wrapper.getPlayer().getEyeHeight(), (Wrapper.getPlayer()).posZ);
/* 100 */     for (EnumFacing side : EnumFacing.values()) {
/* 101 */       BlockPos neighbor = pos.offset(side);
/* 102 */       EnumFacing side2 = side.getOpposite();
/* 103 */       if (!BlockInteractionHelper.canBeClicked(neighbor)) {
/* 104 */         if (((Boolean)this.debugMessages.getValue()).booleanValue()) {
/* 105 */           Command.sendChatMessage("[WebAuraMin] No neighbor to click at!");
/*     */         }
/*     */       } else {
/*     */         
/* 109 */         Vec3d hitVec = (new Vec3d((Vec3i)neighbor)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(side2.getDirectionVec())).scale(0.5D));
/* 110 */         if (eyesPos.squareDistanceTo(hitVec) > 18.0625D) {
/* 111 */           if (((Boolean)this.debugMessages.getValue()).booleanValue()) {
/* 112 */             Command.sendChatMessage("[WebAuraMin] Distance > 4.25 blocks!");
/*     */           }
/*     */         } else {
/*     */           
/* 116 */           if (((Boolean)this.spoofRotations.getValue()).booleanValue()) {
/* 117 */             BlockInteractionHelper.faceVectorPacketInstant(hitVec);
/*     */           }
/* 119 */           boolean needSneak = false;
/* 120 */           Block blockBelow = mc.world.getBlockState(neighbor).getBlock();
/* 121 */           if (BlockInteractionHelper.blackList.contains(blockBelow) || BlockInteractionHelper.shulkerList.contains(blockBelow)) {
/* 122 */             if (((Boolean)this.debugMessages.getValue()).booleanValue()) {
/* 123 */               Command.sendChatMessage("[WebAuraMin] Sneak enabled!");
/*     */             }
/* 125 */             needSneak = true;
/*     */           } 
/* 127 */           if (needSneak) {
/* 128 */             mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SNEAKING));
/*     */           }
/* 130 */           int obiSlot = findObiInHotbar();
/* 131 */           if (obiSlot == -1) {
/* 132 */             if (((Boolean)this.debugMessages.getValue()).booleanValue()) {
/* 133 */               Command.sendChatMessage("[WebAuraMin] No Obi in Hotbar, disabling!");
/*     */             }
/* 135 */             disable();
/*     */             return;
/*     */           } 
/* 138 */           if (this.lastHotbarSlot != obiSlot) {
/* 139 */             if (((Boolean)this.debugMessages.getValue()).booleanValue()) {
/* 140 */               Command.sendChatMessage("[WebAuraMin Setting Slot to Obi at  = " + obiSlot);
/*     */             }
/* 142 */             if (((Boolean)this.spoofHotbar.getValue()).booleanValue()) {
/* 143 */               mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(obiSlot));
/*     */             } else {
/*     */               
/* 146 */               (Wrapper.getPlayer()).inventory.currentItem = obiSlot;
/*     */             } 
/* 148 */             this.lastHotbarSlot = obiSlot;
/*     */           } 
/* 150 */           mc.playerController.processRightClickBlock(Wrapper.getPlayer(), mc.world, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
/* 151 */           mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
/* 152 */           if (needSneak) {
/* 153 */             if (((Boolean)this.debugMessages.getValue()).booleanValue()) {
/* 154 */               Command.sendChatMessage("[WebAurav] Sneak disabled!");
/*     */             }
/* 156 */             mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
/*     */           } 
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int findObiInHotbar() {
/* 165 */     int slot = -1;
/* 166 */     for (int i = 0; i < 9; i++) {
/* 167 */       ItemStack stack = (Wrapper.getPlayer()).inventory.getStackInSlot(i);
/* 168 */       if (stack != ItemStack.EMPTY && 
/* 169 */         stack.getItem() instanceof ItemBlock) {
/* 170 */         Block block = ((ItemBlock)stack.getItem()).getBlock();
/* 171 */         if (block instanceof net.minecraft.block.BlockWeb) {
/* 172 */           slot = i;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 178 */     return slot;
/*     */   }
/*     */   
/*     */   private void findTarget() {
/* 182 */     List<EntityPlayer> playerList = (Wrapper.getWorld()).playerEntities;
/* 183 */     for (EntityPlayer target : playerList) {
/* 184 */       if (target == mc.player) {
/*     */         continue;
/*     */       }
/* 187 */       if (Friends.isFriend(target.getName())) {
/*     */         continue;
/*     */       }
/* 190 */       if (!EntityUtil.isLiving((Entity)target)) {
/*     */         continue;
/*     */       }
/* 193 */       if (target.getHealth() <= 0.0F) {
/*     */         continue;
/*     */       }
/* 196 */       double currentDistance = Wrapper.getPlayer().getDistance((Entity)target);
/* 197 */       if (currentDistance > ((Double)this.range.getValue()).doubleValue()) {
/*     */         continue;
/*     */       }
/* 200 */       if (this.closestTarget == null) {
/* 201 */         this.closestTarget = target;
/*     */         continue;
/*     */       } 
/* 204 */       if (currentDistance >= Wrapper.getPlayer().getDistance((Entity)this.closestTarget)) {
/*     */         continue;
/*     */       }
/* 207 */       this.closestTarget = target;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void endLoop() {
/* 213 */     this.offsetStep = 0;
/* 214 */     if (((Boolean)this.debugMessages.getValue()).booleanValue()) {
/* 215 */       Command.sendChatMessage("[WebAuraMin] Ending Loop");
/*     */     }
/* 217 */     if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
/* 218 */       if (((Boolean)this.debugMessages.getValue()).booleanValue()) {
/* 219 */         Command.sendChatMessage("[WebAuraMin] Setting Slot back to  = " + this.playerHotbarSlot);
/*     */       }
/* 221 */       if (((Boolean)this.spoofHotbar.getValue()).booleanValue()) {
/* 222 */         mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.playerHotbarSlot));
/*     */       } else {
/*     */         
/* 225 */         (Wrapper.getPlayer()).inventory.currentItem = this.playerHotbarSlot;
/*     */       } 
/* 227 */       this.lastHotbarSlot = this.playerHotbarSlot;
/*     */     } 
/* 229 */     findTarget();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onEnable() {
/* 234 */     if (mc.player == null) {
/* 235 */       disable();
/*     */       return;
/*     */     } 
/* 238 */     if (((Boolean)this.debugMessages.getValue()).booleanValue()) {
/* 239 */       Command.sendChatMessage("[WebAuraMin] Enabling");
/*     */     }
/* 241 */     this.playerHotbarSlot = (Wrapper.getPlayer()).inventory.currentItem;
/* 242 */     this.lastHotbarSlot = -1;
/* 243 */     if (((Boolean)this.debugMessages.getValue()).booleanValue()) {
/* 244 */       Command.sendChatMessage("[WebAuraMin] Saving initial Slot  = " + this.playerHotbarSlot);
/*     */     }
/* 246 */     findTarget();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onDisable() {
/* 251 */     if (mc.player == null) {
/*     */       return;
/*     */     }
/* 254 */     if (((Boolean)this.debugMessages.getValue()).booleanValue()) {
/* 255 */       Command.sendChatMessage("[WebAuraMin] Disabling");
/*     */     }
/* 257 */     if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
/* 258 */       if (((Boolean)this.debugMessages.getValue()).booleanValue()) {
/* 259 */         Command.sendChatMessage("[WebAuraMin] Setting Slot to  = " + this.playerHotbarSlot);
/*     */       }
/* 261 */       if (((Boolean)this.spoofHotbar.getValue()).booleanValue()) {
/* 262 */         mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.playerHotbarSlot));
/*     */       } else {
/*     */         
/* 265 */         (Wrapper.getPlayer()).inventory.currentItem = this.playerHotbarSlot;
/*     */       } 
/*     */     } 
/* 268 */     this.playerHotbarSlot = -1;
/* 269 */     this.lastHotbarSlot = -1;
/*     */   }
/*     */ }


