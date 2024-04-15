import { ProfileAction } from "@/types/player/profile-action";
import { SkinModel } from "@/types/player/skin-model";
import { SkinPart } from "@/types/player/skin-part";

/**
 * A cacheable {@link Player}.
 */
export interface CachedPlayer extends Player {
	/**
	 * The unix timestamp of when this
	 * player was cached, -1 if not cached.
	 */
	cached: number;
}

/**
 * A Minecraft player.
 */
export type Player = {
	/**
	 * The unique id of this player.
	 */
	uniqueId: string;

	/**
	 * The username of this player.
	 */
	username: string;

	/**
	 * The skin of this player.
	 */
	skin: Skin;

	/**
	 * The cape of this player, undefined if none.
	 */
	cape?: Cape | undefined;

	/**
	 * The properties of the Mojang
	 * profile for this player.
	 */
	properties: ProfileProperty[];

	/**
	 * The profile actions this player has, undefined if none.
	 */
	profileActions?: ProfileAction[] | undefined;

	/**
	 * Is this player legacy?
	 * <p>
	 * A "Legacy" player is a player that
	 * has not yet migrated to a Mojang account.
	 * </p>
	 */
	legacy: boolean;
};

/**
 * A skin for a {@link Player}.
 */
type Skin = {
	/**
	 * The texture URL of this skin.
	 */
	url: string;

	/**
	 * The model of this skin.
	 */
	model: SkinModel;

	/**
	 * Is this skin legacy?
	 */
	legacy: boolean;

	/**
	 * URLs to the parts of this skin.
	 * <p>
	 * The key is the part, and the
	 * value is the URL to the part.
	 * </p>
	 */
	parts: {
		[part in SkinPart]: string;
	};
};

/**
 * A cape for a {@link Player}.
 */
type Cape = {
	/**
	 * The texture URL of this cape.
	 */
	url: string;
};

/**
 * A property of a Mojang profile.
 */
type ProfileProperty = {
	/**
	 * The name of this property.
	 */
	name: string;

	/**
	 * The Base64 value of this property.
	 */
	value: string;

	/**
	 * The Base64 signature of this
	 * property, undefined if not signed.
	 */
	signature?: string | undefined;
};
