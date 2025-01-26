package top.mpt.huihui.manhunt.timer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import top.mpt.huihui.manhunt.Manhunt;

import java.util.UUID;

public class timer extends BukkitRunnable {
    @Override
    public void run(){
        for (UUID uuid:Manhunt.instance.hunters) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.getInventory().setItem(0, Manhunt.instance.Hunter_Compass);
            }
        }
    }
}
