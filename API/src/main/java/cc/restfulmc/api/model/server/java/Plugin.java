package cc.restfulmc.api.model.server.java;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * A plugin for a server.
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @ToString
public final class Plugin {
    /**
     * The name of this plugin.
     */
    @NonNull private final String name;

    /**
     * The version of this plugin.
     */
    @NonNull private final String version;
}