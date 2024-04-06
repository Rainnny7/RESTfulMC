package me.braydon.mc.model;

/**
 * Profile actions that can
 * be taken on a {@link Player}.
 *
 * @author Braydon
 */
public enum ProfileAction {
    /**
     * The player is required to change their
     * username before accessing Multiplayer.
     */
    FORCED_NAME_CHANGE,

    /**
     * The player is using a banned skin.
     */
    USING_BANNED_SKIN
}