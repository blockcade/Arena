package net.blockcade.pit.Utils;

import net.blockcade.Arcade.Managers.GamePlayer;
import net.blockcade.Arcade.Utils.Formatting.Text;
import net.blockcade.pit.Enums.Ability;
import net.blockcade.pit.Enums.Levels;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PITPlayer extends GamePlayer {

    public static HashMap<Player, GamePlayer>  players = new HashMap<>();

    public static ArrayList<Ability> abilities=new ArrayList<>();
    public static HashMap<Ability, Integer> upgrade_level = new HashMap<>();

    private ScoreboardManager sbm;

    private double bounty;

    private Levels.level level=Levels.getLevel(1, (int) getExperience());

    private double gems=0;

    public PITPlayer(Player player) {
        super(player);
        abilities.add(Ability.CUPCAKE_HEAD);
        players.put(player,this);
    }

    public Levels.level getLevelObj() {
        return level;
    }

    @Override
    public void addExp(int experience){
        super.addExp(experience);
        int s_level = getLevel();
        Levels.level s_level_obj = getLevelObj();
        while (doLevelCheck()){
            sendMessage("&d&lLEVEL &eYou have leveled up to "+getLevel());
        }
        if(s_level<getLevel()) {
            sendTitle("&aLEVEL UP!");
            getPlayer().sendTitle(Text.format("&a&lLEVEL UP!"), Text.format("&f%s &7to &f%s", "&"+s_level_obj.getChatcolor()+s_level + "", "&"+getLevelObj().getChatcolor()+getLevel() + ""));
            playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }
    }

    private boolean doLevelCheck(){
        if(getLevelObj().getReq_exp()<=getExperience()){
            setLevel(getLevel()+1);
            level=Levels.getLevel(getLevel(), (int) getExperience());
            return true;
        }
        return false;
    }

    public double getGems() {
        return gems;
    }

    public void setGems(double gems) {
        this.gems = gems;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }
    public Ability getAbility(int i){
        return abilities.get(i);
    }
    public boolean hasAbility(Ability ability) {
        return abilities.contains(ability);
    }

    public void setScoreboardManager(ScoreboardManager sbm) {
        this.sbm = sbm;
    }

    public double getBounty() {
        return bounty;
    }

    public void setBounty(double bounty) {
        this.bounty = bounty;
    }

    public ScoreboardManager getScoreboardManager() {
        return sbm;
    }

    public static PITPlayer getPlayer(Player player){
        return players.containsKey(player)?(PITPlayer) players.get(player):new PITPlayer(player);
    }
}
