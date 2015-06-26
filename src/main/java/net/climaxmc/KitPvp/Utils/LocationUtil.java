package net.climaxmc.KitPvp.Utils;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;


public class LocationUtil {
    // The player can stand inside these materials
    public static final Set<Material> HOLLOW_MATERIALS = new HashSet<>();
    private static final HashSet<Material> TRANSPARENT_MATERIALS = new HashSet<>();

    static {
        HOLLOW_MATERIALS.add(Material.AIR);
        HOLLOW_MATERIALS.add(Material.SAPLING);
        HOLLOW_MATERIALS.add(Material.POWERED_RAIL);
        HOLLOW_MATERIALS.add(Material.DETECTOR_RAIL);
        HOLLOW_MATERIALS.add(Material.LONG_GRASS);
        HOLLOW_MATERIALS.add(Material.DEAD_BUSH);
        HOLLOW_MATERIALS.add(Material.YELLOW_FLOWER);
        HOLLOW_MATERIALS.add(Material.RED_ROSE);
        HOLLOW_MATERIALS.add(Material.BROWN_MUSHROOM);
        HOLLOW_MATERIALS.add(Material.RED_MUSHROOM);
        HOLLOW_MATERIALS.add(Material.TORCH);
        HOLLOW_MATERIALS.add(Material.REDSTONE_WIRE);
        HOLLOW_MATERIALS.add(Material.SEEDS);
        HOLLOW_MATERIALS.add(Material.SIGN_POST);
        HOLLOW_MATERIALS.add(Material.WOODEN_DOOR);
        HOLLOW_MATERIALS.add(Material.LADDER);
        HOLLOW_MATERIALS.add(Material.RAILS);
        HOLLOW_MATERIALS.add(Material.WALL_SIGN);
        HOLLOW_MATERIALS.add(Material.LEVER);
        HOLLOW_MATERIALS.add(Material.STONE_PLATE);
        HOLLOW_MATERIALS.add(Material.IRON_DOOR_BLOCK);
        HOLLOW_MATERIALS.add(Material.WOOD_PLATE);
        HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH_OFF);
        HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH_ON);
        HOLLOW_MATERIALS.add(Material.STONE_BUTTON);
        HOLLOW_MATERIALS.add(Material.SNOW);
        HOLLOW_MATERIALS.add(Material.SUGAR_CANE_BLOCK);
        HOLLOW_MATERIALS.add(Material.DIODE_BLOCK_OFF);
        HOLLOW_MATERIALS.add(Material.DIODE_BLOCK_ON);
        HOLLOW_MATERIALS.add(Material.PUMPKIN_STEM);
        HOLLOW_MATERIALS.add(Material.MELON_STEM);
        HOLLOW_MATERIALS.add(Material.VINE);
        HOLLOW_MATERIALS.add(Material.FENCE_GATE);
        HOLLOW_MATERIALS.add(Material.WATER_LILY);
        HOLLOW_MATERIALS.add(Material.NETHER_WARTS);
        HOLLOW_MATERIALS.add(Material.CARPET);

        TRANSPARENT_MATERIALS.addAll(HOLLOW_MATERIALS.stream().collect(Collectors.toList()));
        TRANSPARENT_MATERIALS.add(Material.WATER);
        TRANSPARENT_MATERIALS.add(Material.STATIONARY_WATER);
    }

    public static final int RADIUS = 3;
    public static final Vector3D[] VOLUME;

    public static ItemStack convertBlockToItem(final Block block) {
        final ItemStack is = new ItemStack(block.getType(), 1, (short) 0, block.getData());
        switch (is.getType()) {
            case WOODEN_DOOR:
                is.setType(Material.WOOD_DOOR);
                is.setDurability((short) 0);
                break;
            case IRON_DOOR_BLOCK:
                is.setType(Material.IRON_DOOR);
                is.setDurability((short) 0);
                break;
            case SIGN_POST:
            case WALL_SIGN:
                is.setType(Material.SIGN);
                is.setDurability((short) 0);
                break;
            case CROPS:
                is.setType(Material.SEEDS);
                is.setDurability((short) 0);
                break;
            case CAKE_BLOCK:
                is.setType(Material.CAKE);
                is.setDurability((short) 0);
                break;
            case BED_BLOCK:
                is.setType(Material.BED);
                is.setDurability((short) 0);
                break;
            case REDSTONE_WIRE:
                is.setType(Material.REDSTONE);
                is.setDurability((short) 0);
                break;
            case REDSTONE_TORCH_OFF:
            case REDSTONE_TORCH_ON:
                is.setType(Material.REDSTONE_TORCH_ON);
                is.setDurability((short) 0);
                break;
            case DIODE_BLOCK_OFF:
            case DIODE_BLOCK_ON:
                is.setType(Material.DIODE);
                is.setDurability((short) 0);
                break;
            case DOUBLE_STEP:
                is.setType(Material.STEP);
                break;
            case TORCH:
            case RAILS:
            case LADDER:
            case WOOD_STAIRS:
            case COBBLESTONE_STAIRS:
            case LEVER:
            case STONE_BUTTON:
            case FURNACE:
            case DISPENSER:
            case PUMPKIN:
            case JACK_O_LANTERN:
            case WOOD_PLATE:
            case STONE_PLATE:
            case PISTON_STICKY_BASE:
            case PISTON_BASE:
            case IRON_FENCE:
            case THIN_GLASS:
            case TRAP_DOOR:
            case FENCE:
            case FENCE_GATE:
            case NETHER_FENCE:
                is.setDurability((short) 0);
                break;
            case FIRE:
                return null;
            case PUMPKIN_STEM:
                is.setType(Material.PUMPKIN_SEEDS);
                break;
            case MELON_STEM:
                is.setType(Material.MELON_SEEDS);
                break;
        }
        return is;
    }


    public static class Vector3D {
        public int x;
        public int y;
        public int z;

        public Vector3D(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    static {
        List<Vector3D> pos = new ArrayList<>();
        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int y = -RADIUS; y <= RADIUS; y++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                    pos.add(new Vector3D(x, y, z));
                }
            }
        }
        Collections.sort(pos, (a, b) -> (a.x * a.x + a.y * a.y + a.z * a.z) - (b.x * b.x + b.y * b.y + b.z * b.z));
        VOLUME = pos.toArray(new Vector3D[pos.size()]);
    }

    public static Location getTarget(final LivingEntity entity) throws Exception {
        final Block block = entity.getTargetBlock(TRANSPARENT_MATERIALS, 300);
        if (block == null) {
            throw new Exception("Not targeting a block");
        }
        return block.getLocation();
    }

    static boolean isBlockAboveAir(final World world, final int x, final int y, final int z) {
        return y > world.getMaxHeight() || HOLLOW_MATERIALS.contains(world.getBlockAt(x, y - 1, z).getType());
    }

    public static boolean isBlockUnsafeForUser(final Player player, final World world, final int x, final int y, final int z) {
        return !(player.isOnline() && world.equals(player.getWorld()) && player.getGameMode() == GameMode.CREATIVE && player.getAllowFlight()) && (isBlockDamaging(world, x, y, z) || isBlockAboveAir(world, x, y, z));

    }

    public static boolean isBlockUnsafe(final World world, final int x, final int y, final int z) {
        return isBlockDamaging(world, x, y, z) || isBlockAboveAir(world, x, y, z);
    }

    public static boolean isBlockDamaging(final World world, final int x, final int y, final int z) {
        final Block below = world.getBlockAt(x, y - 1, z);
        return below.getType() == Material.LAVA || below.getType() == Material.STATIONARY_LAVA || below.getType() == Material.FIRE || below.getType() == Material.BED_BLOCK || (!HOLLOW_MATERIALS.contains(world.getBlockAt(x, y, z).getType().getId())) || (!HOLLOW_MATERIALS.contains(world.getBlockAt(x, y + 1, z).getType().getId()));
    }

    // Not needed if using getSafeDestination(loc)
    public static Location getRoundedDestination(final Location loc) {
        final World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = (int) Math.round(loc.getY());
        int z = loc.getBlockZ();
        return new Location(world, x + 0.5, y, z + 0.5, loc.getYaw(), loc.getPitch());
    }

    public static Location getSafeDestination(final Player player, final Location loc) {
        if (player.isOnline() && loc.getWorld().equals(player.getWorld())
                && player.getGameMode() == GameMode.CREATIVE
                && player.getAllowFlight()) {
            if (shouldFly(loc)) {
                player.setFlying(true);
            }
            return getRoundedDestination(loc);
        }
        return getSafeDestination(loc);
    }

    public static Location getSafeDestination(final Location loc) {
        if (loc == null || loc.getWorld() == null) {
            return null;
        }
        final World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = (int) Math.round(loc.getY());
        int z = loc.getBlockZ();
        final int origX = x;
        final int origY = y;
        final int origZ = z;
        while (isBlockAboveAir(world, x, y, z)) {
            y -= 1;
            if (y < 0) {
                y = origY;
                break;
            }
        }
        if (isBlockUnsafe(world, x, y, z)) {
            x = Math.round(loc.getX()) == origX ? x - 1 : x + 1;
            z = Math.round(loc.getZ()) == origZ ? z - 1 : z + 1;
        }
        int i = 0;
        while (isBlockUnsafe(world, x, y, z)) {
            i++;
            if (i >= VOLUME.length) {
                x = origX;
                y = origY + RADIUS;
                z = origZ;
                break;
            }
            x = origX + VOLUME[i].x;
            y = origY + VOLUME[i].y;
            z = origZ + VOLUME[i].z;
        }
        while (isBlockUnsafe(world, x, y, z)) {
            y += 1;
            if (y >= world.getMaxHeight()) {
                x += 1;
                break;
            }
        }
        while (isBlockUnsafe(world, x, y, z)) {
            y -= 1;
            if (y <= 1) {
                x += 1;
                y = world.getHighestBlockYAt(x, z);
                if (x - 48 > loc.getBlockX()) {
                    return null;
                }
            }
        }
        return new Location(world, x + 0.5, y, z + 0.5, loc.getYaw(), loc.getPitch());
    }

    public static boolean shouldFly(Location loc) {
        final World world = loc.getWorld();
        final int x = loc.getBlockX();
        int y = (int) Math.round(loc.getY());
        final int z = loc.getBlockZ();
        int count = 0;
        while (LocationUtil.isBlockUnsafe(world, x, y, z) && y > -1) {
            y--;
            count++;
            if (count > 2) {
                return true;
            }
        }

        return y < 0;
    }
}