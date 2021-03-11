package etpa.envolved.com;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public static Main getPlugin() {
		return JavaPlugin.getPlugin(Main.class);
	}

	@Override
	public void onEnable() {
		registrarComandos();
		new File(getDataFolder(), "config.yml");
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		getServer().getConsoleSender().sendMessage("");
		getServer().getConsoleSender().sendMessage(" §d[ETPA] Habilitado com sucesso.");
		getServer().getConsoleSender().sendMessage("");
	}

	@Override
	public void onDisable() {
		getServer().getConsoleSender().sendMessage("");
		getServer().getConsoleSender().sendMessage(" §c[ETPA] Desabilitado com sucesso.");
		getServer().getConsoleSender().sendMessage("");
	}

	public void registrarComandos() {
		getCommand("tpa").setExecutor(new Comandos());
		getCommand("tpaceitar").setExecutor(new Comandos());
		getCommand("tpanegar").setExecutor(new Comandos());
		getCommand("tpacancelar").setExecutor(new Comandos());
		getServer().getPluginManager().registerEvents(new Comandos(), this);
	}
}
