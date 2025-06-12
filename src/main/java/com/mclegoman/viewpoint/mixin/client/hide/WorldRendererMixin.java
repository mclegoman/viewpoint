/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.hide;

import com.mclegoman.viewpoint.client.hide.Hide;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(priority = 100, value = WorldRenderer.class)
public abstract class WorldRendererMixin {
	@ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawCuboidShapeOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/shape/VoxelShape;DDDFFFF)V"), method = "drawBlockOutline")
	private void perspective$drawBlockOutline(Args args) {
		if (Hide.getRainbowBlockOutline()) {
			int color = Hide.getRainbowOutline();
			// RED
			args.set(6, (color >> 16 & 0xFF) / 255.0F);
			// GREEN
			args.set(7, (color >> 8 & 0xFF) / 255.0F);
			// BLUE
			args.set(8, (color & 0xFF) / 255.0F);
		}
		// ALPHA
		args.set(9, (Hide.getBlockOutlineLevel() / 100.0F));
	}
}