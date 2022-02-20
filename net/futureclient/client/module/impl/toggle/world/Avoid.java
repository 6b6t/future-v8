/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.module.impl.toggle.world;

import me.friendly.api.event.Listener;
import me.friendly.exeter.events.BlockBoundingBoxEvent;
import me.friendly.exeter.module.ModuleType;
import me.friendly.exeter.module.ToggleableModule;
import me.friendly.exeter.properties.Property;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;

public final class Avoid
extends ToggleableModule {
    private final Property<Boolean> fire = new Property<Boolean>(true, "Fire", "f");
    private final Property<Boolean> cactus = new Property<Boolean>(true, "Cactus", "c");

    public Avoid() {
        super("Avoid", new String[]{"avoid"}, -10561537, ModuleType.WORLD);
        this.offerProperties(this.fire, this.cactus);
        this.listeners.add(new Listener<BlockBoundingBoxEvent>("avoid_block_bounding_box_listener"){

            @Override
            public void call(BlockBoundingBoxEvent event) {
                if (!((Avoid)Avoid.this).minecraft.gameSettings.keyBindJump.getIsKeyPressed() && ((Avoid)Avoid.this).minecraft.thePlayer.onGround && (event.getBlock().getMaterial().equals(Material.fire) && ((Boolean)Avoid.this.fire.getValue()).booleanValue() || event.getBlock() instanceof BlockCactus && ((Boolean)Avoid.this.cactus.getValue()).booleanValue())) {
                    event.setBoundingBox(AxisAlignedBB.fromBounds(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ(), (double)event.getBlockPos().getX() + 1.0, (double)event.getBlockPos().getY() + 1.0, (double)event.getBlockPos().getZ() + 1.0));
                }
            }
        });
    }
}

