package com.mclegoman.viewpoint.mixin.client.hud.bars;

import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.Bar;
import net.minecraft.client.gui.hud.bar.LocatorBar;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.JumpingMount;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocatorBar.class)
public abstract class LocatorBarMixin implements Bar {
    @Shadow @Final private MinecraftClient client;
    @Inject(method = "renderBar", at = @At("HEAD"), cancellable = true)
    private void viewpoint$renderBar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (PerspectiveConfig.config.mergeXPLocatorBar.value()) {
            ClientPlayerEntity clientPlayerEntity = this.client.player;
            if (clientPlayerEntity != null) {
                JumpingMount jumpingMount = this.client.player.getJumpingMount();
                int i = this.getCenterX(this.client.getWindow());
                int j = this.getCenterY(this.client.getWindow());
                if (jumpingMount != null || this.client.player.getMountJumpStrength() > 0.0F) {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, Identifier.ofVanilla("hud/jump_bar_background"), i, j, 182, 5);
                    if (jumpingMount != null && jumpingMount.getJumpCooldown() > 0) {
                        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, Identifier.ofVanilla("hud/jump_bar_cooldown"), i, j, 182, 5);
                    } else {
                        int k = (int)(this.client.player.getMountJumpStrength() * 183.0F);
                        if (k > 0) {
                            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, Identifier.ofVanilla("hud/jump_bar_progress"), 182, 5, 0, 0, i, j, k, 5);
                        }
                    }
                } else {
                    int k = clientPlayerEntity.getNextLevelExperience();
                    if (k > 0) {
                        int l = (int)(clientPlayerEntity.experienceProgress * 183.0F);
                        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, Identifier.ofVanilla("hud/experience_bar_background"), i, j, 182, 5);
                        if (l > 0) {
                            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, Identifier.ofVanilla("hud/experience_bar_progress"), 182, 5, 0, 0, i, j, l, 5);
                        }
                    }
                }
                ci.cancel();
            }
        }
    }
}
