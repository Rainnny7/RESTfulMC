package cc.restfulmc.demo.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.ModInfo;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Braydon
 */
public final class ServerPingListener {
    private static final String[] MESSAGES = new String[] {
            "wow omg so cool!",
            "Hello World!",
            "Rainnny was here",
            "Star on GitHub!",
            "restfulmc.cc",
            "discord.restfulmc.cc"
    };
    private static final String[] PLAYERS = new String[] {
            "Rainnny", "Notch", "jeb_", "hypixel", "Dinnerbone", "C418", "g", "hey"
    };

    @Subscribe
    public void onProxyPing(ProxyPingEvent event) {
        ServerPing ping = event.getPing(); // Get the ping response
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // Update the version
        ServerPing.Version version = new ServerPing.Version(ping.getVersion().getProtocol(), "RESTfulMC Demo");

        // Update the player count
        List<ServerPing.SamplePlayer> playerSamples = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            playerSamples.add(new ServerPing.SamplePlayer(PLAYERS[random.nextInt(PLAYERS.length)], UUID.randomUUID()));
        }
        ServerPing.Players players = new ServerPing.Players(random.nextInt(300, 25000), 30000, playerSamples);

        TextComponent motd = Component.text(String.join("\n",
                "§f                 §2§lRESTfulMC §7Demo Server",
                "§7                       " + MESSAGES[random.nextInt(MESSAGES.length)]
        ));

        // Update the mod info
        ModInfo modInfo = new ModInfo(ModInfo.DEFAULT.getType(), Arrays.asList(
                new ModInfo.Mod("bob", "1.0"),
                new ModInfo.Mod("ross", "1.0")
        ));

        // Set the ping response
        event.setPing(new ServerPing(version, players, motd, ping.getFavicon().orElse(null), modInfo));
    }
}