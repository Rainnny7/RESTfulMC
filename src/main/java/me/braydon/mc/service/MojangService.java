package me.braydon.mc.service;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import me.braydon.mc.common.UUIDUtils;
import me.braydon.mc.common.web.JsonWebException;
import me.braydon.mc.common.web.JsonWebRequest;
import me.braydon.mc.exception.impl.BadRequestException;
import me.braydon.mc.exception.impl.ResourceNotFoundException;
import me.braydon.mc.model.Player;
import me.braydon.mc.model.token.MojangProfileToken;
import me.braydon.mc.model.token.MojangUsernameToUUIDToken;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * A service for interacting with the Mojang API.
 *
 * @author Braydon
 */
@Service
@Log4j2(topic = "Mojang Service")
public final class MojangService {
    private static final String SESSION_SERVER_ENDPOINT = "https://sessionserver.mojang.com";
    private static final String API_ENDPOINT = "https://api.mojang.com";
    private static final String UUID_TO_PROFILE = SESSION_SERVER_ENDPOINT + "/session/minecraft/profile/%s";
    private static final String USERNAME_TO_UUID = API_ENDPOINT + "/users/profiles/minecraft/%s";

    /**
     * Get a player by their username or UUID.
     *
     * @param query the query to search for the player by
     * @return the player
     * @throws BadRequestException if the UUID is malformed
     * @throws ResourceNotFoundException if the player is not found
     */
    @NonNull
    public Player getPlayer(@NonNull String query) throws BadRequestException, ResourceNotFoundException {
        log.info("Requesting player with query: {}", query);

        UUID uuid; // The player UUID to lookup
        boolean fullLength = query.length() == 36; // Was a UUID provided?
        if (query.length() == 32 || fullLength) { // Parse the query as a UUID
            try {
                uuid = fullLength ? UUID.fromString(query) : UUIDUtils.addDashes(query);
                log.info("Parsed {}UUID: {} -> {}", fullLength ? "" : "trimmed ", query, uuid);
            } catch (IllegalArgumentException ex) {
                throw new BadRequestException("Malformed UUID provided: %s".formatted(query));
            }
        } else { // The query is a username, request from Mojang
            uuid = usernameToUUID(query);
            log.info("Found UUID for username {}: {}", query, uuid);
        }

        // Send a request to Mojang requesting
        // the player profile by their UUID
        try {
            log.info("Retrieving player profile for UUID: {}", uuid);
            MojangProfileToken token = JsonWebRequest.makeRequest(
                    UUID_TO_PROFILE.formatted(uuid), HttpMethod.GET
            ).execute(MojangProfileToken.class);

            // Return our player model representing the requested player
            return new Player(uuid, token.getName(), token.getProfileActions());
        } catch (JsonWebException ex) {
            // No profile found, return null
            if (ex.getStatusCode() == 400) {
                throw new ResourceNotFoundException("Player not found with query: %s".formatted(query));
            }
            throw ex;
        }
    }

    /**
     * Get the UUID of a player by their username.
     *
     * @param username the player's username
     * @return the player's UUID
     * @throws ResourceNotFoundException if the player isn't found
     */
    @NonNull
    private UUID usernameToUUID(@NonNull String username) throws ResourceNotFoundException {
        // Make a request to Mojang requesting the UUID
        try {
            MojangUsernameToUUIDToken token = JsonWebRequest.makeRequest(
                    USERNAME_TO_UUID.formatted(username), HttpMethod.GET
            ).execute(MojangUsernameToUUIDToken.class);
            return UUIDUtils.addDashes(token.getId()); // Return the UUID with dashes
        } catch (JsonWebException ex) {
            if (ex.getStatusCode() == 404) {
                throw new ResourceNotFoundException("Player not found with username: %s".formatted(username));
            }
            throw ex;
        }
    }
}