package cc.restfulmc.demo;

import cc.restfulmc.demo.listener.ServerPingListener;
import com.google.inject.Inject;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;

/**
 * @author Braydon
 */
@Plugin(id = "demoplugin", name = "DemoPlugin", version = "1.0.0")
public final class DemoPlugin {
    private final ProxyServer server;

    @Inject
    public DemoPlugin(ProxyServer server) {
        this.server = server;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new ServerPingListener());
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onLogin(PreLoginEvent event) {
        event.setResult(PreLoginEvent.PreLoginComponentResult.denied(Component.text("§cYou can't join a demo server :(")));
    }
}