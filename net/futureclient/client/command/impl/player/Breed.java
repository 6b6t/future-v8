/*
 * Decompiled with CFR 0.152.
 */
package me.friendly.exeter.command.impl.player;

import me.friendly.exeter.command.Argument;
import me.friendly.exeter.command.Command;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public final class Breed
extends Command {
    public Breed() {
        super(new String[]{"breed"}, new Argument[0]);
    }

    @Override
    public String dispatch() {
        int counter = 0;
        block0: for (Object object : this.minecraft.theWorld.getLoadedEntityList()) {
            EntityAnimal entityAnimal;
            Entity entity = (Entity)object;
            if (!(entity instanceof EntityAnimal) || !this.isTargetValid(entityAnimal = (EntityAnimal)entity)) continue;
            for (int index = 36; index < 45; ++index) {
                ItemStack stack = this.minecraft.thePlayer.inventoryContainer.getSlot(index).getStack();
                if (stack == null || !entityAnimal.isBreedingItem(stack)) continue;
                this.minecraft.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(index - 36));
                this.minecraft.getNetHandler().addToSendQueue(new C02PacketUseEntity((Entity)entityAnimal, C02PacketUseEntity.Action.INTERACT));
                ++counter;
                if (this.minecraft.thePlayer.capabilities.isCreativeMode || --stack.stackSize > 0) continue block0;
                this.minecraft.thePlayer.inventory.setInventorySlotContents(index, null);
                continue block0;
            }
        }
        this.minecraft.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(this.minecraft.thePlayer.inventory.currentItem));
        return String.format("Bred %s animal%s.", counter, counter == 1 ? "" : "s");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isTargetValid(EntityAnimal animal) {
        if (animal.isChild()) return false;
        if (animal.isInLove()) return false;
        if (animal.getGrowingAge() != 0) return false;
        float f = this.minecraft.thePlayer.getDistanceToEntity(animal);
        int n = this.minecraft.thePlayer.canEntityBeSeen(animal) ? 6 : 3;
        if (!(f < (float)n)) return false;
        return true;
    }
}

