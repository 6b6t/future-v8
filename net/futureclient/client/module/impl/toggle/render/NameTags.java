/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.toggle.render;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.minecraft.render.RenderMethods;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.events.PassSpecialRenderEvent;
import com.gitlab.nuf.exeter.events.RenderEvent;
import com.gitlab.nuf.exeter.module.ModuleType;
import com.gitlab.nuf.exeter.module.ToggleableModule;
import com.gitlab.nuf.exeter.properties.EnumProperty;
import com.gitlab.nuf.exeter.properties.NumberProperty;
import com.gitlab.nuf.exeter.properties.Property;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumChatFormatting;

public final class NameTags
extends ToggleableModule {
    private final Property<Boolean> armor = new Property<Boolean>(true, "Armor", "a");
    private final Property<Boolean> health = new Property<Boolean>(true, "Health", "h");
    private final NumberProperty<Float> scaling = new NumberProperty<Float>(Float.valueOf(0.003f), Float.valueOf(0.001f), Float.valueOf(0.01f), "Scaling", "scale", "s");
    private final NumberProperty<Float> width = new NumberProperty<Float>(Float.valueOf(1.6f), Float.valueOf(1.0f), Float.valueOf(5.0f), "Width", "w");
    private final EnumProperty<Health> healthLook = new EnumProperty<Health>(Health.TWENTY, "HealthLook", "look");

    public NameTags() {
        super("Name Tags", new String[]{"nametags", "np", "nt", "tags", "plates", "nameplates", "nametag"}, ModuleType.RENDER);
        this.offerProperties(this.armor, this.health, this.scaling, this.width, this.healthLook);
        this.listeners.add(new Listener<RenderEvent>("name_tags_render_listener"){

            @Override
            public void call(RenderEvent event) {
                for (Object o : ((NameTags)NameTags.this).minecraft.theWorld.playerEntities) {
                    Entity entity = (Entity)o;
                    if (!(entity instanceof EntityPlayer) || entity == ((NameTags)NameTags.this).minecraft.thePlayer || !entity.isEntityAlive()) continue;
                    double x2 = NameTags.this.interpolate(entity.lastTickPosX, entity.posX, event.getPartialTicks()) - ((NameTags)NameTags.this).minecraft.getRenderManager().renderPosX;
                    double y2 = NameTags.this.interpolate(entity.lastTickPosY, entity.posY, event.getPartialTicks()) - ((NameTags)NameTags.this).minecraft.getRenderManager().renderPosY;
                    double z2 = NameTags.this.interpolate(entity.lastTickPosZ, entity.posZ, event.getPartialTicks()) - ((NameTags)NameTags.this).minecraft.getRenderManager().renderPosZ;
                    NameTags.this.renderNameTag((EntityPlayer)entity, x2, y2, z2, event.getPartialTicks());
                }
            }
        });
        this.listeners.add(new Listener<PassSpecialRenderEvent>("name_tags_pass_special_render_listener"){

            @Override
            public void call(PassSpecialRenderEvent event) {
                event.setCanceled(true);
            }
        });
        this.setRunning(true);
    }

    private void renderNameTag(EntityPlayer player, double x2, double y2, double z2, float delta) {
        double tempY = y2;
        tempY += player.isSneaking() ? 0.5 : 0.7;
        Entity camera = this.minecraft.getRenderViewEntity();
        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
        double distance = camera.getDistance(x2 + this.minecraft.getRenderManager().viewerPosX, y2 + this.minecraft.getRenderManager().viewerPosY, z2 + this.minecraft.getRenderManager().viewerPosZ);
        int width = this.minecraft.fontRenderer.getStringWidth(this.getDisplayName(player)) / 2;
        double scale = 0.0018 + (double)((Float)this.scaling.getValue()).floatValue() * distance;
        if (distance <= 8.0) {
            scale = 0.0245;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x2, (float)tempY + 1.4f, (float)z2);
        GlStateManager.rotate(-this.minecraft.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(this.minecraft.getRenderManager().playerViewX, this.minecraft.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        RenderMethods.drawBorderedRectReliant(-width - 2, -(this.minecraft.fontRenderer.FONT_HEIGHT + 1), (float)width + 2.0f, 1.5f, ((Float)this.width.getValue()).floatValue(), 0x77000000, 0x55000000);
        GlStateManager.enableAlpha();
        this.minecraft.fontRenderer.drawStringWithShadow(this.getDisplayName(player), -width, -(this.minecraft.fontRenderer.FONT_HEIGHT - 1), this.getDisplayColour(player));
        if (this.armor.getValue().booleanValue()) {
            ItemStack stack;
            int index;
            GlStateManager.pushMatrix();
            int xOffset = 0;
            for (index = 3; index >= 0; --index) {
                stack = player.inventory.armorInventory[index];
                if (stack == null) continue;
                xOffset -= 8;
            }
            if (player.getCurrentEquippedItem() != null) {
                xOffset -= 8;
                ItemStack renderStack = player.getCurrentEquippedItem().copy();
                if (renderStack.hasEffect() && (renderStack.getItem() instanceof ItemTool || renderStack.getItem() instanceof ItemArmor)) {
                    renderStack.stackSize = 1;
                }
                this.renderItemStack(renderStack, xOffset, -26);
                xOffset += 16;
            }
            for (index = 3; index >= 0; --index) {
                stack = player.inventory.armorInventory[index];
                if (stack == null) continue;
                ItemStack armourStack = stack.copy();
                if (armourStack.hasEffect() && (armourStack.getItem() instanceof ItemTool || armourStack.getItem() instanceof ItemArmor)) {
                    armourStack.stackSize = 1;
                }
                this.renderItemStack(armourStack, xOffset, -26);
                xOffset += 16;
            }
            GlStateManager.popMatrix();
        }
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }

    private void renderItemStack(ItemStack stack, int x2, int y2) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        this.minecraft.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableAlpha();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableAlpha();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        this.minecraft.getRenderItem().renderItemAndEffectIntoGUI(stack, x2, y2);
        this.minecraft.getRenderItem().renderItemOverlays(this.minecraft.fontRenderer, stack, x2, y2);
        this.minecraft.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        this.renderEnchantmentText(stack, x2, y2);
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }

    private void renderEnchantmentText(ItemStack stack, int x2, int y2) {
        int enchantmentY = y2 - 24;
        if (stack.getEnchantmentTagList() != null && stack.getEnchantmentTagList().tagCount() >= 6) {
            this.minecraft.fontRenderer.drawString("god", x2 * 2, enchantmentY, -43177);
            return;
        }
        int color = -5592406;
        if (stack.getItem() instanceof ItemArmor) {
            int protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.PROTECTION.effectId, stack);
            int projectileProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.PROJECTILE_PROTECTION.effectId, stack);
            int blastProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.BLAST_PROTECTION.effectId, stack);
            int fireProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.FIRE_PROTECTION.effectId, stack);
            int thornsLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.THORNS.effectId, stack);
            int featherFallingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.FEATHER_FALLING.effectId, stack);
            if (protectionLevel > 0) {
                this.minecraft.fontRenderer.drawString("pr" + protectionLevel, x2 * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (projectileProtectionLevel > 0) {
                this.minecraft.fontRenderer.drawString("pp" + projectileProtectionLevel, x2 * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (blastProtectionLevel > 0) {
                this.minecraft.fontRenderer.drawString("bp" + blastProtectionLevel, x2 * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (fireProtectionLevel > 0) {
                this.minecraft.fontRenderer.drawString("fp" + fireProtectionLevel, x2 * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (thornsLevel > 0) {
                this.minecraft.fontRenderer.drawString("tho" + thornsLevel, x2 * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (featherFallingLevel > 0) {
                this.minecraft.fontRenderer.drawString("ff" + featherFallingLevel, x2 * 2, enchantmentY, color);
                enchantmentY += 8;
            }
        }
        if (stack.getItem() instanceof ItemBow) {
            int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.POWER.effectId, stack);
            int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.PUNCH.effectId, stack);
            int flameLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.FLAME.effectId, stack);
            if (powerLevel > 0) {
                this.minecraft.fontRenderer.drawString("po" + powerLevel, x2 * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (punchLevel > 0) {
                this.minecraft.fontRenderer.drawString("pu" + punchLevel, x2 * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (flameLevel > 0) {
                this.minecraft.fontRenderer.drawString("fl" + flameLevel, x2 * 2, enchantmentY, color);
                enchantmentY += 8;
            }
        }
        if (stack.getItem() instanceof ItemSword) {
            int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.SHARPNESS.effectId, stack);
            int knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.KNOCKBACK.effectId, stack);
            int fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.FIRE_ASPECT.effectId, stack);
            if (sharpnessLevel > 0) {
                this.minecraft.fontRenderer.drawString("sh" + sharpnessLevel, x2 * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (knockbackLevel > 0) {
                this.minecraft.fontRenderer.drawString("kn" + knockbackLevel, x2 * 2, enchantmentY, color);
                enchantmentY += 8;
            }
            if (fireAspectLevel > 0) {
                this.minecraft.fontRenderer.drawString("fa" + fireAspectLevel, x2 * 2, enchantmentY, color);
                enchantmentY += 8;
            }
        }
        if (stack.getItem() == Items.golden_apple && stack.hasEffect()) {
            this.minecraft.fontRenderer.drawStringWithShadow("god", x2 * 2, enchantmentY, -3977919);
        }
    }

    private String getDisplayName(EntityPlayer player) {
        String name = player.getDisplayName().getFormattedText();
        if (Exeter.getInstance().getFriendManager().isFriend(player.getName())) {
            name = Exeter.getInstance().getFriendManager().getFriendByAliasOrLabel(player.getName()).getAlias();
        }
        if (name.contains(this.minecraft.getSession().getUsername())) {
            name = "You";
        }
        if (!this.health.getValue().booleanValue()) {
            return name;
        }
        float health = player.getHealth();
        EnumChatFormatting color = health > 18.0f ? EnumChatFormatting.GREEN : (health > 16.0f ? EnumChatFormatting.DARK_GREEN : (health > 12.0f ? EnumChatFormatting.YELLOW : (health > 8.0f ? EnumChatFormatting.GOLD : (health > 5.0f ? EnumChatFormatting.RED : EnumChatFormatting.DARK_RED))));
        switch ((Health)((Object)this.healthLook.getValue())) {
            case HUNDRED: {
                health *= 5.0f;
                break;
            }
            case TWENTY: {
                break;
            }
            case TEN: {
                health /= 2.0f;
            }
        }
        name = Math.floor(health) == (double)health ? name + (Object)((Object)color) + " " + (health > 0.0f ? Integer.valueOf((int)Math.floor(health)) : "dead") : name + (Object)((Object)color) + " " + (health > 0.0f ? Integer.valueOf((int)health) : "dead");
        if (this.healthLook.getValue() == Health.HUNDRED) {
            name = name + "%";
        }
        return name;
    }

    private int getDisplayColour(EntityPlayer player) {
        int colour = -5592406;
        if (Exeter.getInstance().getFriendManager().isFriend(player.getName())) {
            return -11157267;
        }
        if (player.isInvisible()) {
            colour = -1113785;
        } else if (player.isSneaking()) {
            colour = -6481515;
        }
        return colour;
    }

    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double)delta;
    }

    public static enum Health {
        HUNDRED,
        TWENTY,
        TEN;

    }
}

