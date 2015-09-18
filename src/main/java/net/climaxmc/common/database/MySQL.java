package net.climaxmc.common.database;

import lombok.Getter;
import net.climaxmc.Administration.Punishments.Punishment;
import org.bukkit.plugin.Plugin;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * MySQL Tools
 *
 * @author computerwizjared
 */
public class MySQL {
    public static final String GET_PLAYERDATA = "SELECT * FROM `climax_playerdata` WHERE `uuid` = ?;";
    public static final String CREATE_PLAYERDATA_TABLE = "CREATE TABLE IF NOT EXISTS `climax_playerdata` (`uuid` VARCHAR(36) NOT NULL PRIMARY KEY, `rank` VARCHAR(20) DEFAULT 'DEFAULT' NOT NULL, `balance` INT DEFAULT 0 NOT NULL, `kills` INT DEFAULT 0 NOT NULL, `deaths` INT DEFAULT 0 NOT NULL, `nickname` VARCHAR(32) DEFAULT NULL);";
    public static final String CREATE_PLAYERDATA = "INSERT IGNORE INTO `climax_playerdata` (`uuid`, `rank`, `balance`, `kills`, `deaths`, `nickname`) VALUES (?, ?, ?, ?, ?, ?);";
    public static final String UPDATE_PLAYERDATA = "UPDATE `climax_playerdata` SET `rank` = ?, `balance` = ?, `kills` = ?, `deaths` = ?, `nickname` = ? WHERE `uuid` = ?;";
    public static final String CREATE_PUNISHMENTS_TABLE = "CREATE TABLE IF NOT EXISTS `climax_punishments` (`uuid` VARCHAR(36) NOT NULL, `type` VARCHAR(32) NOT NULL, `time` BIGINT(20) NOT NULL, `expiration` BIGINT(20) NOT NULL, `punisheruuid` VARCHAR(26) NOT NULL, `reason` TEXT NOT NULL);";
    public static final String CREATE_PUNISHMENT = "INSERT IGNORE INTO `climax_punishments` (`uuid`, `type`, `time`, `expiration`, `punisheruuid`, `reason`) VALUES (?, ?, ?, ?, ?, ?);";
    public static final String GET_PUNISHMENTS = "SELECT * FROM `climax_punishments` WHERE `uuid` = ?;";
    public static final String UPDATE_PUNISHMENT_TIME = "UPDATE `climax_punishments` SET `expiration` = ? WHERE `uuid` = ? AND `type` = ? AND `time` = ?;";
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
     * @param runnable Runnable to run async
     */
    private void runAsync(Runnable runnable) {
        if (plugin.isEnabled()) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
        } else {
            runnable.run();
        }
    }

    /**
     * Executes a MySQL query (NON-ASYNC)
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
    public synchronized void executeUpdate(String query, Object... values) {
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

    /**
     * Update Player Data
     *
     * @param column The column to update
     * @param to     What to update the column to
     * @param uuid   UUID of the player to update
     */
    public synchronized void updatePlayerData(String column, Object to, UUID uuid) {
        executeUpdate("UPDATE `climax_playerdata` SET " + column + " = ? WHERE uuid = ?;", to, uuid.toString());
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

        ResultSet data = executeQuery(GET_PLAYERDATA, uuid.toString());

        if (data == null) {
            return null;
        }

        try {
            if (data.next()) {
                Rank rank = Rank.valueOf(data.getString("rank"));
                int balance = data.getInt("balance");
                int kills = data.getInt("kills");
                int deaths = data.getInt("deaths");
                String nickname = data.getString("nickname");

                PlayerData playerData = new PlayerData(this, uuid, rank, balance, kills, deaths, nickname, new ArrayList<>());

                ResultSet punishments = executeQuery(GET_PUNISHMENTS, uuid.toString());
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
     */
    public synchronized void createPlayerData(UUID uuid) {
        executeUpdate(CREATE_PLAYERDATA, uuid.toString(), Rank.DEFAULT.toString(), 0, 0, 0, null);
    }

    /**
     * Save cached player data
     *
     * @param playerData Cached player data to save
     */
    public void savePlayerData(PlayerData playerData) {
        executeUpdate(UPDATE_PLAYERDATA,
                playerData.getRank().toString(),
                playerData.getBalance(),
                playerData.getKills(),
                playerData.getDeaths(),
                playerData.getNickname(),
                playerData.getUuid().toString()
        );
    }
}