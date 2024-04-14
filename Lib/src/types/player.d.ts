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
}

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
	model: Model;

	/**
	 * Is this skin legacy?
	 */
	legacy: boolean;
}

/**
 * Possible models for a skin.
 */
type Model = "default" | "slim";
