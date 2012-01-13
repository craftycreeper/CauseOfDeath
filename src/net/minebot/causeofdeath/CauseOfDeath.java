package net.minebot.causeofdeath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.java.JavaPlugin;

public class CauseOfDeath extends JavaPlugin {

	protected static File dataDir = new File("plugins/CauseOfDeath");
	protected static Logger log = Logger.getLogger("Minecraft");
	
	protected static Random random = new Random();

	public void onEnable() {
		//If folder does not exist, create it
		if (!dataDir.isDirectory()) {
			dataDir.mkdir();
		}
		
		loadConfig();
		
		DeathMessages dm = new DeathMessages(this, dataDir + "/messages.yml");
		if (!dm.loadMessages()) {
			log.warning("[CauseOfDeath] Could not load message file! Aborting enable.");
			return;
		}
		log.info("[CauseOfDeath] Loaded " + dm.getMsgs() + " death messages in " + dm.getTypes() + " sections.");
		
		getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DEATH, new DeathListener(dm), Priority.Low, this);
		
		log.info("[CauseOfDeath] Version " + getDescription().getVersion() + " enabled.");
	}
	
	public void onDisable() {
		log.info("[CauseOfDeath] Version " + getDescription().getVersion() + " disabled.");
	}
	
	private void loadConfig() {
	//Load config, set defaults
		String confFile = dataDir + "/config.yml";
		try {
			getConfig().load(confFile);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} catch (InvalidConfigurationException e) {
		}
		getConfig().addDefault("colorBase", "LIGHT_PURPLE");
		getConfig().addDefault("colorName", "GOLD");
		getConfig().addDefault("colorWeapon", "GOLD");
		getConfig();
		getConfig().options().copyDefaults(true);
		try {
			getConfig().save(confFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
