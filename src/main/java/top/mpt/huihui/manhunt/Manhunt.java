package top.mpt.huihui.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import top.mpt.huihui.manhunt.events.CompassExecutor;
import top.mpt.huihui.manhunt.events.PlayerJoinAndQuitEvent;
import top.mpt.huihui.manhunt.executor.CommandHandler;
import top.mpt.huihui.manhunt.timer.timer;
import top.mpt.huihui.manhunt.utils.ItemUtils;

import java.util.*;

import static org.bukkit.ChatColor.*;

public final class Manhunt extends JavaPlugin {

    // 设置normal项（用于broadcast）
    public static String normal = BLUE + "[Manhunt] ";
    public static Manhunt instance;
    // 新建数组，存放玩家
    public static List<String> Online_Players = new ArrayList<>();
    // 猎人指针
    public ItemStack Hunter_Compass = ItemUtils.newItem(Material.COMPASS, "#RED#猎人指针");

    // 存放猎人
    public Set<UUID> hunters;
    // 存放速通者地狱门/末地门坐标
    public Location door;



    @Override
    public void onEnable() {
        instance = this;

        hunters = new HashSet<>();

        // config
//        getConfig().options().copyDefaults();
//        saveDefaultConfig();

        // 初始化online_players
        Bukkit.getOnlinePlayers().forEach(it -> Online_Players.add(it.getName()));

        // 指令
        Objects.requireNonNull(getCommand("manhunt")).setExecutor(new CommandHandler());

        // 注册事件
        getServer().getPluginManager().registerEvents(new PlayerJoinAndQuitEvent(), this);
        getServer().getPluginManager().registerEvents(new CompassExecutor(), this);
        getLogger().info(normal + AQUA + "Plugin Enabled");
        new timer().runTaskTimer(Manhunt.getPlugin(Manhunt.class), 10, 1);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
