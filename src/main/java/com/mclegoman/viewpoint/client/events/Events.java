/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.events;

import com.mclegoman.viewpoint.client.events.runnables.Runnables;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class Events {
	public static class GenericRegistry<K, V> {
		public final Map<K, V> registry = new HashMap<>();

		public void register(K key, V value) {
			if (!this.registry.containsKey(key)) {
				this.registry.put(key, value);
			}

		}

		public V get(K key) {
			return (V)this.registry.get(key);
		}

		public void modify(K key, V value) {
			this.registry.replace(key, value);
		}

		public void remove(K key) {
			this.registry.remove(key);
		}
	}

	public static class Registry<T> extends GenericRegistry<net.minecraft.util.Identifier, T> {
	}

	public static final Events.Registry<Runnables.Variable<Text>> Variables = new Events.Registry<>();
}
