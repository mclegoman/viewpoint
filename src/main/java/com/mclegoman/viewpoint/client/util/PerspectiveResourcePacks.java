/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.util;

import com.mclegoman.viewpoint.client.translation.PerspectiveTranslation;
import com.mclegoman.viewpoint.common.data.PerspectiveData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class PerspectiveResourcePacks {
    /**
        When including resource packs with Perspective, register it here along with a comment with the following details:
        Resource Pack: ___________________
        Author: _________
        Github: github.com/_________
        Licence: _________
     **/
    public static void init() {
        /*
            Resource Pack: Perspective Default
            Author: MCLegoMan
            Github: https://github.com/MCLegoMan
            License: GNU LGPLv3
        */
        ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("viewpoint"), PerspectiveData.MOD_CONTAINER, PerspectiveTranslation.getTranslation("resource_pack.perspective_default"), ResourcePackActivationType.DEFAULT_ENABLED);
    }
}