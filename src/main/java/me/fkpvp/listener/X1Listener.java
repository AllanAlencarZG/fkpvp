package me.fkpvp.listener;

import me.fkpvp.Fkpvp;
import me.fkpvp.game.X1;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class X1Listener implements Listener {
    @EventHandler
    public void invitePlayer(PlayerInteractEntityEvent event)
    {
        Player from_player = event.getPlayer();
        if (from_player.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD && event.getRightClicked() instanceof Player to_player)
        {
            for (int i = 0; i < X1.invitations.size(); i++)
            {
                UUID from = X1.invitations.get(i).get("from");
                UUID to = X1.invitations.get(i).get("to");

                if (from == from_player.getUniqueId() && to == to_player.getUniqueId() || from == to_player.getUniqueId() && to == from_player.getUniqueId())
                {
                        return;
                }
            }

            from_player.sendMessage("§eVocê convidou §b" + to_player.getName() + " §epara 1v1!");
            HashMap<String, UUID> invitation = new HashMap<>();

            invitation.put("from", from_player.getUniqueId());
            invitation.put("to", to_player.getUniqueId());

            X1.invitations.add(invitation);
        }
    }

    @EventHandler
    public void acceptPlayerInvitation(PlayerInteractEntityEvent event)
    {
        Player from_player = event.getPlayer();
        if (from_player.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD && event.getRightClicked() instanceof Player to_player)
        {
            for (int i = 0; i < X1.invitations.size(); i++)
            {
                UUID from = X1.invitations.get(i).get("from");
                UUID to = X1.invitations.get(i).get("to");

                if (from == to_player.getUniqueId() && to == from_player.getUniqueId())
                {
                    from_player.sendMessage("§eVocê aceitou o convite de  §b" + to_player.getName() + " §epara 1v1!");
                    to_player.sendMessage("§eVocê entrou em batalha contra §b" + from_player.getName());
                    from_player.sendMessage("§eVocê entrou em batalha contra §b" + to_player.getName());
                    X1.startDuel(from_player, to_player);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        UUID player_id = player.getUniqueId();

        if ((event.getMaterial() == Material.GRAY_DYE || event.getMaterial() == Material.LIME_DYE) && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR))
        {
            String item_display_name;
            ItemStack item;
            if (X1.randomQueue.contains(player_id))
            {
                item = new ItemStack(Material.GRAY_DYE);
                item_display_name = "§cOFF §bRandom Invite 1v1";
                X1.randomQueue.remove(player_id);
            } else {
                if (!X1.randomQueue.isEmpty())
                {
                    UUID random_player_id = X1.randomQueue.get(0);
                    Player random_player = Bukkit.getPlayer(random_player_id);

                    X1.randomQueue.remove(random_player_id);
                    assert random_player != null;
                    X1.startDuel(player, random_player);

                    return;
                    
                }

                item = new ItemStack(Material.LIME_DYE);
                item_display_name = "§aON §bRandom Invite 1v1";
                X1.randomQueue.add(player_id);
            }
            (new BukkitRunnable() {
                public void run() {
                    ItemMeta item_meta = item.getItemMeta();
                    item_meta.displayName(Component.text(item_display_name));
                    item.setItemMeta(item_meta);
                    player.getInventory().setItemInMainHand(item);
                }
            }).runTaskLater(Fkpvp.instance, 1L);
        }
    }

    @EventHandler
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event)
    {
        if (event.getEntity() instanceof Player player && event.getDamager() instanceof Player player_damager)
        {

            if ((X1.enablePvP.contains(player.getUniqueId()) && X1.playersInMatch(player, player_damager)) || player_damager.hasPermission("fkpvp.admin"))
            {
                if (player.getHealth() - event.getDamage() > 0.0f)
                {
                    return;
                } else
                {
                    X1.stopDuel(player, player_damager);
                }
            }

            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();

        if (X1.enablePvP.contains(player.getUniqueId()))
        {
            World world = Bukkit.getWorld("world");
            assert world != null;
            world.sendMessage(Component.text("§b" + player.getName() + " §6saiu correndo do 1v1!"));

        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event)
    {
        if (X1.enablePvP.contains(event.getPlayer().getUniqueId()))
        {
            event.getItemDrop().remove();
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        Player player = event.getPlayer();

        if (X1.enablePvP.contains(player.getUniqueId()))
        {
            World world = Bukkit.getWorld("world");
            assert world != null;
            world.sendMessage(Component.text("§b" + player.getName() + " §6saiu correndo do 1v1!"));


        }
    }
}
