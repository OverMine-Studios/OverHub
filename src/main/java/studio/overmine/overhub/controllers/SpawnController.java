package studio.overmine.overhub.controllers;

import org.bukkit.Bukkit;
import studio.overmine.overhub.OverHub;
import studio.overmine.overhub.utilities.FileConfig;
import studio.overmine.overhub.utilities.SerializeUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SpawnController {

    private final FileConfig configFile;
    private Location spawnLocation;

    public SpawnController(OverHub plugin) {
        this.configFile = plugin.getFileConfig("config");
        this.spawnLocation = SerializeUtil.deserializeLocation(configFile.getString("spawn-location"));
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = location;
        this.configFile.set("spawn-location", SerializeUtil.serializeLocation(location));
        this.configFile.save();
    }

    public Location getSpawnLocation(Player player) {
        return spawnLocation == null ? player.getWorld().getSpawnLocation() : spawnLocation;
    }

    public void toSpawn(Player player) {
        Bukkit.getScheduler().runTask(OverHub.getPlugin(OverHub.class), () -> player.teleport(getSpawnLocation(player)));
    }
}
