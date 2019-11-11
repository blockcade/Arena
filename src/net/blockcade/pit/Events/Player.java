package net.blockcade.pit.Events;

import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Managers.EventManager.PlayerCombatLogEvent;
import net.blockcade.Arcade.Managers.EventManager.PlayerDeathEvent;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Utils.GameUtils.NPC;
import net.blockcade.Arcade.Utils.JavaUtils;
import net.blockcade.Arcade.Varables.GameState;
import net.blockcade.pit.Enums.Levels;
import net.blockcade.pit.Events.PITEvents.PlayerBountyEvent;
import net.blockcade.pit.Main;
import net.blockcade.pit.Utils.PITPlayer;
import net.blockcade.pit.Utils.ScoreboardManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.Objects;
import java.util.Random;

import static org.bukkit.Material.AIR;

public class Player implements Listener {

    Game game;
    public Player(Game game){this.game=game;}

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event){
        event.setJoinMessage(null);
        PITPlayer player = new PITPlayer(event.getPlayer());
        player.setLevel(0);

        if(!player.getInventory().contains(Material.IRON_SWORD))player.getInventory().addItem(new ItemStack(Material.IRON_SWORD));
        if(!player.getInventory().contains(Material.BOW))player.getInventory().addItem(new ItemStack(Material.BOW));
        if(!player.getInventory().containsAtLeast(new ItemStack(Material.ARROW),24))player.getInventory().setItem(9,new ItemStack(Material.ARROW,24));

        // Add required armor
        if(player.getPlayer().getInventory().getHelmet()==null){player.getPlayer().getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));}
        if(player.getPlayer().getInventory().getChestplate()==null){player.getPlayer().getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));}
        if(player.getPlayer().getInventory().getLeggings()==null){player.getPlayer().getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));}
        if(player.getPlayer().getInventory().getBoots()==null){player.getPlayer().getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));}

        player.sendMessage(Text.format("&c&m&l============================="));
        player.sendMessage(Text.format(JavaUtils.center("&e&lArena", 42 + (4))));
        player.sendMessage(Text.format(JavaUtils.center("&fThe world has gone to chaos, it's every", 41 + (4))));
        player.sendMessage(Text.format(JavaUtils.center("&fman, woman and child for themselves!! you", 41 + (4))));
        player.sendMessage(Text.format(JavaUtils.center("&fmust aquire gems from other players and get", 41 + (4))));
        player.sendMessage(Text.format(JavaUtils.center("&fupgrades from the traders. Good luck!", 41 + (4))));
        player.sendMessage(Text.format("&c&m&l============================="));

        event.getPlayer().teleport(Main.spawn_locations.get(new Random().nextInt(Main.spawn_locations.size())));

        ScoreboardManager scoreboard = new ScoreboardManager(event.getPlayer().getName(),game,player);
        scoreboard.enableHealthCounter();
        scoreboard.setDisplayname("&c&lArena");
        scoreboard.addLine("&7:server_name: &0/ &8"+ game.title());
        scoreboard.addBlank();
        scoreboard.addLine("&e&lGems");
        scoreboard.addLine(" &f:player_gems:");
        scoreboard.addBlank();
        scoreboard.addLine("&d&lKills");
        scoreboard.addLine(" &f:player_kills:");
        scoreboard.addBlank();
        scoreboard.addLine(":killstreak_0:");
        scoreboard.addLine("&3&lLevel");
        scoreboard.addLine(" &f:player_level:");
        scoreboard.addLine("&7&oNeed :player_level_req: exp");
        scoreboard.addBlank();
        scoreboard.addLine(":combat_tag_0:");
        scoreboard.addLine(":bounty_0:");
        scoreboard.addLine("&cblockcade.net");

        scoreboard.showFor(player.getPlayer());
        player.setScoreboardManager(scoreboard);

        for(NPC npc : Main.npcs){
            try {
                npc.showFor(player.getPlayer());
            }catch (NPC.EntityNotBuiltException e){
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void CombatLog(PlayerCombatLogEvent e) {
        e.getKiller().sendActionBar("&c"+e.getPlayer().getPlayer().getDisplayName()+"&c has combat logged.");
    }

    @EventHandler
    public void PlayerLeave(PlayerQuitEvent e){
        PITPlayer.players.remove(e.getPlayer());
        e.setQuitMessage(null);
    }

    @EventHandler
    public void Interact(PlayerInteractEvent event){
        if(ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName()).equalsIgnoreCase("cupcakes")){
            event.getItem().setAmount(event.getItem().getAmount()-1);
            org.bukkit.entity.Player player = (org.bukkit.entity.Player) event.getPlayer();
            player.playSound(player.getLocation(),Sound.ENTITY_PLAYER_BURP,1L,1L);
            // Add gold hearts
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,160,1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,160,1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,80,1));
        }
    }

    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent event){
        Location location = event.getPlayer().getLocation().clone();
        PITPlayer player = PITPlayer.getPlayer(event.getPlayer());
        event.getPlayer().setHealth(20);
        event.getPlayer().teleport(Main.spawn_locations.get(new Random().nextInt(Main.spawn_locations.size())));
        event.getPlayer().getInventory().clear();
        event.getPlayer().getInventory().addItem(new ItemStack(Material.IRON_SWORD));
        event.getPlayer().getInventory().addItem(new ItemStack(Material.BOW));
        event.getPlayer().getInventory().setItem(9,new ItemStack(Material.ARROW,24));

        ItemStack is = new ItemStack(Material.EMERALD);
        ItemMeta ism = is.getItemMeta();
        double amount;
        if(event.getKiller()!=null&&event.getKiller() instanceof org.bukkit.entity.Player){
            PITPlayer killer = PITPlayer.getPlayer((org.bukkit.entity.Player) event.getKiller());
            killer.sendMessage(Text.format("&c&lKILL &eYou have killed "+event.getPlayer().getDisplayName()));
            killer.addExp(25);
            event.getPlayer().sendMessage(Text.format("&4&lDEATH &eYou have died to "+killer.getPlayer().getDisplayName()));
            if(!killer.getInventory().containsAtLeast(new ItemStack(Material.GOLDEN_APPLE),2)){killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));}
            killer.getScoreboardManager().update();

            // Kill streaks
            if(killer.getKillstreak()>=5){
                killer.setBounty(killer.getKillstreak()*3);
                Bukkit.getPluginManager().callEvent(new PlayerBountyEvent(killer, PlayerBountyEvent.BountyReason.KILLSTREAK));
            }

            if(player.getBounty()>=1) {
                killer.sendMessage(String.format("&a&lBOUNTY GEMS &eYou have claimed the &a%sg&e bounty on %s", player.getBounty(), player.getPlayer().getName()));
                player.setBounty(0);
                player.getScoreboardManager().update();
                killer.getScoreboardManager().update();
                player.getPlayer().setPlayerListName(Text.format(player.getPlayer().getDisplayName()));
                killer.setGems(killer.getGems() + player.getBounty());
            }

            switch(((org.bukkit.entity.Player)event.getKiller()).getItemInHand().getType()){
                case DIAMOND_SWORD:
                case DIAMOND_AXE:
                    amount=3.5;
                    break;
                case IRON_SWORD:
                case IRON_AXE:
                    amount=2.5;
                    break;
                case WOODEN_SWORD:
                    amount=69.99;
                    break;
                default:
                    amount=5;
                    break;
            }
        }else { amount=1; }
        ism.setLore(Collections.singletonList("VALUE|"+amount));
        is.setItemMeta(ism);
        location.getWorld().dropItem(location,is);
    }

    @EventHandler
    public void PlayerPickupItem(EntityPickupItemEvent e){
        if(e.getEntity() instanceof org.bukkit.entity.Player) {
            if(ChatColor.stripColor(e.getItem().getItemStack().getItemMeta().getDisplayName()).equalsIgnoreCase("cupcakes")){
                if(((org.bukkit.entity.Player) e.getEntity()).getInventory().containsAtLeast(new ItemStack(Material.LEGACY_SKULL),2)){
                    e.setCancelled(true);
                }
                if(e.getItem().getItemStack().getAmount()>=2){
                    e.getItem().getItemStack().setAmount(e.getItem().getItemStack().getAmount()-2);
                    ItemStack is = e.getItem().getItemStack().clone();
                    is.setAmount(2);
                    ((org.bukkit.entity.Player) e.getEntity()).getInventory().addItem(is);
                }
            }
            if (e.getItem().getItemStack().getType().equals(Material.EMERALD) && Objects.requireNonNull(e.getItem().getItemStack().getItemMeta()).getLore().get(0).contains("VALUE|")) {
                double amount = Double.parseDouble(Objects.requireNonNull(e.getItem().getItemStack().getItemMeta()).getLore().get(0).split("[|]")[1]);
                if (amount <= 0) {
                    e.setCancelled(true);
                    return;
                }
                e.getItem().getItemStack().setAmount(0);
                e.setCancelled(true);
                PITPlayer p = PITPlayer.getPlayer((org.bukkit.entity.Player) e.getEntity());
                if(p!=null){
                    p.sendActionBar(String.format("&a+%s Gems",amount));
                    p.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP,0.5f,2);
                    p.setGems(p.getGems()+amount);
                    p.getScoreboardManager().update();
                    p.sendMessage(String.format("&a&lGEMS &eYou have picked up &a%s&e gems!",amount));
                }else {
                    System.out.println("Failed to get PITPlayer");
                }
            }
        }
    }

    @EventHandler
    public void PlayerBounty(PlayerBountyEvent event){
        String message = String.format("&eBounty applied on &6%s&e for &6%s&e of &a%s&e!",event.getPlayer().getPlayer().getName(),event.getReason().name(),event.getPlayer().getBounty());
        event.getPlayer().getPlayer().setPlayerListName(Text.format(event.getPlayer().getPlayer().getDisplayName()+" &a&l"+event.getBounty()+"g&r"));
        Text.sendAll(message, Text.MessageType.ACTION_BAR);
        event.getPlayer().getScoreboardManager().update();
        Text.sendAll("&6&lBOUNTY "+message, Text.MessageType.TEXT_CHAT);
    }

    @EventHandler
    public void ChatEvent(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        if (game.GameState() == GameState.IN_GAME) {
            PITPlayer player = PITPlayer.getPlayer(e.getPlayer());
            Levels.level level = player.getLevelObj();
            Bukkit.broadcastMessage(Text.format(String.format("&7[%s&7] &e%s&7: %s", "&"+level.getChatcolor()+level.getLevel(),e.getPlayer().getName(), e.getMessage())));
        }
    }

    @EventHandler
    public void BlockPlaceEvent(PlayerBucketEmptyEvent event){
        if(event.getBucket().equals(Material.LAVA_BUCKET)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Block b = event.getBlockClicked().getLocation().getBlock().getRelative(event.getBlockFace());
                    b.setType(AIR);
                    b.getWorld().spawnParticle(Particle.BLOCK_CRACK,b.getLocation(),2);
                }
            }.runTaskLater(game.handler(), 150L);
        }
    }

    @EventHandler
    public void LiquidFlow(BlockFromToEvent e){
        if(e.getBlock().getType().equals(Material.LAVA)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void FishingEvent(PlayerFishEvent e){
        PITPlayer player = PITPlayer.getPlayer(e.getPlayer());
        if(e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)||e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            Objects.requireNonNull(e.getCaught()).remove();
            e.setExpToDrop(0);
            player.playSound(Sound.ENTITY_FISHING_BOBBER_RETRIEVE, 1, 1);
            player.sendMessage("&a&lGEMS &eYou have fished 60 gems!");
            player.setGems(player.getGems() + 60);
        }
    }

}
