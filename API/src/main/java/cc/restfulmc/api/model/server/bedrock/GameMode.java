package cc.restfulmc.api.model.server.bedrock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * The gamemode of a server.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public final class GameMode {
    /**
     * The name of this gamemode.
     */
    @NonNull private final String name;

    /**
     * The numeric of this gamemode, -1 if unknown.
     */
    private final int numericId;
}