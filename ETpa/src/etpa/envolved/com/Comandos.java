package etpa.envolved.com;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import mkremins.fanciful.FancyMessage;

public class Comandos implements CommandExecutor, Listener {

	HashMap<String, String> tpa = new HashMap<>();

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("tpa")) {
			if (!(s instanceof Player)) {
				s.sendMessage("§cApenas jogadores tem acesso a este comando.");
				return false;
			}
			if (args.length == 0) {
				s.sendMessage(verifyString("Utilize"));
				return false;
			}
			if (args.length == 1) {
				Player p2 = (Player) s;
				Player p1 = Bukkit.getPlayerExact(args[0]);
				if (p1 == null) {
					s.sendMessage(verifyString("JogadorOffline"));
					return false;
				}
				if (p1 == p2) {
					s.sendMessage(verifyString("VoceMesmo"));
					return false;
				}
				if (tpa.containsKey(p2.getName())) {
					s.sendMessage(verifyString("VoceJaEnviouPedido"));
					return false;
				}
				tpa.put(p2.getName(), p1.getName());
				executarTimerVerify(p2);
				ActionBarUtil.sendActionBarMessage(p2, "EnviouPedido".replace("{player}", p1.getName()));
				p2.sendMessage("");
				p2.sendMessage(verifyString("EnviouPedido"));
				new FancyMessage("§eClique ").then("§c§lAQUI").tooltip("§cClique para cancelar o teleporte.")
						.command("/tpacancelar " + p2.getName()).then("§e para cancelar o pedido de teleporte.")
						.send(p2);
				p2.sendMessage("");
				p1.sendMessage("");
				p1.sendMessage("RecebeuPedido".replace("{player}", p2.getName()));
				new FancyMessage("§e Clique ").then("§a§lAQUI").tooltip("§aClique para aceitar.")
						.command("/tpaceitar " + p2.getName()).then("§e para aceitar ou ").then("§c§lAQUI")
						.tooltip("§cClique para negar.").command("/tpanegar " + p2.getName()).then(" §epara negar.")
						.send(p1);
				p1.sendMessage("");
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("tpanegar")) {
			if (!(s instanceof Player)) {
				s.sendMessage("§cApenas jogadores tem acesso a este comando.");
				return false;
			}
			Player p1 = (Player) s;
			if (args.length == 0) {
				s.sendMessage(verifyString("Utilize2"));
				return false;
			}
			if (args.length == 1) {
				Player p2 = Bukkit.getPlayerExact(args[0]);
				if (p2 == null) {
					s.sendMessage(verifyString("JogadorOffline"));
					return false;
				}
				if (p2 == p1) {
					s.sendMessage(verifyString("VoceMesmo2"));
					return false;
				}
				if (!tpa.containsKey(p2.getName())) {
					s.sendMessage(verifyString("NaoTemConvite").replace("{player}", p2.getName()));
					return false;
				}
				if (!(tpa.get(p2.getName()) == p1.getName())) {
					s.sendMessage(verifyString("NaoTemConvite").replace("{player}", p2.getName()));
					return false;
				}
				tpa.remove(p2.getName());
				ActionBarUtil.sendActionBarMessage(p2,
						verifyString("NegouSeuPedido").replace("{player}", p1.getName()));
				ActionBarUtil.sendActionBarMessage(p1, verifyString("NegouUmPedido").replace("{player}", p2.getName()));
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("tpacancelar")) {
			if (!(s instanceof Player)) {
				s.sendMessage("§cApenas jogadores tem acesso a este comando.");
				return false;
			}
			if (args.length == 0) {
				if (!tpa.containsKey(s.getName())) {
					s.sendMessage(verifyString("NadaParaCancelar"));
					return false;
				}
				String p1 = tpa.get(s.getName());
				s.sendMessage(verifyString("PedidoCancelado").replace("{player}", p1));
				tpa.remove(s.getName());
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("tpaceitar")) {
			if (!(s instanceof Player)) {
				s.sendMessage("§cApenas jogadores tem acesso a este comando.");
				return false;
			}
			Player p1 = (Player) s;
			if (args.length == 0) {
				s.sendMessage(verifyString("Utilize1"));
				return false;
			}
			if (args.length == 1) {
				Player p2 = Bukkit.getPlayerExact(args[0]);
				if (p2 == null) {
					s.sendMessage(verifyString("JogadorOffline"));
					return false;
				}
				if (p2 == p1) {
					s.sendMessage(verifyString("VoceMesmo1"));
					return false;
				}
				if (!tpa.containsKey(p2.getName())) {
					s.sendMessage(verifyString("NaoTemConvite").replace("{player}", p2.getName()));
					return false;
				}
				if (!(tpa.get(p2.getName()) == p1.getName())) {
					s.sendMessage(verifyString("NaoTemConvite").replace("{player}", p2.getName()));
					return false;
				}
				APITpa api = new APITpa(p1, p2);
				if (api.playerHasPermission()) {
					ActionBarUtil.sendActionBarMessage(p1, verifyString("TpaAceito").replace("{player}", p2.getName()));
					ActionBarUtil.sendActionBarMessage(p2,
							verifyString("TeleportandoComPerm").replace("{player}", p1.getName()));
					tpa.remove(p2.getName());
					p2.teleport(api.getLocation());
					return true;
				}
				ActionBarUtil.sendActionBarMessage(p1, verifyString("TpaAceito").replace("{player}", p2.getName()));
				new BukkitRunnable() {

					int count = 1;

					@Override
					public void run() {
						if (!p2.isOnline()) {
							tpa.remove(p2.getName());
							count = 0;
							cancel();
						}
						if (count == Main.getPlugin().getConfig().getInt("Tempo")) {
							ActionBarUtil.sendActionBarMessage(p2,
									"TeleportandoSemPerm".replace("{tempo}", String.valueOf(count)));
							tpa.remove(p2.getName());
							p2.teleport(api.getLocation());
							count = 0;
							cancel();
						}
						ActionBarUtil.sendActionBarMessage(p2,
								"TeleportandoSemPerm".replace("{tempo}", String.valueOf(count)));
						count++;
					}
				}.runTaskTimer(Main.getPlugin(), 0L, 20L);
			}
		}
		return false;

	}

	private String verifyString(String config) {
		return Main.getPlugin().getConfig().getString(config).replace("&", "§");
	}

	@EventHandler
	public void aoSairDoServidor(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (tpa.containsKey(p.getName())) {
			tpa.remove(p.getName());
		}
	}

	private void executarTimerVerify(Player p2) {
		new BukkitRunnable() {
			int tempo = 1;

			@Override
			public void run() {
				if (!tpa.containsKey(p2.getName())) {
					tempo = 0;
					cancel();
				}
				if (tempo == Main.getPlugin().getConfig().getInt("TempoExpirar")) {
					String p1 = tpa.get(p2.getName());
					p2.sendMessage(verifyString("PedidoExpirou").replace("{player}", p1));
					tpa.remove(p2.getName());
					tempo = 0;
					cancel();
				}
				tempo++;
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(), 0, 20L);
	}
}
