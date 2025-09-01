/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.util;

import com.mclegoman.viewpoint.client.config.PerspectiveConfigHelper;
import com.mclegoman.viewpoint.client.experimental.PerspectiveExperimental;
import com.mclegoman.viewpoint.client.overlays.PerspectiveHUDOverlays;
import com.mclegoman.viewpoint.client.panorama.PerspectivePanorama;
import com.mclegoman.viewpoint.client.perspective.PerspectivePerspective;
import com.mclegoman.viewpoint.client.shaders.PerspectiveShader;
import com.mclegoman.viewpoint.client.zoom.PerspectiveZoom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

@Environment(EnvType.CLIENT)
public class PerspectiveTick {
    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PerspectivePerspective.tick(client);
            PerspectiveShader.tick(client);
            PerspectiveZoom.tick(client);
            PerspectiveConfigHelper.tick(client);
            PerspectivePanorama.tick(client);
            PerspectiveExperimental.tick(client);
            PerspectiveHUDOverlays.tick(client);
        });
    }
}