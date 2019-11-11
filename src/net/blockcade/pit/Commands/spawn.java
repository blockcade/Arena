package net.blockcade.pit.Commands;

import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.pit.Main;
import net.blockcade.pit.Utils.PITPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class spawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        PITPlayer player = PITPlayer.getPlayer((Player)sender);
        if(player.isCombat()){
            player.sendMessage(Text.format("&c&lCOMBAT &7You are in combat for &e%s&7 more seconds",(player.getCombatTime()/1000)+""));
        }else {
            player.teleport(Main.spawn_locations.get(new Random().nextInt(Main.spawn_locations.size())));
        }
        return false;
    }
}
