package me.braydon.demo;

import com.comphenix.protocol.ProtocolLibrary;
import me.braydon.demo.listener.ServerPingListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Braydon
 */
public final class DemoSpigotPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new ServerPingListener(this));
    }
}