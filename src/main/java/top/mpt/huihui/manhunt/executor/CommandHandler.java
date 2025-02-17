package top.mpt.huihui.manhunt.executor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import top.mpt.huihui.manhunt.commands.ICommand;
import top.mpt.huihui.manhunt.commands.impl.*;
import top.mpt.huihui.manhunt.utils.PlayerUtils;
import top.mpt.huihui.manhunt.utils.i18N;

import java.util.*;

/**
 * 子指令处理器
 */
public class CommandHandler implements TabExecutor {

    /**
     * 维护的指令集合
     */
    private final Map<String, ICommand> commands = new HashMap<>();

    /**
     * handler初始化构造器
     */
    public CommandHandler() {
        initHandler();
    }

    /**
     * 初始化指令集
     * 注意要使用小写，与发送者的指令进行匹配
     */
    private void initHandler() {
        registerCommand(new hunter());
        registerCommand(new debug());
    }

    /**
     * 手动注册指令
     */
    public void registerCommand(ICommand command) {
        //command.setHandler(this);
        commands.put(command.getCmdName(), command);
    }

    /**
     * 使用帮助指令
     */
    public void showHelp(CommandSender sender) {
        PlayerUtils.send(sender, "#BLUE#manhunt  #GREEN#Helper");
        for (String key: commands.keySet()) {
            sender.sendMessage(commands.get(key).showUsage());
        }
    }

    /**
     * 统一返回true，使用自定义的showHelp()方法。
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args == null || args.length < 1) {
            showHelp(sender);
            return true;
        }

        //mc输入的文字区分大小写
        ICommand cmd = commands.get(args[0].toLowerCase());
        try {
            if (cmd != null && sender.hasPermission(cmd.permission())) {
                //指令参数
                String[] params = new String[0];
                if (args.length >= 2) {
                    //用链表的removeFirst，删掉第指令，得到参数
                    LinkedList<String> list = new LinkedList<>(Arrays.asList(args));
                    list.removeFirst();
                    params = list.toArray(new String[0]);
                }
                boolean res = cmd.onCommand(sender, params);
                if (!res) {
                    //使用 cmd 自身的说明，而非调用 showHelp()
                    sender.sendMessage(cmd.showUsage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            PlayerUtils.send(sender, "#RED#Got an Exception：%s", e.getMessage());
            return true;
        }
        return true;
    }

    /**
     * 玩家每输入一个字母都会被服务器响应
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args == null || args.length < 1) {
            showHelp(sender);
            return null;
        }

        List<String> result = new ArrayList<>();
        //正在输第一个指令，如 /sub god...
        if (args.length == 1) {
            String typingStr = args[0].toLowerCase();
            for (String cmdName : commands.keySet()) {
                /*
                  如果正在输入的字母是正确指令的前缀，且玩家拥有对应指令的权限，就将指令名称拼接到结果里去
                  注意：这里并不是检测到一个符合就立马返回，而是返回符合前缀的指令集合
                 */
                if (cmdName.startsWith(typingStr)) {
                    ICommand cmd = commands.get(cmdName);
                    if (sender.hasPermission(cmd.permission())) {
                        result.add(cmdName);
                    }
                }
            }
        } else if (args.length == 2){
            //得到第一个指令，查看对应参数
            ICommand cmd = commands.get(args[0].toLowerCase());
            //玩家可能会输错，找不到指令，那就不管了
            if (cmd != null) {
                return Arrays.asList("add", "del");
            }
        } else{
            //得到第一个指令，查看对应参数
            ICommand cmd = commands.get(args[0].toLowerCase());
            //玩家可能会输错，找不到指令，那就不管了
            if (cmd != null) {
                if (Objects.equals(cmd.getCmdName(), "hunter")){
                    return cmd.getListParams();
                }
            }
        }
        return result;
    }
}