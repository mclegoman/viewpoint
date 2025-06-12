/*
    Perspective
    Contributor(s): dannytaylor
    Github: https://github.com/mclegoman/perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.contributor;


import com.mclegoman.viewpoint.client.data.ClientData;
import com.mclegoman.viewpoint.client.events.PerspectiveEvents;
import com.mclegoman.viewpoint.common.util.Identifiers;
import com.mclegoman.viewpoint.luminance.common.util.Couple;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Contributor {
	/**
	 * Developers Notes:
	 * 1. Having contributor features be set and obtained through an api would be pretty cool,
	 * but as this would require a connection to a server, dataloaders are best for now.

	 *  2. Players could technically use this through the April Fools' Prank,
	 * but it would set all players, not specific ones.

	 * 3. If you are a frequent contributor (or have made a decently sized contribution),
	 * you are allowed to add your uuid to initContributorUuids(), alongside a perspective:contributors/<name>.json.
	 * Note; You are welcome to set your cape to developer_cape,
	 * However, if you don't want the default perspective cape,
	 * you will need to use a resource pack to change your data.
	 * If you don't set the shouldReplaceCape to true, your official cape will be rendered instead (if you have one set).
	 * You are also welcome to set shouldFlipUpsideDown.
	 * Please only use overlayTexture in resource packs, or ask before adding one into Perspective itself.
	**/
	private static final List<ContributorLockData> allowedUuids = new ArrayList<>();
	public static void init() {
		initAllowedUuids();
		PerspectiveEvents.ClientResourceReloaders.register(Identifiers.CONTRIBUTORS, new ContributorDataLoader());
	}
	private static void initAllowedUuids() {
		initDeveloperUuids();
		initContributorUuids();
		initAttributionUuids();
	}
	private static void initDeveloperUuids() {
		addAllowedUuid(Type.DEVELOPER, "772eb47b-a24e-4d43-a685-6ca9e9e132f7", "3445ebd7-25f8-41a6-8118-0d19d7f5559e"); // dannytaylor
	}
	private static void initContributorUuids() {
		addAllowedUuid(Type.CONTRIBUTOR, "9c3adf8d-a723-40c9-845b-c6e78c3ac460"); // Nettakrim
	}
	private static void initAttributionUuids() {
		addAllowedUuid(Type.ATTRIBUTION, "1f7a5ac1-664b-479d-9e2d-15ed891b080c"); // Phantazap: "Jester" Giant Textured Entity Texture.
	}
	private static void addAllowedUuid(Type type, String... uuids) {
		for (String uuid : uuids) {
			if (getUuid(uuid) == null) allowedUuids.add(new ContributorLockData(type, uuid));
		}
	}
	protected static ContributorLockData getUuid(String uuid) {
		for (ContributorLockData user : allowedUuids) {
			if (user.getUuids().contains(uuid)) return user;
		}
		return null;
	}
	public static ContributorData getContributorData(String uuid) {
		return getRawContributorData(uuid);
	}
	public static ContributorData getRawContributorData(String uuid) {
		return ContributorDataLoader.registry.get(uuid);
	}
	// In future, these could be used to limit functionality to specific types.
	public enum Type {
		DEVELOPER("developer"),
		CONTRIBUTOR("contributor"),
		ATTRIBUTION("attribution"),
		NONE("none");
		private final String name;
		Type(String name) {
			this.name = name;
		}
		public String getName() {
			return this.name;
		}
	}
	private static Couple<UUID, Boolean> clientContributor = updateClientContributor();
	public static boolean isClientContributor() {
		if (ClientData.minecraft.player != null) {
			if (clientContributor == null || !clientContributor.getFirst().equals(ClientData.minecraft.player.getGameProfile().getId())) updateClientContributor();
			return clientContributor.getSecond();
		}
		return false;
	}
	public static Couple<UUID, Boolean> updateClientContributor() {
		if (ClientData.minecraft.player != null) clientContributor = new Couple<>(ClientData.minecraft.player.getGameProfile().getId(), ContributorDataLoader.registry.containsKey(ClientData.minecraft.player.getGameProfile().getId().toString()));
		return null;
	}
}