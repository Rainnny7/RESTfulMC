package cc.restfulmc.demo;

import cc.restfulmc.demo.listener.ServerPingListener;
import com.comphenix.protocol.ProtocolLibrary;
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