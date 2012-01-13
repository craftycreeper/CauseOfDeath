package net.minebot.causeofdeath;

import java.util.Stack;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.PlayerDeathEvent;;

public class DeathListener extends EntityListener {

	private DeathMessages dm;
	
	public DeathListener(DeathMessages dm) {
		this.dm = dm;
	}
	
	public void onEntityDeath(EntityDeathEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		
		Player p = (Player)(event.getEntity());
		Coroner coroner = new Coroner(event);
		String message = dm.getDeathMessage(p, coroner);
		
		if (message != null) {
			((PlayerDeathEvent)event).setDeathMessage(message);
			CauseOfDeath.log.info("[CauseOfDeath] " + message);
		}
		else {
			StringBuilder sb = new StringBuilder();
			Stack<String> causes = coroner.getCauses();
			while (causes.size() > 0) {
				sb.append(causes.pop() + " ");
			}
			CauseOfDeath.log.warning("[CauseOfDeath] Failed to make a death message! Causes: (" + sb.toString() + ")");
			CauseOfDeath.log.warning("[CauseOfDeath] Default message: " + ((PlayerDeathEvent)event).getDeathMessage());
		}
		//otherwise leave it alone
		
	}
	
}
