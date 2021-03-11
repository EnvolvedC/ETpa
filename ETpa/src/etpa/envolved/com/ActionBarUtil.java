package etpa.envolved.com;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class ActionBarUtil {

	private static final Map<Player, BukkitTask> PENDING_MESSAGES = new HashMap<>();

	public static void sendActionBarMessage(@Nonnull Player bukkitPlayer, @Nonnull String message) {
		sendRawActionBarMessage(bukkitPlayer, "{\"text\": \"" + message + "\"}");
	}

	public static void sendRawActionBarMessage(@Nonnull Player bukkitPlayer, @Nonnull String rawMessage) {
		final CraftPlayer player = (CraftPlayer) bukkitPlayer;
		final IChatBaseComponent chatBaseComponent = ChatSerializer.a(rawMessage);
		final PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(chatBaseComponent, (byte) 2);
		player.getHandle().playerConnection.sendPacket(packetPlayOutChat);
	}

	public static void sendActionBarMessage(@Nonnull final Player bukkitPlayer, @Nonnull final String message,
			@Nonnull Plugin plugin) {
		cancelPendingMessages(bukkitPlayer);
		final BukkitTask messageTask = new BukkitRunnable() {
			private int count = 0;

			@Override
			public void run() {
				if (count >= 1) {
					cancel();
				}
				sendActionBarMessage(bukkitPlayer, message);
				count++;
			}
		}.runTaskTimerAsynchronously(plugin, 0L, 20L);
		PENDING_MESSAGES.put(bukkitPlayer, messageTask);
	}

	private static void cancelPendingMessages(@Nonnull Player bukkitPlayer) {
		if (PENDING_MESSAGES.containsKey(bukkitPlayer)) {
			PENDING_MESSAGES.get(bukkitPlayer).cancel();
		}
	}
}
