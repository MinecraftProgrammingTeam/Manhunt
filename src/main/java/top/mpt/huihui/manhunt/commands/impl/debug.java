package top.mpt.huihui.manhunt.commands.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.mpt.huihui.manhunt.commands.ICommand;

import top.mpt.huihui.manhunt.Manhunt;

public class debug extends ICommand {
    public debug(){
        super("debug", "", "");
    }
    public boolean onCommand(CommandSender sender, String[] args){
        Player player = (Player) sender;
//        player.getInventory().setItem(0, Manhunt.instance.Hunter_Compass);
        System.out.println(player.getInventory().getItem(0));
        System.out.println(Manhunt.instance.Hunter_Compass);
        player.getInventory().setItem(0, Manhunt.instance.Hunter_Compass);
        return true;
    }
    public String permission(){
        return "manhunt.any";
    }
}
