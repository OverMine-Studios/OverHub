package studio.overmine.overhub.controllers;

import org.bukkit.entity.Player;
import studio.overmine.overhub.OverHub;
import studio.overmine.overhub.models.combat.CombatPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatController {

    private final OverHub plugin;
    private final Map<UUID, CombatPlayer> combats;

    public CombatController(OverHub plugin) {
        this.plugin = plugin;
        this.combats = new HashMap<>();
    }

    public CombatPlayer getCombatPlayer(Player player) {
        return combats.get(player.getUniqueId());
    }

    public void addCombatPlayer(Player player) {
        CombatPlayer combatPlayer = new CombatPlayer(plugin, player);
        combats.put(player.getUniqueId(), combatPlayer);
        combatPlayer.startCombatTask(plugin, this);
    }

    public void removeCombatPlayer(Player player) {
        combats.remove(player.getUniqueId());
    }
}
