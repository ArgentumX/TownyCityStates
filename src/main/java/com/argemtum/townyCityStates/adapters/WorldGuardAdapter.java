package com.argemtum.townyCityStates.adapters;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.World;

import javax.inject.Inject;

public class WorldGuardAdapter {
    private final WorldGuard worldGuard;
    private final int CHUNK_SIZE = 16;
    @Inject
    public WorldGuardAdapter() {
        this.worldGuard = WorldGuard.getInstance();
    }

    public ProtectedRegion getRegion(World world, String regionName) {
        RegionManager manager = getRegionManager(world);
        if (manager == null) {
            return null;
        }
        return manager.getRegion(regionName);
    }

    public boolean hasRegion(World world, String regionName) {
        RegionManager manager = getRegionManager(world);
        if (manager == null) {
            return false;
        }
        return manager.hasRegion(regionName);
    }

    public void createChunkCubeRegion(World world, String regionNameId, int centerX, int centerZ, int radius){
        int minX = (centerX - radius) * CHUNK_SIZE;
        int minZ = (centerZ - radius) * CHUNK_SIZE;
        BlockVector3 minBlock = BlockVector3.at(minX, world.getMinHeight(), minZ);

        int maxX = (centerX + radius + 1) * CHUNK_SIZE - 1;
        int maxZ = (centerZ + radius + 1) * CHUNK_SIZE - 1;
        BlockVector3 maxBlock = BlockVector3.at(maxX, world.getMaxHeight() - 1, maxZ);

        createCuboidRegion(world, regionNameId, minBlock, maxBlock);
    }

    public void createCuboidRegion(World world, String regionId, BlockVector3 min, BlockVector3 max) {
        ProtectedRegion region = new ProtectedCuboidRegion(regionId, min, max);
        addRegion(world, region);
    }
    public void addRegion(World world, ProtectedRegion region) {
        RegionManager manager = getRegionManager(world);
        if (manager != null) {
            manager.addRegion(region);
        }
    }
    public RegionManager getRegionManager(World world) {
        return worldGuard.getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
    }
}