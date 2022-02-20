//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\aesthetical\Documents\Minecraft\decomped\mappings\1.8.9"!

/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.world;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.minecraft.helper.PlayerHelper;
import com.gitlab.nuf.exeter.events.MotionUpdateEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.EnumProperty;
import com.gitlab.nuf.exeter.properties.Property;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.init.Items;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public final class AutoFarm
extends ToggleableModule {
    private final EnumProperty<Mode> mode = new EnumProperty<Mode>(Mode.HARVEST, "Mode", "m");
    private final Property<Boolean> silent = new Property<Boolean>(true, "Silent", "s");
    private BlockPos focus;

    public AutoFarm() {
        super("Auto Farm", new String[]{"autoharvest", "autoplant", "af"}, 245820, ModuleType.WORLD);
        this.offerProperties(this.mode, this.silent);
        this.listeners.add(new Listener<MotionUpdateEvent>("auto_farm_motion_update_listener"){

            @Override
            public void call(MotionUpdateEvent event) {
                AutoFarm.this.setTag(String.format("Auto %s", AutoFarm.this.mode.getFixedValue()));
                for (double y2 = event.getPositionY() + 6.0; y2 > event.getPositionY() - 6.0; y2 -= 1.0) {
                    for (double x2 = event.getPositionX() - 6.0; x2 < event.getPositionX() + 6.0; x2 += 1.0) {
                        for (double z2 = event.getPositionZ() - 6.0; z2 < event.getPositionZ() + 6.0; z2 += 1.0) {
                            BlockPos position = new BlockPos(x2, y2, z2);
                            if (!AutoFarm.this.isBlockValid(position)) continue;
                            if (AutoFarm.this.focus == null) {
                                AutoFarm.this.focus = position;
                                continue;
                            }
                            if (!(((AutoFarm)AutoFarm.this).minecraft.thePlayer.getDistance(AutoFarm.this.focus.getX(), AutoFarm.this.focus.getY(), AutoFarm.this.focus.getZ()) > ((AutoFarm)AutoFarm.this).minecraft.thePlayer.getDistance(x2, y2, z2))) continue;
                            AutoFarm.this.focus = position;
                        }
                    }
                }
                if (AutoFarm.this.focus == null || AutoFarm.this.mode.getValue() == Mode.PLANT && !AutoFarm.this.isStackValid(((AutoFarm)AutoFarm.this).minecraft.thePlayer.getCurrentEquippedItem())) {
                    return;
                }
                float[] rotations = AutoFarm.this.getRotations(AutoFarm.this.focus, AutoFarm.this.getFacingDirectionToPosition(AutoFarm.this.focus));
                event.setLockview((Boolean)AutoFarm.this.silent.getValue() == false);
                if (((Boolean)AutoFarm.this.silent.getValue()).booleanValue()) {
                    event.setRotationYaw(PlayerHelper.wrapAngleTo180(rotations[0]));
                    event.setRotationPitch(PlayerHelper.wrapAngleTo180(rotations[1]));
                } else {
                    ((AutoFarm)AutoFarm.this).minecraft.thePlayer.rotationYaw = PlayerHelper.wrapAngleTo180(rotations[0]);
                    ((AutoFarm)AutoFarm.this).minecraft.thePlayer.rotationPitch = PlayerHelper.wrapAngleTo180(rotations[1]);
                }
                if (AutoFarm.this.focus != null) {
                    if (AutoFarm.this.isBlockValid(AutoFarm.this.focus)) {
                        if (event.getTime() == MotionUpdateEvent.Time.AFTER) {
                            if (AutoFarm.this.mode.getValue() == Mode.PLANT) {
                                if (((AutoFarm)AutoFarm.this).minecraft.playerController.onPlayerRightClick(((AutoFarm)AutoFarm.this).minecraft.thePlayer, ((AutoFarm)AutoFarm.this).minecraft.theWorld, ((AutoFarm)AutoFarm.this).minecraft.thePlayer.getCurrentEquippedItem(), AutoFarm.this.focus, AutoFarm.this.getFacingDirectionToPosition(AutoFarm.this.focus), new Vec3(AutoFarm.this.focus.getX(), AutoFarm.this.focus.getY(), AutoFarm.this.focus.getZ()))) {
                                    ((AutoFarm)AutoFarm.this).minecraft.thePlayer.swingItem();
                                }
                            } else if (AutoFarm.this.mode.getValue() == Mode.HARVEST && ((AutoFarm)AutoFarm.this).minecraft.playerController.clickBlock(AutoFarm.this.focus, AutoFarm.this.getFacingDirectionToPosition(AutoFarm.this.focus))) {
                                ((AutoFarm)AutoFarm.this).minecraft.thePlayer.swingItem();
                            }
                        }
                    } else {
                        AutoFarm.this.focus = null;
                    }
                }
            }
        });
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        this.focus = null;
    }

    private boolean isBlockValid(BlockPos position) {
        boolean valid = false;
        Block target = this.minecraft.theWorld.getBlockState(position).getBlock();
        if (this.mode.getValue() == Mode.PLANT) {
            if (target instanceof BlockFarmland) {
                BlockFarmland farmland = (BlockFarmland)target;
                if (!(this.minecraft.theWorld.getBlockState(position.offsetUp()).getBlock() instanceof BlockCrops) && farmland.getMetaFromState(this.minecraft.theWorld.getBlockState(position)) == 7) {
                    valid = true;
                }
            } else if (target instanceof BlockSoulSand) {
                if (!(this.minecraft.theWorld.getBlockState(position.offsetUp()).getBlock() instanceof BlockNetherWart)) {
                    valid = true;
                }
            } else if ((target instanceof BlockDirt || target instanceof BlockGrass || target instanceof BlockSand) && !(this.minecraft.theWorld.getBlockState(position.offsetUp()).getBlock() instanceof BlockCrops) && !(this.minecraft.theWorld.getBlockState(position.offsetUp()).getBlock() instanceof BlockReed) && this.minecraft.theWorld.isAnyLiquid(target.getCollisionBoundingBox(this.minecraft.theWorld, position, this.minecraft.theWorld.getBlockState(position)))) {
                valid = true;
            }
        } else if (this.mode.getValue() == Mode.HARVEST) {
            if (target instanceof BlockCrops) {
                BlockCrops crops = (BlockCrops)target;
                valid = crops.getMetaFromState(this.minecraft.theWorld.getBlockState(position)) == 7;
            } else if (target instanceof BlockNetherWart) {
                BlockNetherWart wart = (BlockNetherWart)target;
                valid = wart.getMetaFromState(this.minecraft.theWorld.getBlockState(position)) == 3;
            } else if (target instanceof BlockReed) {
                valid = this.minecraft.theWorld.getBlockState(position.offsetDown()).getBlock() instanceof BlockReed;
            }
        }
        return valid && this.getFacingDirectionToPosition(position) != null && this.minecraft.thePlayer.getDistance(position.getX(), position.getY(), position.getZ()) < (double)this.minecraft.playerController.getBlockReachDistance() - 1.0;
    }

    private boolean isStackValid(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        Block block = this.minecraft.theWorld.getBlockState(this.focus).getBlock();
        return block instanceof BlockFarmland ? stack.getItem() instanceof ItemSeeds || stack.getItem() == Items.carrot || stack.getItem() == Items.potato : (block instanceof BlockSoulSand ? stack.getItem() == Items.nether_wart : (block instanceof BlockDirt || block instanceof BlockGrass || block instanceof BlockSand) && this.minecraft.theWorld.isAnyLiquid(block.getCollisionBoundingBox(this.minecraft.theWorld, this.focus, this.minecraft.theWorld.getBlockState(this.focus))) && stack.getItem() instanceof ItemReed);
    }

    private float[] getRotations(BlockPos position, EnumFacing facing) {
        double xDifference = (double)position.getX() + 0.5 + (double)facing.getDirectionVec().getX() * 0.25 - this.minecraft.thePlayer.posX;
        double yDifference = (double)position.getY() + 0.5 + (double)facing.getDirectionVec().getY() * 0.25 - this.minecraft.thePlayer.posY;
        double zDifference = (double)position.getZ() + 0.5 + (double)facing.getDirectionVec().getZ() * 0.25 - this.minecraft.thePlayer.posZ;
        double positions = MathHelper.sqrt_double(xDifference * xDifference + zDifference * zDifference);
        float yaw = (float)(Math.atan2(zDifference, xDifference) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(yDifference, positions) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    private EnumFacing getFacingDirectionToPosition(BlockPos position) {
        EnumFacing direction = null;
        if (!this.minecraft.theWorld.getBlockState(position.add(0, 1, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.UP;
        } else if (!this.minecraft.theWorld.getBlockState(position.add(0, -1, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.DOWN;
        } else if (!this.minecraft.theWorld.getBlockState(position.add(1, 0, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.EAST;
        } else if (!this.minecraft.theWorld.getBlockState(position.add(-1, 0, 0)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.WEST;
        } else if (!this.minecraft.theWorld.getBlockState(position.add(0, 0, 1)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.SOUTH;
        } else if (!this.minecraft.theWorld.getBlockState(position.add(0, 0, 1)).getBlock().isSolidFullCube()) {
            direction = EnumFacing.NORTH;
        }
        return direction;
    }

    public static enum Mode {
        PLANT,
        HARVEST;

    }
}

