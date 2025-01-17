/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.viewpoint.client.contributor;

import com.mclegoman.viewpoint.client.util.IdentifierHelper;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContributorData {
	private final List<String> ids;
	private final String uuid;
	private final String type;
	private final boolean shouldFlipUpsideDown;
	private final boolean shouldReplaceCape;
	private final Identifier capeTexture;
	private ContributorData(List<String> ids, String uuid, String type, boolean shouldFlipUpsideDown, boolean shouldReplaceCape, Identifier capeTexture) {
		this.ids = ids;
		this.uuid = uuid;
		this.type = type;
		this.shouldFlipUpsideDown = shouldFlipUpsideDown;
		this.shouldReplaceCape = shouldReplaceCape;
		this.capeTexture = capeTexture;
	}
	public static Builder builder(String uuid) {
		return new Builder(uuid);
	}
	public static class Builder {
		private final List<String> ids;
		private final String uuid;
		private String type;
		private boolean shouldFlipUpsideDown;
		private boolean shouldReplaceCape;
		private Identifier capeTexture;
		public Builder(String uuid) {
			this.ids = new ArrayList<>();
			this.uuid = uuid;
			this.type = Contributor.Type.CONTRIBUTOR.name();
			this.shouldFlipUpsideDown = false;
			this.shouldReplaceCape = false;
			this.capeTexture = IdentifierHelper.identifierFromString("none");
		}
		public Builder id(String... ids) {
			Collections.addAll(this.ids, ids);
			return this;
		}
		public Builder id(List<String> id) {
			this.ids.addAll(id);
			return this;
		}
		public Builder type(String type) {
			this.type = type;
			return this;
		}
		public Builder shouldFlipUpsideDown(boolean shouldFlipUpsideDown) {
			this.shouldFlipUpsideDown = shouldFlipUpsideDown;
			return this;
		}
		public Builder shouldReplaceCape(boolean shouldReplaceCape) {
			this.shouldReplaceCape = shouldReplaceCape;
			return this;
		}
		public Builder capeTexture(Identifier capeTexture) {
			this.capeTexture = capeTexture;
			return this;
		}
		public ContributorData build() {
			return new ContributorData(this.ids, this.uuid, this.type, this.shouldFlipUpsideDown, this.shouldReplaceCape, this.capeTexture);
		}
	}
	public List<String> getIds() {
		return this.ids;
	}
	public String getUuid() {
		return this.uuid;
	}
	public String getType() {
		return this.type;
	}
	public boolean getShouldFlipUpsideDown() {
		return this.shouldFlipUpsideDown;
	}
	public boolean getShouldReplaceCape() {
		return this.shouldReplaceCape;
	}
	public Identifier getCapeTexture() {
		return this.capeTexture;
	}
}
