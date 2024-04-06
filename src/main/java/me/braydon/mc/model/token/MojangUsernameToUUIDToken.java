package me.braydon.mc.model.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A token representing a Mojang username to UUID response.
 *
 * @author Braydon
 * @see <a href="https://wiki.vg/Mojang_API#Username_to_UUID">Mojang API</a>
 */
@NoArgsConstructor @Setter @Getter @ToString
public final class MojangUsernameToUUIDToken {
    /**
     * The id of the username.
     */
    private String id;
}