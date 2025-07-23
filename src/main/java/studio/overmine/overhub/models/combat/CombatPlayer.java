package studio.overmine.overhub.models.combat;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import studio.overmine.overhub.OverHub;
import studio.overmine.overhub.controllers.CombatController;
import studio.overmine.overhub.controllers.HotbarController;
import studio.overmine.overhub.controllers.SpawnController;
import studio.overmine.overhub.models.resources.types.ConfigResource;
import studio.overmine.overhub.tasks.CombatTask;

/**
 * @author Risas
 * @date 06-07-2025
 * @discord https://risas.me/discord
 */

@Getter @Setter
public class CombatPlayer {

    private final OverHub plugin;
    private final Player player;
    private CombatStatus status;
    private CombatTask combatTask;
    private final HotbarController hotbarController;
    private final SpawnController spawnController;

    public CombatPlayer(OverHub plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.hotbarController = plugin.getHotbarController();
        this.spawnController = plugin.getSpawnController();
        this.status = CombatStatus.EQUIPPING;
    }

    public boolean isPvP() {
        return status == CombatStatus.UN_EQUIPPING;
    }

    public boolean isCombatTaskRunning() {
        return combatTask != null;
    }

    public void startCombatTask(OverHub plugin, CombatController combatController) {
        if (combatTask != null) {
            combatTask.cancel();
        }

        combatTask = new CombatTask(plugin, this, combatController);
        combatTask.start();
    }

    public void stopCombatTask() {
        if (combatTask != null) {
            combatTask.cancel();
            combatTask = null;
        }
    }

    public void onApplyEquipment(CombatController combatController) {
        switch (status) {
            case EQUIPPING:
                stopCombatTask();
                status = CombatStatus.UN_EQUIPPING;
                player.getInventory().clear();
                player.getInventory().setArmorContents(ConfigResource.HUB_SWORD_SYSTEM_EQUIPMENT);
                player.getInventory().setItemInMainHand(ConfigResource.HUB_SWORD_SYSTEM_SWORD);
                break;
            case UN_EQUIPPING:
                stopCombatTask();

                combatController.removeCombatPlayer(player);
                player.getInventory().setArmorContents(null);
                player.getInventory().clear();
                hotbarController.giveHotbar(player);
                spawnController.toSpawn(player);
                break;
        }
    }
}
