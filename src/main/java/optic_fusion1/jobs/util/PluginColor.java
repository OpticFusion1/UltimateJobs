package optic_fusion1.jobs.util;

public enum PluginColor {

    INFO("§a[INFO] ", "§a"),
    JOB_RELATED_INFO("§b§l[JOBINFO] §b", "§b"),
    JOB_RELATED_WARNING("§6§l[JOBWARNING] §6", "§6"),
    JOB_RELATED_ERROR("§4§l[JOBERROR] §4", "§4"),
    JOB_LOADED("§a§l[JOBLOADED] §a", "§a"),
    GUI_RELATED_INFO("§e&l[GUIINFO] §e", "§e"),
    GUI_RELATED_WARNING("§6§l[GUIWARNING] §6", "§6"),
    GUI_RELATED_ERROR("§4§l[GUIERROR] §4", "§4"),
    LANG_RELATED_INFO("§d§l[LANGINFO] §d", "§d"),
    LANG_RELATED_WARNING("§6§l[LANGWARNING] §6", "§6"),
    LANG_RELATED_ERROR("§4§l[LANGERROR] §4", "§4"),
    LANG_LOADED("§d§l[LANGLOADED] §d", "§d"),
    ITEM_RELATED_INFO("§3§l[ITEMINFO] §3", "§3"),
    ITEM_RELATED_WARNING("§6§l[ITEMWARNING] §6", "§6"),
    ITEM_RELATED_ERROR("§4§l[ITEMERROR] §4", "§4"),
    ITEM_LOADED("§3§l[ITEMLOADED] §3", "§3"),
    WARNING("§6[WARNING] ", "§6"),
    ERROR("§c[ERROR] ", "§c"),
    FAILED("§4§l[FAILED] ", "§4§l");

    public String color;
    public String prefix;

    PluginColor(String prefix, String color) {
        this.color = color;
        this.prefix = prefix;
    }

    public String getColor() {
        return this.color;
    }

    public String getPrefix() {
        return this.prefix;
    }

}
