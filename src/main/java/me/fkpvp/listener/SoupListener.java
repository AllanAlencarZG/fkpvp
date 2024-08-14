package me.fkpvp.listener;

import me.fkpvp.game.X1;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class SoupListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {

        if (event.getMaterial() == Material.MUSHROOM_STEW && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR))
        {

            Player player = event.getPlayer();
            if (player.getHealth() + 6 > 20)
            {
                player.setHealth(20);
            } else
            {
                player.setHealth(player.getHealth() + 10);
            }
            ItemStack item = new ItemStack(Material.BOWL);
            PlayerInventory inventory = player.getInventory();
            inventory.setItemInMainHand(item);
        }
    }
}
