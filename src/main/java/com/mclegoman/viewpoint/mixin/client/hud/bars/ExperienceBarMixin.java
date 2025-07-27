package com.mclegoman.viewpoint.mixin.client.hud.bars;

import com.mclegoman.viewpoint.client.config.PerspectiveConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.Bar;
import net.minecraft.client.gui.hud.bar.ExperienceBar;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.resource.waypoint.WaypointStyleAsset;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.waypoint.TrackedWaypoint;
import net.minecraft.world.waypoint.Waypoint;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceBar.class)
public abstract class ExperienceBarMixin implements Bar {
    @Unique
    private static final Identifier viewpoint$arrow_up = Identifier.ofVanilla("hud/locator_bar_arrow_up");
    @Unique
    private static final Identifier viewpoint$arrow_down = Identifier.ofVanilla("hud/locator_bar_arrow_down");
    @Shadow @Final private MinecraftClient client;
    @Inject(method = "renderAddons", at = @At("HEAD"))
    private void viewpoint$renderBar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (PerspectiveConfig.config.mergeXPLocatorBar.value()) {
            if (this.client.cameraEntity != null && this.client.player != null) {
                World world = this.client.cameraEntity.getWorld();
                this.client.player.networkHandler.getWaypointHandler().forEachWaypoint(this.client.cameraEntity, (waypoint) -> {
                    if (!(Boolean)waypoint.getSource().left().map((uuid) -> uuid.equals(this.client.cameraEntity.getUuid())).orElse(false)) {
                        double d = waypoint.getRelativeYaw(world, this.client.gameRenderer.getCamera());
                        if (!(d <= (double)-61.0F) && !(d > (double)60.0F)) {
                            int i = this.getCenterY(this.client.getWindow());
                            int j = MathHelper.ceil((float)(context.getScaledWindowWidth() - 9) / 2.0F);
                            Waypoint.Config config = waypoint.getConfig();
                            WaypointStyleAsset waypointStyleAsset = this.client.getWaypointStyleAssetManager().get(config.style);
                            float f = MathHelper.sqrt((float)waypoint.squaredDistanceTo(this.client.cameraEntity));
                            Identifier identifier = waypointStyleAsset.getSpriteForDistance(f);
                            int k = config.color.orElseGet(() -> waypoint.getSource().map((uuid) -> ColorHelper.withBrightness(ColorHelper.withAlpha(255, uuid.hashCode()), 0.9F), (name) -> ColorHelper.withBrightness(ColorHelper.withAlpha(255, name.hashCode()), 0.9F)));
                            int l = (int)(d * (double)173.0F / (double)2.0F / (double)60.0F);
                            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, identifier, j + l, i - 2, 9, 9, k);
                            TrackedWaypoint.Pitch pitch = waypoint.getPitch(world, this.client.gameRenderer);
                            if (pitch != TrackedWaypoint.Pitch.NONE) {
                                int m;
                                Identifier identifier2;
                                if (pitch == TrackedWaypoint.Pitch.DOWN) {
                                    m = 6;
                                    identifier2 = viewpoint$arrow_down;
                                } else {
                                    m = -6;
                                    identifier2 = viewpoint$arrow_up;
                                }
                                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, identifier2, j + l + 1, i + m, 7, 5);
                            }
                        }
                    }
                });
            }
        }
    }
}
