package top.mpt.huihui.manhunt.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import top.mpt.huihui.manhunt.utils.PlayerUtils;

import static top.mpt.huihui.manhunt.Manhunt.instance;
public class CompassExecutor implements Listener {
    // 如果猎人扔掉指南针
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        Player player = event.getPlayer();
        if (instance.hunters.contains(player.getUniqueId())){
            if (event.getItemDrop().getItemStack().getType().equals(Material.COMPASS)){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (instance.hunters.contains(player.getUniqueId()) && event.hasItem() && event.getItem().getType() == Material.COMPASS && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
            Player nearest = null;
            double distance = Double.MAX_VALUE;

            // 获取最近的玩家
            for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.equals(player) && onlinePlayer.getWorld().equals(player.getWorld()) && !instance.hunters.contains(onlinePlayer.getUniqueId())) {
                    double distanceSquared = onlinePlayer.getLocation().distanceSquared(player.getLocation());
                    if (distanceSquared < distance) {
                        distance = distanceSquared;
                        nearest = onlinePlayer;
                    }
                }
            }

            if (nearest == null) {
                if(instance.door != null){
                    player.setCompassTarget(instance.door);
                    ItemMeta itemMeta = instance.Hunter_Compass.getItemMeta();
                    CompassMeta compassMeta = (CompassMeta) itemMeta;
                    compassMeta.setLodestoneTracked(true);
                    compassMeta.setLodestone(instance.door);
                    instance.Hunter_Compass.setItemMeta(compassMeta);
                    player.getInventory().remove(Material.COMPASS);
                    player.getInventory().addItem(instance.Hunter_Compass);
                    PlayerUtils.send(player, "#GREEN#指针正在指向传送门");
                } else {
                    PlayerUtils.send(event.getPlayer(), "#AQUA#没有可以追踪的玩家");
                }
                return;
            }

            player.setCompassTarget(nearest.getLocation());
            ItemMeta itemMeta = instance.Hunter_Compass.getItemMeta();
            CompassMeta compassMeta = (CompassMeta) itemMeta;
            compassMeta.setLodestoneTracked(true);
            compassMeta.setLodestone(nearest.getLocation());
            instance.Hunter_Compass.setItemMeta(compassMeta);
            player.getInventory().remove(Material.COMPASS);
            player.getInventory().addItem(instance.Hunter_Compass);
            PlayerUtils.send(player, "#GREEN#指针正在指向: %s", nearest.getName());
        }

    }


    // 玩家重生
    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (instance.hunters.contains(player.getUniqueId())) {
            player.getInventory().addItem(instance.Hunter_Compass);
        }
    }

    // 玩家切换世界
    @EventHandler
    public void onPlayerChangeWorld(PlayerPortalEvent event){
        Player player = event.getPlayer();
        if (!instance.hunters.contains(player.getUniqueId())){
            instance.door = player.getLocation();
        }
    }

}
