package net.climaxmc;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import net.climaxmc.Administration.Administration;
import net.climaxmc.Administration.Runnables.UpdateRunnable;
import net.climaxmc.Donations.Donations;
import net.climaxmc.KitPvp.KitManager;
import net.climaxmc.KitPvp.KitPvp;
import net.climaxmc.KitPvp.Kits.PvpKit;
import net.climaxmc.KitPvp.Utils.Challenges.ChallengesFiles;
import net.climaxmc.KitPvp.Utils.ChatColor.DChatColor;
import net.climaxmc.KitPvp.Utils.ChatUtils;
import net.climaxmc.KitPvp.Utils.DeathEffects.DeathEffect;
import net.climaxmc.KitPvp.Utils.EntityHider;
import net.climaxmc.KitPvp.Utils.I;
import net.climaxmc.KitPvp.Utils.ServerScoreboard;
import net.climaxmc.KitPvp.events.EventManager;
import net.climaxmc.KitPvp.events.tournament.TPManager;
import net.climaxmc.KitPvp.events.tournament.TournamentManager;
import net.climaxmc.KitPvp.packets.PacketCore;
import net.climaxmc.common.database.MySQL;
import net.climaxmc.common.database.PlayerData;
import net.gpedro.integrations.slack.SlackApi;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.*;

public class ClimaxPvp extends JavaPlugin {
    @Getter
    private static ClimaxPvp instance = null;
    @Getter
    public HashMap<UUID, Location> currentWarps = new HashMap<>();
    @Getter
    public HashMap<UUID, String> playersInWarp = new HashMap<>();
    @Getter
    private MySQL mySQL = null;
    @Getter
    private String prefix = ChatColor.BLACK + "" + ChatColor.BOLD + "[" + ChatColor.RED + "Climax" + ChatColor.BLACK + "" + ChatColor.BOLD + "] " + ChatColor.RESET;
    @Getter
    private List<String> rules = new ArrayList<>();
    @Getter
    private List<String> help = new ArrayList<>();
    @Getter
    private List<String> filteredWords = new ArrayList<>();
    @Getter
    private FileConfiguration warpsConfig = null;
    private File warpsConfigFile = null;
    @Getter
    private ChallengesFiles challengesFiles = null;
    @Getter
    private HashMap<UUID, PlayerData> playerDataList = new HashMap<>();
    @Getter
    private HashMap<UUID, UUID> messagers = new HashMap<>();
    private Donations donations = null;
    @Getter
    private SlackApi slackReports = null;
    @Getter
    private SlackApi slackBans = null;
    @Getter
    private SlackApi slackStaffHelp = null;
    @Getter
    private SlackApi slackDonations = null;
    @Getter
    private List<String> info = new ArrayList<>();

    public static ArrayList<UUID> isSpectating = new ArrayList<>();

    public static ArrayList<Player> isVanished = new ArrayList<>();

    //public static ArrayList<Integer> activeDuelArenas = new ArrayList<>();
    public static ArrayList<Player> inDuel = new ArrayList<>();
    public static ArrayList<Player> duelSpectators = new ArrayList<>();
    public static HashMap<Player, Player> isDueling = new HashMap<>();
    public static HashMap<Player, Player> isDuelingReverse = new HashMap<>();
    public static HashMap<Player, String> duelsKit = new HashMap<>();
    public static HashMap<Player, Player> initialRequest = new HashMap<>();
    public static HashMap<Player, Player> duelRequestReverse = new HashMap<>();
    public static HashMap<Player, Player> hasRequest = new HashMap<>();
    public static HashMap<Player, Integer> currentPlayerArena = new HashMap<>();
    public static ArrayList<Integer> currentArenas = new ArrayList<>();

    /*public static ArrayList<Player> inTourney = new ArrayList<>();
    public static ArrayList<Player> inTourneyLobby = new ArrayList<>();
    public static ArrayList<Player> tourneySpectators = new ArrayList<>();
    public static ArrayList<Player> tourneyWinners = new ArrayList<>();
    public static HashMap<Integer, Player> playerPoint = new HashMap<>();
    public static boolean isTourneyRunning = false;
    public static boolean isTourneyHosted = false;
    public static int tourneyPrize;*/

    public static ArrayList<Player> inTag = new ArrayList<>();
    public static ArrayList<Player> inTagLobby = new ArrayList<>();
    public static ArrayList<Player> tagSpectators = new ArrayList<>();
    public static ArrayList<Player> tagWinners = new ArrayList<>();
    public static ArrayList<Integer> tagDeathsSorted = new ArrayList<>();
    public static HashMap<Player, Integer> tagPlayerDeaths = new HashMap<>();
    public static boolean isTagRunning = false;
    public static boolean isTagHosted = false;
    public static int tagPrize;
    public static Player isIt;

    public HashMap<Player, String> lastHitType = new HashMap<>();

    public HashMap<Player, Integer> itemClickDelay = new HashMap<>();

    public static HashMap<Player, String> inTitle = new HashMap<>();

    public static HashMap<UUID, DChatColor> currentChatColor = new HashMap<>();
    public static boolean chatColorItalics = false;

    public static HashMap<Player, String> inEffect = new HashMap<>();

    public static Map<UUID, DeathEffect> playerDeathEffect = new HashMap<>();

    public static ArrayList<Player> deadPeoples = new ArrayList<>();

    public static ArrayList<Player> inFighterKit = new ArrayList<>();

    private EntityHider entityHider;

    public HashMap<Player, Location> freezeLocation = new HashMap<>();
    public Map<Player, Location> lastValidLocation = new HashMap<>();

    public Map<Player, Boolean> spawnSoupTrue = new HashMap<>();
    public Map<Player, Boolean> respawnValue = new HashMap<>();

    public Map<UUID, Integer> currentKS = new HashMap<>();

    public EventManager eventManager;
    public TournamentManager tournamentManager;
    public TPManager tpManager;

    @Override
    public void onEnable() {
        // Initialize Instance
        instance = this;

        // Save Default Configuration
        saveDefaultConfig();

        // Save Default Warps Storage File
        saveDefaultWarpsConfig();

        // Save Help File
        File helpFile = new File(getDataFolder(), "help.txt");
        if (!helpFile.exists()) {
            saveResource("help.txt", false);
        }
        try {
            Files.lines(FileSystems.getDefault().getPath(helpFile.getPath())).forEach(helpLine -> help.add(ChatColor.translateAlternateColorCodes('&', helpLine)));
        } catch (IOException e) {
            getLogger().severe("Could not get help!");
        }

        // Save Rules File
        File rulesFile = new File(getDataFolder(), "rules.txt");
        if (!rulesFile.exists()) {
            saveResource("rules.txt", false);
        }
        try {
            Files.lines(FileSystems.getDefault().getPath(rulesFile.getPath())).forEach(ruleLine -> rules.add(ChatColor.translateAlternateColorCodes('&', ruleLine)));
        } catch (IOException e) {
            getLogger().severe("Could not get rules!");
        }

        // Save Filter File
        File filterFile = new File(getDataFolder(), "filter.txt");
        if (!filterFile.exists()) {
            saveResource("filter.txt", false);
        }
        try {
            Files.lines(FileSystems.getDefault().getPath(filterFile.getPath())).forEach(filterLine -> filteredWords.add(ChatColor.translateAlternateColorCodes('&', filterLine)));
        } catch (IOException e) {
            getLogger().severe("Could not get filtered words!");
        }

        // Connect to MySQL
        mySQL = new MySQL(
                this,
                getConfig().getString("Database.Host"),
                getConfig().getInt("Database.Port"),
                getConfig().getString("Database.Database"),
                getConfig().getString("Database.Username"),
                getConfig().getString("Database.Password")
        );

        // Save Info File
        File infoFile = new File(getDataFolder(), "info.txt");
        if (!infoFile.exists()) {
            saveResource("info.txt", false);
        }
        try {
            Files.lines(FileSystems.getDefault().getPath(infoFile.getPath())).forEach(infoLine -> info.add(ChatColor.translateAlternateColorCodes('&', infoLine)));
        } catch (IOException e) {
            getLogger().severe("Could not get info!");
        }

        // Create temporary player data
        getServer().getOnlinePlayers().forEach(player -> mySQL.getTemporaryPlayerData().put(player.getUniqueId(), new HashMap<>()));
        //getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        // Load Modules
        new KitPvp(this);
        donations = new Donations(this);
        new Administration(this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new UpdateRunnable(this), 120, 120);
        challengesFiles = new ChallengesFiles();
        KitManager.setAllKitsEnabled(getConfig().getBoolean("AllKitsEnabled"));

        // Initialize SlackApi
        slackReports = new SlackApi("https://hooks.slack.com/services/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");
        slackBans = new SlackApi("https://hooks.slack.com/services/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");
        slackStaffHelp = new SlackApi("https://hooks.slack.com/services/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");
        slackDonations = new SlackApi("https://hooks.slack.com/services/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");

        entityHider = new EntityHider(this, EntityHider.Policy.BLACKLIST);

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        /*getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                rotateMaps();
            }
        }, 0L, ((20L * 60) * 60) * 1);*/

        Bukkit.getServer().getWorld("KitPvp3.0").setSpawnLocation(-41, 58, 672);
        currentMap = 0;

        for (Player players : Bukkit.getOnlinePlayers()) {
            ServerScoreboard serverScoreboard = new ServerScoreboard(players);
            serverScoreboard.updateScoreboard();
            scoreboards.put(players.getUniqueId(), serverScoreboard);
        }

        new PacketCore(this);

        eventManager = new EventManager(this);
        tournamentManager = new TournamentManager(this);
        tpManager = new TPManager(this);
    }

    private int currentMap = 0;

    public void hideEntity(Player observer, Entity entity) {
        entityHider.hideEntity(observer, entity);
    }

    public void showEntity(Player observer, Entity entity) {
        entityHider.showEntity(observer, entity);
    }

    @Override
    public void onDisable() {
        getServer().getOnlinePlayers().forEach(player -> savePlayerData(getPlayerData(player)));

        if (donations.getVoteReceiver() != null) {
            donations.getVoteReceiver().shutdown();
        }

        //trailsRunnable.stopTrails();

        // Close MySQL Connection
        if (mySQL.getConnection() != null) {
            try {
                mySQL.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<UUID, ServerScoreboard> scoreboards = new HashMap<>();

    public ServerScoreboard getScoreboard(Player player) {
        return scoreboards.get(player.getUniqueId());
    }

    public void rotateMaps() {

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f\u00BB &bMap rotation commencing, do /spawn!"));
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.playSound(players.getLocation(), Sound.LEVEL_UP, 1F, 0.7F);
        }

        if (currentMap == 0) {
            Bukkit.getServer().getWorld("KitPvp3.0").setSpawnLocation(-53, 63, -907);
            currentMap = 1;
        /*} else if (currentMap == 1) {
            Bukkit.getServer().getWorld("KitPvp3.0").setSpawnLocation(4, 10, 8);
            currentMap = 2;*/
        } else if (currentMap == 1) {
            Bukkit.getServer().getWorld("KitPvp3.0").setSpawnLocation(-41, 58, 672);
            currentMap = 0;
        }
    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }

    public boolean isWithinProtectedRegion(Location location) {
        ApplicableRegionSet set = getWorldGuard().getRegionManager(location.getWorld()).getApplicableRegions(location.getBlock().getLocation());
        for (ProtectedRegion region : set.getRegions()) {
            if (region.getFlags().containsKey(DefaultFlag.INVINCIBILITY)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get player data from MySQL
     *
     * @param player Player to get data of
     * @return Data of player
     */
    public PlayerData getPlayerData(OfflinePlayer player) {
        if (!playerDataList.containsKey(player.getUniqueId())) {
            playerDataList.put(player.getUniqueId(), mySQL.getPlayerData(player.getUniqueId()));
        }
        return playerDataList.get(player.getUniqueId());
    }

    /**
     * Saves player data
     *
     * @param playerData Player data to save
     */
    public void savePlayerData(PlayerData playerData) {
        if (playerDataList.containsKey(playerData.getUuid())) {
            mySQL.savePlayerData(playerData);
        }
    }

    /**
     * Respawns a player at a location
     *
     * @param player Player to respawn
     * @param location Location for player to respawn at
     */
    public void respawn(Player player, Location location) {
        player.spigot().respawn();
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            if (ClimaxPvp.getInstance().getCurrentWarps().containsKey(player.getUniqueId())) {
                player.teleport(ClimaxPvp.getInstance().getCurrentWarps().get(player.getUniqueId()));
            } else {
                player.teleport(location);
            }
        } else {
            player.teleport(location);
        }
        getServer().getPluginManager().callEvent(new PlayerRespawnEvent(player, location, false));
    }

    /**
     * Respawns a player at the world spawn
     *
     * @param player Player to respawn
     */
    public void respawn(Player player) {
        respawn(player, player.getWorld().getSpawnLocation());
    }

    /**
     * Saves the default currentWarps configuration file
     */
    private void saveDefaultWarpsConfig() {
        if (warpsConfigFile == null) {
            warpsConfigFile = new File(getDataFolder(), "warps.yml");
        }

        if (!warpsConfigFile.exists()) {
            saveResource("warps.yml", false);
        }

        warpsConfig = YamlConfiguration.loadConfiguration(warpsConfigFile);
    }

    /**
     * Saves the currentWarps configuration file
     */
    public void saveWarpsConfig() {
        try {
            warpsConfig.save(warpsConfigFile);
        } catch (IOException e) {
            getLogger().severe("Could not save warps configuration!");
        }
    }

    /**
     * Gets the location of a warp
     *
     * @param warp Name of warp to get location of
     * @return Location of warp
     */
    public Location getWarpLocation(String warp) {
        ConfigurationSection noSoupSection;

        try {
            noSoupSection = warpsConfig.getConfigurationSection(warpsConfig.getKeys(false).stream().filter(key -> key.equalsIgnoreCase(warp)).findFirst().get());
        } catch (NoSuchElementException ignored) {
            return null;
        }

        return new Location(
                getServer().getWorld(noSoupSection.getString("World")),
                noSoupSection.getDouble("X"),
                noSoupSection.getDouble("Y"),
                noSoupSection.getDouble("Z"),
                (float) noSoupSection.getDouble("Yaw"),
                (float) noSoupSection.getDouble("Pitch")
        );
    }

    /**
     * Warps a player to specified warp
     *
     * @param player Player to warp
     */
    public void warp(String warp, Player player) {
        Location location = getWarpLocation(warp);

        if (location == null) {
            player.sendMessage(ChatColor.RED + "That warp does not exist!");
            return;
        }

        respawn(player, location);
        currentWarps.put(player.getUniqueId(), getWarpLocation(warp));
        playersInWarp.put(player.getUniqueId(), warp);

        if (warp.equalsIgnoreCase("Fair")) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.getInventory().setItem(0, new I(Material.DIAMOND_SWORD).name(ChatUtils.color("&bPunch to duel")));
        }
        if (warp.equalsIgnoreCase("Duel")) {
            player.getInventory().clear();
            player.getInventory().addItem(new I(Material.DIAMOND_AXE).name(ChatColor.WHITE + "Duel Axe " + ChatColor.AQUA + "(Punch a player!)"));
        }
    }
}

//name: ${project.artifactId}
//main: ${project.groupId}.${project.artifactId}
//        version: ${project.version}