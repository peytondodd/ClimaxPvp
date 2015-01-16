package net.climaxmc.KitPvp;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import net.climaxmc.ClimaxPvp;
import net.climaxmc.KitPvp.Kits.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

public class KitManager {
	public static ArrayList<Kit> kits = new ArrayList<Kit>();
	
	public KitManager(ClimaxPvp plugin) {
		// Default Kits
		kits.add(new PvpKit());
		kits.add(new HeavyKit());
		kits.add(new ArcherKit());
		kits.add(new IronGolemKit());
		kits.add(new FishermanKit());
		kits.add(new NinjaKit());
		// Amateur Kits
		kits.add(new SoldierKit());
		kits.add(new EndermanKit());
		// Experienced Kits
		
		// Advanced Kits
		
		// Veteran Kits
		
		
		for (Kit kit : kits) {
			plugin.getServer().getPluginManager().registerEvents(kit, plugin);
			getCommandMap().register(plugin.getConfig().getName(), getCommand(kit.getName().replaceAll("\\s+", ""), plugin));
			plugin.getCommand(kit.getName().replaceAll("\\s+", "")).setExecutor(kit);
		}
	}

	private PluginCommand getCommand(String name, Plugin plugin) {
		PluginCommand command = null;

		try {
			Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			c.setAccessible(true);

			command = c.newInstance(name, plugin);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return command;
	}

	private CommandMap getCommandMap() {
		CommandMap commandMap = null;

		try {
			if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
				Field f = SimplePluginManager.class.getDeclaredField("commandMap");
				f.setAccessible(true);

				commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return commandMap;
	}
}