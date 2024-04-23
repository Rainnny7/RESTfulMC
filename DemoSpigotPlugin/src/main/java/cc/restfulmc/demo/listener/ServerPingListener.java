package cc.restfulmc.demo.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import cc.restfulmc.demo.DemoSpigotPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Braydon
 */
public final class ServerPingListener extends PacketAdapter {
    private static final String[] MESSAGES = new String[] {
            "wow omg so cool!",
            "Hello World!",
            "Rainnny was here",
            "Star on GitHub!",
            "restfulmc.cc"
    };
    private static final String[] PLAYERS = new String[] {
            "Rainnny", "Notch", "jeb_", "hypixel", "Dinnerbone", "C418", "g", "hey"
    };

    public ServerPingListener(DemoSpigotPlugin plugin) {
        super(plugin, PacketType.Status.Server.SERVER_INFO);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        WrappedServerPing ping = event.getPacket().getServerPings().read(0);
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // Update the MOTD
        ping.setMotD(String.join("\n",
                "§f                 §2§lRESTfulMC §7Demo Server",
                "§7                       " + MESSAGES[random.nextInt(MESSAGES.length)]
        ));

        // Update the player count
        ping.setPlayersOnline(random.nextInt(300, 25000));
        ping.setPlayersMaximum(30000);

        List<WrappedGameProfile> playerSamples = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            playerSamples.add(new WrappedGameProfile(UUID.randomUUID(), PLAYERS[random.nextInt(PLAYERS.length)]));
        }
        ping.setPlayers(playerSamples);

        ping.setVersionName("RESTfulMC Demo");
    }
}