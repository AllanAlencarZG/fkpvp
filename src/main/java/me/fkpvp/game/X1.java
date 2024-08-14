package me.fkpvp.game;

import me.fkpvp.Fkpvp;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class X1 {
    private static final Location pos1 = new Location(Bukkit.getWorld("world"), -48.5f, 5.0f, -18.5f, 180.0f, 1.0f);
    private static final Location pos2 = new Location(Bukkit.getWorld("world"), -48.5f, 5.0f, -58.5, 0.0f, 1.0f);
    private static final Location SPAWN_LOCATION = new Location(Bukkit.getWorld("world"), -48.5f, 5.0f, -70.5f);

    public static List<HashMap<String, UUID>> invitations = new ArrayList<>();
    public static List<HashMap<String, UUID>> match = new ArrayList<>();

    public static List<UUID> enablePvP = new ArrayList<>();
    public static List<UUID> randomQueue = new ArrayList<>();
    public static FileConfiguration kitConfig;

    public static void addPlayerItem(PlayerInventory player_inventory, Material material, String display_name, int index)
    {
        ItemStack item = new ItemStack(material);
        ItemMeta item_meta = item.getItemMeta();
        item_meta.displayName(Component.text(display_name));
        item.setItemMeta(item_meta);
        player_inventory.setItem(index, item);
    }

    public static void addPlayerToMatch(Player player1, Player player2)
    {
        HashMap<String, UUID> players = new HashMap<>();

        players.put("from", player1.getUniqueId());
        players.put("to", player2.getUniqueId());

        X1.match.add(players);
    }
    public static void goInvitationArea(Player player)
    {
        player.teleport(SPAWN_LOCATION);
        PlayerInventory player_inventory = player.getInventory();
        player_inventory.clear();
        player.setHealth(20);

        addPlayerItem(player_inventory, Material.BLAZE_ROD, "§bInvite 1v1", 0);
        addPlayerItem(player_inventory, Material.GRAY_DYE, "§cOFF §bRandom Invite 1v1", 4);
    }

    public static void startDuel(Player player1, Player player2)
    {
        X1.addPlayerToMatch(player1, player2);

        for (HashMap<String, UUID> players : invitations) {
            if (players.containsValue(player1.getUniqueId()) && players.containsValue(player2.getUniqueId()))
            {
                match.remove(players);
                break;
            }
        }

        List<Player> players = Arrays.asList(player1, player2);
        List<Location> positions = Arrays.asList(pos1, pos2);

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Location pos = positions.get(i);

            enablePvP.add(player.getUniqueId());
            player.teleport(pos);
            setKit(player);
        }

    }


        public static void stopDuel(Player player1, Player player2)
        {
            for (HashMap<String, UUID> players : match) {
                if (players.containsValue(player1.getUniqueId()) && players.containsValue(player2.getUniqueId()))
                {
                    match.remove(players);
                    break;
                }
            }

            List<Player> players = Arrays.asList(player1, player2);
            for (Player player : players) {
                enablePvP.remove(player.getUniqueId());
                goInvitationArea(player);
            }

        }

    private static ItemStack getArmorItem(String path)
    {
        String material_string = kitConfig.getString(path);
        assert material_string != null;
        Material material = Material.getMaterial(material_string);
        assert material != null;

        return new ItemStack(material);
    }

    public static void setKit(Player player)
    {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();

        for (int i = 0; i < 36; i++)
        {
            String item_string = kitConfig.getString("Inventory.Items." + i + ".Material");
            int amount = kitConfig.getInt("Inventory.Items." + i + ".Amount", 1);

            if (item_string == null)
            {
                String fill_item_string = kitConfig.getString("Inventory.Items.Fill.Material");

                if (fill_item_string == null) continue;

                Material material = Material.getMaterial(fill_item_string);

                if (material == null) continue;

                ItemStack item = new ItemStack(material);
                inventory.setItem(i, item);

                continue;
            }

            Material material = Material.getMaterial(item_string);

            assert material != null;

            ItemStack item = new ItemStack(material);
            item.setAmount(amount);
            inventory.setItem(i, item);

        }

        ItemStack helmet = getArmorItem("Inventory.Armor.Helmet.Material");
        ItemStack chestplate = getArmorItem("Inventory.Armor.Chestplate.Material");
        ItemStack leggings = getArmorItem("Inventory.Armor.Leggings.Material");
        ItemStack boots = getArmorItem("Inventory.Armor.Boots.Material");

        inventory.setHelmet(helmet);
        inventory.setChestplate(chestplate);
        inventory.setLeggings(leggings);
        inventory.setBoots(boots);

    }

    public static boolean playersInMatch(Player player1, Player player2)
    {
        for (HashMap<String, UUID> players : match) {
            if (players.containsValue(player1.getUniqueId()) && players.containsValue(player2.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    public static void loadKitConfig(Fkpvp fkpvp)
    {
        InputStream KitFile = fkpvp.getResource("kits/1v1.yml");

        assert KitFile != null;

        kitConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(KitFile));

    }
}
