package net.blockcade.pit.InventoryMenus;

import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Managers.EntityManager;
import net.blockcade.Arcade.Utils.Formatting.Item;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Utils.JavaUtils;
import net.blockcade.pit.Enums.Ability;
import net.blockcade.pit.Main;
import net.blockcade.pit.Utils.PITPlayer;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.npc.skin.SkinnableEntity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class Shop {

    NPC npc;

    public Shop(Game game) {
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        NPC npc = registry.createNPC(EntityType.PLAYER, Text.format("&aFriendly Traider"));
        npc.spawn(JavaUtils.parseConfigLocation(Objects.requireNonNull(Main.getPlugin(net.blockcade.Arcade.Main.class).getConfig().getString("maps." + game.map().getName().toLowerCase() + ".TRAIDER"))));
        SkinnableEntity skin = (SkinnableEntity) npc.getEntity();
        skin.setSkinPersistent("Guard","DEgxYcBcrl3Dw+xzXxzVUx5YYGlr7VxM3x7NsFMsxPTpKtQO7VQHEtHg1t1c3S+QwDwB29OIwgLpArvg9cl6vMURO/5KXOjDSU6IVw5zY141a5/hucsOmZDx2ojL1x/wG2Tk/Ckf/pG5F11PFRPaM6/i/9j0QtttS7WDT14r9ZaJv5j2mhiiJIataSsuaj1AX7DGKVPcvf8iZF/wjQ1X8xrpIVgKhiCxsG5pxjXoYJMhbQekKFgttPRbTPRnBu7Y3p60H2UOmVZCR2VnKmT8OQjvqMRoiGgmXEp1FOSAD3SH7zI/crQQeHcIguWGi4DyljL0T6hW6SclsnAfIDWl8qBnUyxNfzj7GW0CR/tPznC1AUhARWz7nbBdPUnfkZl2vdHlwDSqFjxNTROQ5Zz8u357gtfvNR6kPqrHGLly77fe/m2znXK7UESvtNqpamh7UWP4iYdbndzNeDxT0VIRESGRLacFW8g4P4CvnCYfJvKZFON9PZxtkH8Ba1RwNvTYDPLAujZBB4+fqEkYOmw56ky8pGL5ZNg9Mx40Am8IIJVAiamdHvwLBHfzK2D7bMgenT5nO3+TobYL2VZBzKriTtof/vRJBCygPOCJl8C48vwLHB1Zx83AEL8b+euUpFTFYN37Av9RTd7sEOb85j7YU7bunRqLyj92iC6SIcBuRng=","eyJ0aW1lc3RhbXAiOjE1NjYyNTczNDgwMDEsInByb2ZpbGVJZCI6IjJjMTA2NGZjZDkxNzQyODI4NGUzYmY3ZmFhN2UzZTFhIiwicHJvZmlsZU5hbWUiOiJOYWVtZSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDYxMjZhNmY5OTJhZTg4YjYwMzNiYzIxMmNhMTEyZmYzZGQzZjcyN2VhYTdlZDkxYzc1NGU2OTU1YjAxY2I1MCJ9fX0=");
        game.EntityManager().AddGameEntity(npc.getEntity());
        game.EntityManager().setFunction(npc.getEntity(), new EntityManager.click() {
            @Override
            public void run(Player player) {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK,0.5f,0.5f);
                player.openInventory(getShop(game,player));
            }
        });
    }
    public Inventory getShop(Game game, Player p) {
        Inventory shop = Bukkit.createInventory(null,6*9,Text.format("&aThe friendly shopkeeper"));
        /*
         * [EMERALD_BLOCK - &aAbility 1]
         *  - &7Select a ability
         *
         * [COAL_BLOCK - &cNot Unlocked]
         *  - &7Unlocked at &6Level %level%&7.
         *  - &7See you then!
         *
         * [EXPERIENCE_BOTTLE - &bXP Boost]
         *  - &7Gain &b+10% XP&7 per kill.
         *  -
         *  - &eClick to purchase
         *  - &6Click to upgrade
         *  - &aFully upgraded
         *
         * [EMERALD = &bGem Boost]
         *  - &7Gain &b+10% Gems&7 per kill.
         *  -
         *  - &eClick to purchase
         *  - &6Click to upgrade
         *  - &aFully upgraded
         *
         */
        PITPlayer player = PITPlayer.getPlayer(p);
        List<Ability> abilities = player.getAbilities();
        Item[] ability = new Item[]{new Item(Material.EMERALD_BLOCK,"&aAbility 1"),new Item(Material.EMERALD_BLOCK,"&aAbility 2"),new Item(Material.EMERALD_BLOCK,"&aAbility 3")};
        for(int i=0;i<abilities.size()&&i<3;i++)
            ability[i]=new Item(abilities.get(i).getItem(),abilities.get(i).getName());

        shop.setItem(12, ability[0].spigot());
        shop.setItem(13, ability[1].spigot());
        shop.setItem(14, ability[2].spigot());
        return shop;
    }


    public static boolean doCharge(Player player, Material mat, int amount) {

        if (player.getInventory().contains(mat, amount)) {
            ItemStack payload = new ItemStack(mat);
            for (int i = 0; i < amount; i++) {
                player.getInventory().removeItem(payload);
            }
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            return true;
        } else {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return false;
        }
    }
}
