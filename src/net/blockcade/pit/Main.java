package net.blockcade.pit;

import net.blockcade.Arcade.Commands.GameCommand;
import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Managers.GamePlayer;
import net.blockcade.Arcade.Utils.GameUtils.NPC;
import net.blockcade.Arcade.Utils.JavaUtils;
import net.blockcade.Arcade.Varables.GameModule;
import net.blockcade.Arcade.Varables.GameName;
import net.blockcade.Arcade.Varables.GameState;
import net.blockcade.Arcade.Varables.GameType;
import net.blockcade.pit.Commands.spawn;
import net.blockcade.pit.Events.Player;
import net.blockcade.pit.InventoryMenus.Shop;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Main extends JavaPlugin {

    public static ArrayList<Location> spawn_locations = new ArrayList<>();
    public static ArrayList<NPC> npcs = new ArrayList<>();

    @Override
    public void onEnable() {
        /*
         * Init Game
         */
        Game game = new Game("Arena", GameType.CUSTOM,1,100,this, Bukkit.getWorlds().get(0));
        game.GameState(GameState.DISABLED);
        game.AutoStart(false);
        game.setGameName(GameName.ARENA);
        game.setMaxDamageHeight(130);
        game.setModule(GameModule.DEATH_MANAGER,false);
        game.setModule(GameModule.BLOCK_PLACEMENT,true);
        game.setModule(GameModule.VOID_DEATH,true);
        game.setModule(GameModule.NO_CRAFTING,true);
        game.setModule(GameModule.BLOCK_PLACEMENT,true);
        game.setModule(GameModule.ALLSTATE_JOIN,true);
        game.setModule(GameModule.NO_FALL_DAMAGE,true);
        game.setModule(GameModule.CHAT_MANAGER,false);
        game.map().setGameRule(GameRule.DO_WEATHER_CYCLE,false);
        game.map().setGameRule(GameRule.DO_DAYLIGHT_CYCLE,false);

        game.map().setStorm(false);
        game.map().setWeatherDuration(0);
        game.map().setTime(6316);
        game.map().setFullTime(1700464);
        getServer().getPluginManager().registerEvents(new GamePlayer(game,false), game.handler());

        new Shop(game);

        /*
         * Register Events
         */
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new Player(game),this);


        new BukkitRunnable() {
            @Override
            public void run() {
                game.GameState(GameState.IN_GAME);
                for(org.bukkit.entity.Player p : Bukkit.getOnlinePlayers())
                    Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(p,null));
            }
        }.runTaskLater(this, 60L);

        getCommand("game").setExecutor(new GameCommand(this, game));
        getCommand("spawn").setExecutor(new spawn());

        // Get spawn Locations
        String position = String.format("maps.%s.SPAWN",game.map().getName().toLowerCase());
        String[] locations = Main.getPlugin(net.blockcade.Arcade.Main.class).getConfig().getStringList(position).toArray(new String[]{});
        for(String raw_location : locations)
            spawn_locations.add(JavaUtils.parseConfigLocation(raw_location));

        System.out.println(String.format("Loaded %s spawn points [%s, %s]",spawn_locations.size(),position,locations.length));
    }

}
