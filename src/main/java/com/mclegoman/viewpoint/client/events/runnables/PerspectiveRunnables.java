/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.events.runnables;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class PerspectiveRunnables {
	public interface UseItem {
		void run(ItemStack stack, World world, PlayerEntity user, Hand hand);
	}
	public interface FinishUsingItem {
		void run(ItemStack stack, World world, LivingEntity user);
	}
	public interface Variable<T> {
		T call(String... args);
	}
}
