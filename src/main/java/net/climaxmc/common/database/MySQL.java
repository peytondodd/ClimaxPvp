package net.climaxmc.common.database;

import lombok.Getter;
import net.climaxmc.Administration.Punishments.Punishment;
import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * MySQL Tools
 *
 * @author computerwizjared
 */
public class MySQL {

    // PLAYERDATA ---------------------------------------------------------------------
    public static final String GET_PLAYERDATA_FROM_UUID = "SELECT * FROM `climax_playerdata` WHERE `uuid` = ?;";
    public static final String GET_PLAYERDATA_FROM_IP = "SELECT * FROM `climax_playerdata` WHERE `ip` = ?;";
    public static final String CREATE_PLAYERDATA_TABLE = "CREATE TABLE IF NOT EXISTS `climax_playerdata` (`uuid` VARCHAR(36) NOT NULL PRIMARY KEY, " +
            "`ip` VARCHAR(15) DEFAULT '' NOT NULL, `rank` VARCHAR(20) DEFAULT 'DEFAULT' NOT NULL, `balance` INT DEFAULT 0 NOT NULL, " +
            "`kills` INT DEFAULT 0 NOT NULL, `deaths` INT DEFAULT 0 NOT NULL, " /*`achievements` "VARCHAR(5000) DEFAULT NULL,*/ + "`nickname` VARCHAR(32) DEFAULT NULL, " +
            "`kdr` INT DEFAULT 0 NOT NULL, `topks` INT DEFAULT 0 NOT NULL);";
    public static final String CREATE_PLAYERDATA = "INSERT IGNORE INTO `climax_playerdata` (`uuid`, `ip`, `rank`, `balance`, `kills`, " +
            "`deaths`, " /*`achievements`,*/ + "`nickname`, `kdr`, `topks`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    public static final String UPDATE_PLAYERDATA = "UPDATE `climax_playerdata` SET `ip` = ?, `rank` = ?, `balance` = ?, `kills` = ?, " +
            "`deaths` = ?, " /*`achievements` = ?,*/ + "`nickname` = ?, `kdr` = ?, `topks` = ? WHERE `uuid` = ?;";

    // PUNISHMENTS ---------------------------------------------------------------------
    public static final String CREATE_PUNISHMENTS_TABLE = "CREATE TABLE IF NOT EXISTS `climax_punishments` (`uuid` VARCHAR(36) NOT NULL," +
            " `type` VARCHAR(32) NOT NULL, `time` BIGINT(20) NOT NULL, `expiration` BIGINT(20) NOT NULL, `punisheruuid` VARCHAR(26) NOT NULL, `reason` TEXT NOT NULL);";
    public static final String CREATE_PUNISHMENT = "INSERT IGNORE INTO `climax_punishments` (`uuid`, `type`, `time`, `expiration`, `punisheruuid`, `reason`)" +
            " VALUES (?, ?, ?, ?, ?, ?);";
    public static final String GET_PUNISHMENTS_FROM_UUID = "SELECT * FROM `climax_punishments` WHERE `uuid` = ?;";
    public static final String GET_PUNISHMENTS_FROM_IP = "SELECT * FROM `climax_punishments` WHERE `ip` = ?;";
    public static final String UPDATE_PUNISHMENT_TIME = "UPDATE `climax_punishments` SET `expiration` = ? WHERE `uuid` = ? AND `type` = ? AND `time` = ?;";

    /*
    // SETTINGS ------------------------------------------------------------------------
    public static final String GET_SETTINGS = "SELECT * FROM `climax_settings` WHERE `uuid` = ?;";
    public static final String CREATE_SETTINGS_TABLE = "CREATE TABLE IF NOT EXISTS `climax_settings` (`uuid` VARCHAR(36) NOT NULL PRIMARY KEY," +
            " `duelRequests` BOOLEAN DEFAULT TRUE NOT NULL, `teamRequests` BOOLEAN DEFAULT TRUE NOT NULL, `killEffect` VARCHAR(32) DEFAULT 'DEFAULT' NOT NULL," +
            " `killSound` VARCHAR(32) DEFAULT 'NONE' NOT NULL,  `trail` VARCHAR(32) DEFAULT 'NONE' NOT NULL,  `privateMessaging` BOOLEAN DEFAULT TRUE NOT NULL);";
    public static final String CREATE_SETTINGS = "INSERT IGNORE INTO `climax_settings` (`uuid`, `duelRequests`, `teamRequests`, `killEffect`, `killSound`," +
            " `trail`, `privateMessaging`) VALUES (?, ?, ?, ?, ?, ?, ?);";
    public static final String UPDATE_SETTINGS = "UPDATE `climax_playerdata` SET `duelRequests` = ?, `teamRequests` = ?, `killEffect` = ?, `killSound` = ?, `trail` = ?," +
            " `privateMessaging` = ? WHERE `uuid` = ?;";
            */

    // ACTUAL STUFF ---------------------------------------------------------------------

    private final Plugin plugin;
    private final String address;
    private final int port;
    private final String name;
    private final String username;
    private final String password;
    private Logger logger = Logger.getLogger("Climax");
    private Connection connection;
    @Getter
    private Map<UUID, Map<String, Object>> temporaryPlayerData = new HashMap<>();

    /**
     * Defines a new MySQL connection
     *
     * @param address  Address of the MySQL server
     * @param port     Port of the MySQL server
     * @param name     Name of the database
     * @param username Name of user with rights to the database
     * @param password Password of user with rights to the database
     */
    public MySQL(Plugin plugin, String address, int port, String name, String username, String password) {
        this.plugin = plugin;
        this.address = address;
        this.port = port;
        this.name = name;
        this.username = username;
        this.password = password;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/" + name, username, password);
        } catch (SQLException e) {
            logger.severe("Could not create MySQL connection!");
            e.printStackTrace();
        }

        executeUpdate(CREATE_PLAYERDATA_TABLE);
        executeUpdate(CREATE_PUNISHMENTS_TABLE);
        //executeUpdate(CREATE_SETTINGS_TABLE);
    }

    /**
     * Gets the MySQL Connection
     *
     * @return The MySQL Connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Runs a MySQL query asynchronously
     *
     * @param runnable TrailsRunnable to run async
     */
    private void runAsync(Runnable runnable) {
        if (plugin.isEnabled()) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
        } else {
            runnable.run();
        }
    }

    /**
     * Executes a MySQL query
     * <b>WARNING:</b> NOT ASYNC
     *
     * @param query  The query to run on the database
     * @param values The values to insert into the query
     */
    public synchronized ResultSet executeQuery(String query, Object... values) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/" + name, username, password);
            }

            PreparedStatement statement = connection.prepareStatement(query);

            int i = 0;
            for (Object value : values) {
                statement.setObject(++i, value);
            }

            return statement.executeQuery();
        } catch (SQLException e) {
            logger.severe("Could not execute MySQL query!");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Executes a MySQL update
     *
     * @param query The query to run on the database
     */
    public void executeUpdate(String query, Object... values) {
        runAsync(() -> {
            try {
                if (connection == null || connection.isClosed()) {
                    connection = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/" + name, username, password);
                }

                PreparedStatement statement = connection.prepareStatement(query);

                int i = 0;
                for (Object value : values) {
                    statement.setObject(++i, value);
                }

                statement.executeUpdate();
            } catch (SQLException e) {
                logger.severe("Could not execute MySQL query!");
                e.printStackTrace();
            }
        });
    }

    public synchronized void executeUpdateSync(String query, Object... values) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/" + name, username, password);
            }

            PreparedStatement statement = connection.prepareStatement(query);

            int i = 0;
            for (Object value : values) {
                statement.setObject(++i, value);
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Could not execute MySQL query!");
            e.printStackTrace();
        }
    }

    /**
     * Update Player Data
     *
     * @param column The column to update
     * @param to     What to update the column to
     * @param uuid   UUID of the player to update
     */
    public synchronized void updatePlayerData(String column, Object to, UUID uuid) {
        executeUpdate("UPDATE `climax_playerdata` SET " + column + " = ? WHERE uuid = ?;", to, uuid.toString());
        //executeUpdate("UPDATE `climax_settings` SET " + column + " = ? WHERE uuid = ?;", to, uuid.toString());
    }

    /**
     * Get data of a player
     *
     * @param uuid UUID of the player to get data of
     * @return Data of player
     */
    public PlayerData getPlayerData(UUID uuid) {
        if (uuid == null) {
            return null;
        }

        ResultSet data = executeQuery(GET_PLAYERDATA_FROM_UUID, uuid.toString());
        //ResultSet playerSettings = executeQuery(GET_SETTINGS, uuid.toString());

        if (data == null /*|| duelData == null || playerSettings == null*/) {
            return null;
        }

        try {
            if (data.next() /*&& playerSettings.next()*/) {
                String ip = data.getString("ip");
                Rank rank = Rank.valueOf(data.getString("rank"));
                int balance = data.getInt("balance");
                int kills = data.getInt("kills");
                int deaths = data.getInt("deaths");
                //String achievements = data.getString("achievements");
                String nickname = data.getString("nickname");
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                double kdr = Double.parseDouble(decimalFormat.format(data.getDouble("kdr")));
                int topks = data.getInt("topks");
                /*
                boolean teamRequests = playerSettings.getBoolean("teamRequests");
                String killEffect = playerSettings.getString("killEffect");
                String killSound = playerSettings.getString("killSound");
                String trail = playerSettings.getString("trail");
                boolean privateMessaging = playerSettings.getBoolean("privateMessaging");
                */

                PlayerData playerData = new PlayerData(this, uuid, ip, rank, balance, kills, deaths,
                        nickname, kdr, topks, new ArrayList<>());

                ResultSet punishments = executeQuery(GET_PUNISHMENTS_FROM_UUID, uuid.toString());
                while (punishments != null && punishments.next()) {
                    Punishment.PunishType type = Punishment.PunishType.valueOf(punishments.getString("type"));
                    long time = punishments.getLong("time");
                    long expiration = punishments.getLong("expiration");
                    UUID punisherUUID = UUID.fromString(punishments.getString("punisheruuid"));
                    String reason = punishments.getString("reason");
                    playerData.getPunishments().add(new Punishment(uuid, type, time, expiration, punisherUUID, reason));
                }

                return playerData;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Create player data
     *
     * @param uuid UUID of the player to create data of
     * @param ip IP address of the player to create data of
     */
    public void createPlayerData(UUID uuid, String ip) {
        executeUpdateSync(CREATE_PLAYERDATA, uuid.toString(), ip, Rank.DEFAULT.toString(), 0, 0, 0, null, 0, 0);
        //executeUpdate(CREATE_SETTINGS, uuid.toString(), true, true, "DEFAULT", "NONE", "NONE", true);
    }

    /**
     * Save cached player data
     *
     * @param playerData Cached player data to save
     */
    public void savePlayerData(PlayerData playerData) {
        executeUpdate(UPDATE_PLAYERDATA,
                playerData.getIp(),
                playerData.getRank().toString(),
                playerData.getBalance(),
                playerData.getKills(),
                playerData.getDeaths(),
                //playerData.getAchievements(),
                playerData.getNickname(),
                playerData.getKdr(),
                playerData.getTopks(),
                playerData.getUuid().toString()
        );

        /*
        executeUpdate(UPDATE_SETTINGS,
                playerData.getUuid().toString(),
                playerData.isTeamRequests(),
                playerData.getKillEffect(),
                playerData.getKillSound(),
                playerData.getTrail(),
                playerData.isPrivateMessaging()
        );*/
    }

    /**
     * Get punishment's from an IP address
     * <b>WARNING:</b> NOT ASYNC
     *
     * @param ip IP address to get punishments of
     * @return List of punishments
     */
    public List<Punishment> getPunishmentsFromIP(String ip) {
        List<Punishment> punishments = new ArrayList<>();
        ResultSet data = executeQuery(GET_PLAYERDATA_FROM_IP, ip);

        try {
            if (data != null && data.next()) {
                UUID uuid = UUID.fromString(data.getString("uuid"));

                ResultSet punishmentsResults = executeQuery(GET_PUNISHMENTS_FROM_UUID, uuid.toString());
                while (punishmentsResults != null && punishmentsResults.next()) {
                    Punishment.PunishType type = Punishment.PunishType.valueOf(punishmentsResults.getString("type"));
                    long time = punishmentsResults.getLong("time");
                    long expiration = punishmentsResults.getLong("expiration");
                    UUID punisherUUID = UUID.fromString(punishmentsResults.getString("punisheruuid"));
                    String reason = punishmentsResults.getString("reason");
                    punishments.add(new Punishment(uuid, type, time, expiration, punisherUUID, reason));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return punishments;
    }
}
