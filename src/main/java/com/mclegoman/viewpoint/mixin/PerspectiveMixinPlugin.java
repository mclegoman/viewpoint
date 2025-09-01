/*
    Perspective
    Author: MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    License: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import com.mclegoman.viewpoint.common.data.PerspectiveData;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class PerspectiveMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        MixinExtrasBootstrap.init();
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.equals("com.mclegoman.viewpoint.mixin.client.shaders.PerspectiveShaderNamespaceFix")) {
            return !(PerspectiveData.isModInstalled("souper_secret_settings") || PerspectiveData.isModInstalled("architectury") || PerspectiveData.isModInstalled("satin") || PerspectiveData.isModInstalled("perspective"));
        }
        if (mixinClassName.equals("com.mclegoman.viewpoint.mixin.client.shaders.PerspectiveShaderTextureNamespaceFix")) {
            return !(PerspectiveData.isModInstalled("souper_secret_settings") || PerspectiveData.isModInstalled("perspective"));
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
