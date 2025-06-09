/*
    viewpoint
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/viewpoint
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.mixin.client.entity;

import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.WolfVariant;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(priority = 100, value = WolfEntity.class)
public interface WolfEntityAccessor {
	@Invoker("getVariant")
	RegistryEntry<WolfVariant> getVariant();
}