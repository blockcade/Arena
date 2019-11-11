package net.blockcade.pit.Enums;

import org.bukkit.Material;

public enum Ability {
    CUPCAKE_HEAD(Material.PLAYER_HEAD, "&dCupcakes", ""),
    FISHING_ROD(Material.FISHING_ROD,"&dFishermen",""),
    LAVA_BUCKET(Material.LAVA_BUCKET,"&dLava Bucket",""),
    DOUBLE_SHOT(Material.ARROW,"&dDouble Shot",""),
    TNT_DEATH(Material.TNT,"&dTNT On Death",""),
    SUMMONING(Material.RED_WOOL,"&dSummoning",""),
    // ;)
    SPEED_BOOST(Material.SUGAR,"&dSped Boost",""),
    ARSONIST(Material.FLINT_AND_STEEL,"&dArsonist",""),
    MINER(Material.GOLDEN_PICKAXE,"&dMiner",""),
    RAND_PROT(Material.ENCHANTED_BOOK,"&dRandom Protection", "&7Protection I"),
    DIAMOND_DROP(Material.DIAMOND_BOOTS,"&dDiamond Drop",""),
    SOUL_LEECHER(Material.NETHER_WART,"&dSoul Leecher",""),
    MAGIC_JUICE(Material.POTION,"&dMatic Jews",""),
    WOLF_GUARD(Material.WOLF_SPAWN_EGG,"&dWolf Guard",""),
    REVIVAL(Material.NETHER_STAR,"&dRevival","")
    ;
    Material item;
    String name, lore;
    Ability(Material item, String name, String lore){
        this.item=item;
        this.name=name;
        this.lore=lore;
    }

    public String getName() {
        return name;
    }

    public Material getItem() {
        return item;
    }

    public String getLore() {
        return lore;
    }
}
