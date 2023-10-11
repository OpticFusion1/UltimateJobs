package optic_fusion1.jobs.item;

import java.util.List;
import org.bukkit.inventory.ItemStack;


public class Item {

    private String internal;

    private ItemStack icon;

    private String named;

    private ItemType type;

    private List<String> actions;

    public Item(String internal, String named, ItemType type, ItemStack icon, List<String> cfgactions) {

        this.internal = internal;
        this.icon = icon;
        this.actions = cfgactions;
        this.type = type;
        this.named = named;
    }

    public ItemType getType() {
        return type;
    }

    public String getNamed() {
        return this.named;
    }

    public List<String> getActions() {
        return this.actions;
    }

    public ItemStack getItemStack() {
        return icon;
    }

    public String getInternalID() {
        return this.internal;
    }

}
