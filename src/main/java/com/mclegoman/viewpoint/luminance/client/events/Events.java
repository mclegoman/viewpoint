/*
    Luminance
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/Luminance
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.luminance.client.events;

import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class Events {
	public static class GenericRegistry<K, V> {
		public final Map<K, V> registry = new HashMap<>();
		public void register(K key, V value) {
			if (!registry.containsKey(key)) registry.put(key, value);
		}
		public V get(K key) {
			return registry.get(key);
		}
		public void modify(K key, V value) {
			registry.replace(key, value);
		}
		public void remove(K key) {
			registry.remove(key);
		}
	}

	public static class Registry<T> extends GenericRegistry<Identifier, T> {}

	public static final Registry<ResourceReloader> ClientResourceReloaders = new Registry<>();
	public static final Registry<Runnable> AfterClientResourceReload = new Registry<>();

	public static final Registry<Runnables.InGameHudRender> BeforeInGameHudRender = new Registry<>();
	public static final Registry<Runnables.InGameHudRender> AfterInGameHudRender = new Registry<>();
	public static final Registry<Runnable> BeforeWorldRender = new Registry<>();
	public static final Registry<Runnables.GameRender> AfterWorldRender = new Registry<>();
	public static final Registry<Runnable> BeforeGameRender = new Registry<>();
	public static final Registry<Runnables.GameRender> AfterUiRender = new Registry<>();
	public static final Registry<Runnables.GameRender> AfterUiBackgroundRender = new Registry<>();
	public static final Registry<Runnables.GameRender> AfterPanoramaRender = new Registry<>();

	public static final Registry<Runnables.OnResized> OnResized = new Registry<>();
}
