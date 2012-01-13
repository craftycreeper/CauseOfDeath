package net.minebot.causeofdeath;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class DeathMessages {

	public static HashMap<String, List<String>> deathMessages;
	
	private String filename;
	private int types = 0;
	private int msgs = 0;
	
	private ChatColor colorBase;
	private ChatColor colorName;
	private ChatColor colorWeapon;

	private CauseOfDeath plugin;
	
	public DeathMessages(CauseOfDeath plugin, String filename) {
		this.plugin = plugin;
		this.filename = filename;
		deathMessages = new HashMap<String, List<String>>();
	}
	
	public boolean loadMessages() {
		File file = new File(filename);
		
		YamlConfiguration messageFile = new YamlConfiguration();
		try {
			messageFile.load(file);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} catch (InvalidConfigurationException e) {
			return false;
		}
		
		for(String key : messageFile.getKeys(false)) {
			types++;
			CauseOfDeath.log.info("Death cause: " + key);
			List<String> messages = messageFile.getStringList(key);
			msgs += messages.size();
			deathMessages.put(key, messages);
			for (String msg : messages) {
				CauseOfDeath.log.info("   Message: " + msg);
			}
		}
		
		//Colors
		try {
			colorBase = ChatColor.valueOf(plugin.getConfig().getString("colorBase"));
		} catch (Exception e) {
			CauseOfDeath.log.warning("[CauseOfDeath] Invalid colorBase value.");
			colorBase = ChatColor.WHITE;
		}
		try {
			colorName = ChatColor.valueOf(plugin.getConfig().getString("colorName"));
		} catch (Exception e) {
			CauseOfDeath.log.warning("[CauseOfDeath] Invalid colorName value.");
			colorName = ChatColor.WHITE;
		}
		try {
			colorWeapon = ChatColor.valueOf(plugin.getConfig().getString("colorWeapon"));
		} catch (Exception e) {
			CauseOfDeath.log.warning("[CauseOfDeath] Invalid colorWeapon value.");
			colorWeapon = ChatColor.WHITE;
		}
		
		return true;
	}
	
	public String getDeathMessage(Player player, Coroner coroner) {
		Stack<String> causes = coroner.getCauses();
		
		//Locate the topmost death cause we have messages for.
		String foundCause = null;
		while (foundCause == null && causes.size() > 0) {
			String cause = causes.pop();
			if (deathMessages.containsKey(cause) && deathMessages.get(cause).size() > 0)
				foundCause = cause; 
		}
		if (foundCause == null)
			return null; //nothing we can do
		
		//Pick a random message.
		List<String> messages = deathMessages.get(foundCause);
		String message = messages.get(CauseOfDeath.random.nextInt(messages.size()));
		
		message = colorBase + message;
		
		//Substitute variables
		if (player != null) {
			message = message.replace("%p", colorName +
				player.getDisplayName() + colorBase);
		}
		
		Entity killer = coroner.getKiller();
		if (killer != null) {
			if (killer instanceof Player)
				message = message.replace("%k", colorName +
					((Player)killer).getDisplayName() + colorBase);
			else {
				String creatureType = coroner.getCreatureType(killer);
				if (creatureType != null)
					message = message.replace("%k", colorName +
						creatureType + colorBase);
			}
		}
		
		String weapon = coroner.getWeapon();
		if (weapon != null) {
			message = message.replace("%w", colorWeapon + weapon + colorBase);
		}
		
		
		return message;
	}
	
	public int getTypes() {
		return types;
	}

	public int getMsgs() {
		return msgs;
	}
	
}
