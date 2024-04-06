package me.braydon.mc.exception.impl;

import me.braydon.mc.model.MinecraftServer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is raised when a {@link MinecraftServer}
 * lookup is made for an invalid {@link MinecraftServer.Platform}.
 *
 * @author Braydon
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public final class InvalidMinecraftServerPlatform extends RuntimeException {
    public InvalidMinecraftServerPlatform() {
        super("Invalid Minecraft server platform");
    }
}