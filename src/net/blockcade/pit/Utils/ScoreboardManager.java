package net.blockcade.pit.Utils;


import net.blockcade.Arcade.Game;
import net.blockcade.Arcade.Main;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.Arcade.Varables.GameModule;
import net.blockcade.Arcade.Varables.TeamColors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScoreboardManager {
    private org.bukkit.scoreboard.ScoreboardManager manager;
    private String name;
    private Scoreboard board;
    private Objective objective;
    private HashMap<Integer, String> lines = new HashMap();
    public int longest_line = 0;
    private int counter = 32;
    private String payload = " ";
    private int payload_count = 1;
    private PITPlayer gamePlayer;
    private Game game;

    public ScoreboardManager(String name, Game game, PITPlayer pitPlayer) {
        this.name = name;
        this.manager = Bukkit.getServer().getScoreboardManager();
        this.board = this.manager.getNewScoreboard();
        this.objective = this.board.registerNewObjective(Text.format(name), "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.gamePlayer=pitPlayer;
        this.game = game;

        new BukkitRunnable(){
            @Override
            public void run() {
                if(pitPlayer.getPlayer().isOnline()){ update(); }else { delete();cancel(); }
            }
        }.runTaskTimerAsynchronously(game.handler(),0L,15L);
    }

    public void setDisplayname(String name) {
        this.objective.setDisplayName(Text.format(name));
        this.name = name;
    }

    public int addLine(String message) {
        if (message.length() > this.longest_line) {
            ++this.longest_line;
        }
        String message_raw = message;
        if(message.length()>=35){
            message=message.substring(0,35);
        }
        this.objective.getScore(Text.format(message)).setScore(this.counter);
        --this.counter;
        this.lines.put(this.counter, Text.format(message_raw));
        this.update();
        return this.counter;
    }

    public int addBlank() {
        String message = "";

        for (int i = this.payload_count; i > 0; --i) {
            message = message + this.payload;
        }

        this.addLine(message.replaceAll(":player_count:", Bukkit.getServer().getOnlinePlayers().size() + ""));
        this.lines.put(this.counter, message);
        ++this.payload_count;
        this.update();
        return this.counter;
    }

    public void editLine(int line, String message) {
        this.lines.put(line, Text.format(message));
        this.update();
    }

    public void enableHealthCounter() {
        Objective obj = this.board.registerNewObjective("healthname", "health");
        obj.setDisplayName(Text.format("&c❤"));
        obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
        Iterator var2 = Bukkit.getOnlinePlayers().iterator();

        while (var2.hasNext()) {
            Player player = (Player) var2.next();
            obj.getScore(player).setScore((int) player.getHealth());
        }

    }

    public void clear() {
        this.lines.clear();
    }

    public void update() {
        this.objective.unregister();
        this.objective = this.board.registerNewObjective(Text.format(this.name), "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Iterator var1 = this.lines.entrySet().iterator();

        while (var1.hasNext()) {
            Map.Entry<Integer, String> line = (Map.Entry) var1.next();
            String text = line.getValue();
            if(game.hasModule(GameModule.TEAMS)) {
                Matcher m = Pattern.compile(":ELIMINATED_(.*):").matcher(text);
                if (m.find()) {
                    String group = m.group(1);
                    TeamColors team = TeamColors.valueOf(group.toUpperCase());
                    int teamSize = game.TeamManager().getTeamPlayers(team).size();
                    text = text.replaceAll(":ELIMINATED_(.*):", game.TeamManager().getCanRespawn(team) ? "&a&l✓" : teamSize >= 1 ? "&a" + teamSize : "&c&l✗");
                }
            }

            if(text.length()>=35){
                text=text.substring(0,35);
            }
            if(text.contains(":combat_tag_0:")) {
                if(gamePlayer.isCombat()) {
                    this.objective.getScore(Text.format("&c&lCombat")).setScore(line.getKey());
                    this.objective.getScore(Text.format("&f " + gamePlayer.getCombatTime() / 1000 + "s")).setScore(line.getKey());
                    this.objective.getScore(Text.format("&r&c&a")).setScore(line.getKey());
                }
                continue;
            }
            if(text.contains(":killstreak_0:")) {
                if(gamePlayer.getKillstreak()>=1) {
                    this.objective.getScore(Text.format("&a&lKillStreak")).setScore(line.getKey());
                    this.objective.getScore(Text.format("&f " + gamePlayer.getKillstreak())).setScore(line.getKey());
                    this.objective.getScore(Text.format("&k&b&r")).setScore(line.getKey());
                }
                continue;
            }
            if(text.contains(":bounty_0:")) {
                if(gamePlayer.getBounty()>=1) {
                    this.objective.getScore(Text.format("&6&lBounty")).setScore(line.getKey());
                    this.objective.getScore(Text.format("&f " + gamePlayer.getBounty())+"g").setScore(line.getKey());
                    this.objective.getScore(Text.format("&k&b&r")).setScore(line.getKey());
                }
                continue;
            }
            gamePlayer.getPlayer().setLevel(gamePlayer.getLevel());
            int percent = gamePlayer.getLevelObj().getReq_exp()/gamePlayer.getPlayer().getExpToLevel();
            //gamePlayer.getPlayer().setExp(gamePlayer.getPlayer().getExpToLevel());
            this.objective.getScore(Text.format(text)
                    .replaceAll(":player_count:", Bukkit.getServer().getOnlinePlayers().size() + "")
                    .replaceAll(":server_name:", Main.networking.serverName)
                    .replaceAll(":player_exp:", (int)(gamePlayer!=null?gamePlayer.getExperience():-1)+"")
                    .replaceAll(":player_level:", (gamePlayer!=null?gamePlayer.getLevel():-1)+"")
                    .replaceAll(":player_level_req:", (gamePlayer!=null? gamePlayer.getLevelObj().getReq_exp()-gamePlayer.getExperience():-1)+"")
                    .replaceAll(":player_kills:", (gamePlayer!=null?gamePlayer.getKills():-1)+"")
                    .replaceAll(":player_gems:", (int)(gamePlayer!=null?gamePlayer.getGems():-1)+"")
            ).setScore(line.getKey());
        }

    }

    public void delete() {
        try {this.objective.unregister();}catch (IllegalStateException e){e.printStackTrace();}
        this.clear();
    }

    public void showFor(Player player) {
        player.setScoreboard(this.board);
    }
}
