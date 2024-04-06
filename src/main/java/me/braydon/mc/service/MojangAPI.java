package me.braydon.mc.service;

/**
 * Endpoints for the Mojang API.
 *
 * @author Braydon
 */
public class MojangAPI {
    private static final String SESSION_SERVER_ENDPOINT = "https://sessionserver.mojang.com";
    private static final String API_ENDPOINT = "https://api.mojang.com";
    protected static final String UUID_TO_PROFILE = SESSION_SERVER_ENDPOINT + "/session/minecraft/profile/%s";
    protected static final String USERNAME_TO_UUID = API_ENDPOINT + "/users/profiles/minecraft/%s";
}