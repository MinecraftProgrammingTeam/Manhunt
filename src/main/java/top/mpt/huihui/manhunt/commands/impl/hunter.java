package top.mpt.huihui.manhunt.commands.impl;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.mpt.huihui.manhunt.commands.ICommand;
import top.mpt.huihui.manhunt.utils.ChatUtils;
import top.mpt.huihui.manhunt.utils.PlayerUtils;

import static top.mpt.huihui.manhunt.Manhunt.Online_Players;
import static top.mpt.huihui.manhunt.Manhunt.instance;

public class hunter extends ICommand {
    public hunter(){
        super("hunter", "", "/manhunt hunter add/del [HunterName]");
        setListParams(Online_Players);
    }


    public boolean onCommand(CommandSender sender, String[] args){
        if (args.length != 2){
            PlayerUtils.send(sender, "#AQUA#请按照指令格式输入！");
            return true;
        }
        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            PlayerUtils.send(sender, "#RED#未找到玩家！");
            return false;
        }
        if (args[0].equalsIgnoreCase("add")){
            instance.hunters.add(player.getUniqueId());
            ChatUtils.broadcast("#AQUA#已添加玩家: #RED# %s #AQUA#为 #RED#猎人", sender.getName());
            player.getInventory().addItem(instance.Hunter_Compass);
        } else if (args[0].equalsIgnoreCase("del")){
            instance.hunters.remove(player.getUniqueId());
            ChatUtils.broadcast("#AQUA#已移除玩家: #RED# %s #AQUA#的#RED#猎人#AQUA#身份", sender.getName());
            player.getInventory().remove(instance.Hunter_Compass);
        }


        return true;
    }

    public String permission(){
        return "manhunt.any";
    }
}
