package cc.restfulmc.api.model.token.mojang;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * A token representing a Mojang username to UUID response.
 *
 * @author Braydon
 * @see <a href="https://wiki.vg/Mojang_API#Username_to_UUID">Mojang API</a>
 */
@AllArgsConstructor @Getter @ToString
public final class MojangUsernameToUUIDToken {
    /**
     * The id of the username.
     */
    @NonNull private final String id;
}