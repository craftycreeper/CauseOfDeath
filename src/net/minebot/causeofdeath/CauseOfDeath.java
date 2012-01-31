package net.minebot.causeofdeath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

public class CauseOfDeath extends JavaPlugin {

	protected static File dataDir = new File("plugins/CauseOfDeath");
	
	protected static Random random = new Random();

	public void onEnable() {
		//If folder does not exist, create it
		if (!dataDir.isDirectory()) {
			dataDir.mkdir();
		}
		
		loadConfig();
		
		DeathMessages dm = new DeathMessages(this, dataDir + "/messages.yml");
		if (!dm.loadMessages()) {
			getLogger().warning("Could not load message file! Aborting enable.");
			return;
		}
		getLogger().info("Loaded " + dm.getMsgs() + " death messages in " + dm.getTypes() + " sections.");
		
		getServer().getPluginManager().registerEvents(new DeathListener(this, dm), this);
		
		getLogger().info("Enabled.");
	}
	
	public void onDisable() {
		getLogger().info("Disabled.");
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
		getConfig().addDefault("color.base", "7");
		getConfig().addDefault("color.name", "9");
		getConfig().addDefault("color.weapon", "9");
		getConfig().options().copyDefaults(true);
		try {
			getConfig().save(confFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
