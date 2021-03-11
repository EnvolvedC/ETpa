package etpa.envolved.com;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class APITpa {

	private Player p1;
	private Player p2;

	public APITpa(Player p1, Player p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public Player getPlayer() {
		return p2;
	}

	public Player getTarget() {
		return p1;
	}

	public Location getLocation() {
		Location loc = new Location(p1.getWorld(), p1.getLocation().getX(), p1.getLocation().getY(),
				p1.getLocation().getZ(), p1.getLocation().getYaw(), p1.getLocation().getPitch());
		return loc;
	}

	public boolean playerHasPermission() {
		if (p2.hasPermission("etpa.delay")) {
			return true;
		}
		return false;
	}

}
