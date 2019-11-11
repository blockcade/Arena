package net.blockcade.pit.Utils;

import net.blockcade.Arcade.Utils.Formatting.Item;
import net.blockcade.Arcade.Utils.Formatting.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryMenu {

    private String title;
    private int lines;
    private Inventory inventory;

    public InventoryMenu(String title, int lines){
        this.title=title;
        this.lines=lines;
        inventory=Bukkit.createInventory(null,(lines*9), Text.format(title));
    }

    public InventoryMenu setItem(Item item, int x, int y){
        inventory.setItem(x - 1 + (y - 1) * 9, item.spigot());
        return this;
    }

    public void showFor(Player player){
        player.openInventory(this.inventory);
    }

    public Inventory spigot() {
        return inventory;
    }
}
