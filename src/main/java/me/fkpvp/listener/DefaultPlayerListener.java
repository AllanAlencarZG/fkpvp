package me.fkpvp.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import static me.fkpvp.game.X1.goInvitationArea;

public class DefaultPlayerListener implements Listener {

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        player.setGameMode(GameMode.ADVENTURE);

        goInvitationArea(player);

        player.setFoodLevel(20);
    }

    @EventHandler
    public void onHungerDeplete(FoodLevelChangeEvent event) {

        event.setCancelled(true);
    }

    @EventHandler
    public void OnBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        if (!player.hasPermission("fkpvp.admin"))
        {
            player.sendMessage("§4Você não tem permissão para quebrar bloco!");
            event.setCancelled(true);
        }
    }
}
