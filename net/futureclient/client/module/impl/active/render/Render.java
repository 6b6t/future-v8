/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.module.impl.active.render;

import com.gitlab.nuf.api.event.Listener;
import com.gitlab.nuf.api.minecraft.render.RenderMethods;
import com.gitlab.nuf.exeter.core.Exeter;
import com.gitlab.nuf.exeter.events.RenderGameInfoEvent;
import com.gitlab.nuf.exeter.events.RenderGameOverlayEvent;
import com.gitlab.nuf.exeter.events.ViewmodelEvent;
import com.gitlab.nuf.exeter.module.Module;
import com.gitlab.nuf.exeter.properties.Property;
import net.minecraft.client.gui.ScaledResolution;

public final class Render
extends Module {
    private final Property<Boolean> customBars = new Property<Boolean>(true, "CustomBars", "c", "custom", "cb");
    private final Property<Boolean> nofov = new Property<Boolean>(false, "NoFOV", "nf");
    private final Property<Boolean> pumpkin = new Property<Boolean>(true, "NoPumpkin", "p", "np");
    private final Property<Boolean> fire = new Property<Boolean>(true, "NoFire", "fire", "nf");
    private final Property<Boolean> hurtcam = new Property<Boolean>(false, "NoHurtcam", "hurtcam", "nh");
    private final Property<Boolean> items = new Property<Boolean>(false, "NoItems", "items", "item", "ni");

    public Render() {
        super("Render", new String[]{"render", "norender", "nr"});
        this.offerProperties(this.pumpkin, this.customBars, this.fire, this.hurtcam, this.items, this.nofov);
        Exeter.getInstance().getEventManager().register(new Listener<RenderGameOverlayEvent>("norender_render_game_overlay_listener"){

            @Override
            public void call(RenderGameOverlayEvent event) {
                switch (event.getType()) {
                    case FIRE: {
                        event.setRenderFire((Boolean)Render.this.fire.getValue());
                        break;
                    }
                    case HURTCAM: {
                        event.setRenderHurtcam((Boolean)Render.this.hurtcam.getValue());
                        break;
                    }
                    case ITEM: {
                        event.setRenderItems((Boolean)Render.this.items.getValue());
                        if (!((Boolean)Render.this.items.getValue()).booleanValue()) break;
                        ((Render)Render.this).minecraft.theWorld.removeEntity(event.getEntityItem());
                        break;
                    }
                    case PUMPKIN: {
                        event.setRenderPumpkin((Boolean)Render.this.pumpkin.getValue());
                    }
                }
            }
        });
        Exeter.getInstance().getEventManager().register(new Listener<RenderGameInfoEvent>("textgui_render_game_info_listener"){

            @Override
            public void call(RenderGameInfoEvent event) {
                ScaledResolution scaledResolution = event.getScaledResolution();
                if (((Boolean)Render.this.customBars.getValue()).booleanValue()) {
                    int minusHealth = (int)((Render)Render.this).minecraft.thePlayer.getHealth();
                    int minusFood = ((Render)Render.this).minecraft.thePlayer.getFoodStats().getFoodLevel();
                    if (minusFood > 20) {
                        minusFood = 20;
                    }
                    if (minusHealth > 20) {
                        minusHealth = 20;
                    }
                    RenderMethods.drawGradientBorderedRect(scaledResolution.getScaledWidth() / 2 - 91, scaledResolution.getScaledHeight() - 39, scaledResolution.getScaledWidth() / 2 - 10, scaledResolution.getScaledHeight() - 30, 1.0f, -587202560, -12303292, -14540254);
                    RenderMethods.drawGradientBorderedRect(scaledResolution.getScaledWidth() / 2 - 91, scaledResolution.getScaledHeight() - 39, scaledResolution.getScaledWidth() / 2 - 90 + minusHealth * 4, scaledResolution.getScaledHeight() - 30, 1.0f, -587202560, -577175552, -582746112);
                    ((Render)Render.this).minecraft.fontRenderer.drawStringWithShadow(String.format("%s", (int)((Render)Render.this).minecraft.thePlayer.getHealth()), scaledResolution.getScaledWidth() / 2 - 56, scaledResolution.getScaledHeight() - 38, -1);
                    RenderMethods.drawGradientBorderedRect(scaledResolution.getScaledWidth() / 2 + 10, scaledResolution.getScaledHeight() - 39, scaledResolution.getScaledWidth() / 2 + 91, scaledResolution.getScaledHeight() - 30, 1.0f, -587202560, -12303292, -14540254);
                    RenderMethods.drawGradientBorderedRect(scaledResolution.getScaledWidth() / 2 + 90 - minusFood * 4, scaledResolution.getScaledHeight() - 39, scaledResolution.getScaledWidth() / 2 + 91, scaledResolution.getScaledHeight() - 30, 1.0f, -587202560, -586565120, -586849536);
                    ((Render)Render.this).minecraft.fontRenderer.drawStringWithShadow(String.format("%s", ((Render)Render.this).minecraft.thePlayer.getFoodStats().getFoodLevel()), scaledResolution.getScaledWidth() / 2 + 45, scaledResolution.getScaledHeight() - 38, -1);
                }
            }
        });
        Exeter.getInstance().getEventManager().register(new Listener<ViewmodelEvent>("textgui_view_model_listener"){

            @Override
            public void call(ViewmodelEvent event) {
                if (((Boolean)Render.this.nofov.getValue()).booleanValue()) {
                    event.setCanceled(true);
                }
            }
        });
    }
}

