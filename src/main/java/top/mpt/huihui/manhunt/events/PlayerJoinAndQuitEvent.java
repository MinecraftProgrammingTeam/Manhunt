package top.mpt.huihui.manhunt.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import top.mpt.huihui.manhunt.utils.ChatUtils;

import static top.mpt.huihui.manhunt.Manhunt.Online_Players;
import static top.mpt.huihui.manhunt.Manhunt.instance;

public class PlayerJoinAndQuitEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        String PlayerName = event.getPlayer().getName();
        if (!Online_Players.contains(PlayerName)){
            Online_Players.add(PlayerName);
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        Online_Players.remove(player.getName());
        if (instance.hunters.contains(player.getUniqueId())){
            ChatUtils.broadcast("#RED#猎人: %s 逃逸了", player.getName());
            instance.hunters.remove(player.getUniqueId());
        }
    }
}
