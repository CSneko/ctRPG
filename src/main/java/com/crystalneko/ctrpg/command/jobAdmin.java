package com.crystalneko.ctrpg.command;

import com.crystalneko.ctlib.chat.chatPrefix;
import com.crystalneko.ctlib.sql.sqlite;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class jobAdmin implements CommandExecutor, TabCompleter {
    private String[] columName = {"name","job","xp","loveXP"};

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(player.hasPermission("ctrpg.job.command.admin")) {
            if (args[0].equalsIgnoreCase("join") && args.length == 3) {
                //强制加入职业
                if (!sqlite.checkValueExists("job", "name", player.getName())) {
                    String job = args[2];
                    //添加前缀
                    chatPrefix.addPrivatePrefix(player, "§a" + job);
                    //将玩家名写入数据库
                    if (!sqlite.checkValueExists("job", "name", player.getName())) {
                        sqlite.saveData("job", "name", player.getName());
                    }
                    //将基础信息写入数据库
                    String[] columnValue = {player.getName(), job, "0", "0"};
                    int i = 0;
                    while (i <= 3) {
                        sqlite.saveDataWhere("job", columName[i], "name", player.getName(), columnValue[i]);
                        i++;
                    }
                    player.sendMessage("§a成功将玩家加入职业!");
                    if (job.equals("胡桃的狗")) { //我是胡桃的狗!!!
                        Bukkit.getServer().broadcastMessage("玩家" + player.getName() + "强迫" + player.getName() + "大声地说：§e我是胡桃的狗!!!");
                    }
                } else {
                    player.sendMessage("§c玩家" + args[1] + "已经加入过职业了!");
                }

            } else if (args[0].equalsIgnoreCase("leave")) {
                //强制退出职业
                if (sqlite.checkValueExists("job", "name", player.getName())) {
                    String job = args[2];
                    //删除前缀
                    chatPrefix.subPrivatePrefix(player, "§a" + job);
                    //将数据从数据库删除
                    if(sqlite.deleteLine("job","name",args[1])){
                        player.sendMessage("成功删除玩家"+args[1] + "的职业信息");
                    } else {player.sendMessage("删除失败");}
                }
            } else if (args[0].equalsIgnoreCase("help")) {
                player.sendMessage("§b/joba帮助\n§a/joba help §b获取帮助\n§a/joba join <玩家> <职业> §b加入职业\n§a/joba left <玩家> <职业> §b退出职业");
            } else {
                player.sendMessage("§c无效的子命令,输入§a/jobadmin help§c获取帮助");
            }
        }else {player.sendMessage("§c你没有权限执行该命令");}
        return true;
    }


    //tab补全
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            completions.add("help");
            completions.add("join");
            completions.add("leave");
            return completions;
        }
        if(args.length == 2 && args[1].equals("join") | args[1].equals("left")){
            Collection<? extends Player> completions =(Bukkit.getOnlinePlayers());
            return convertPlayerCollectionToList(completions);
        }
        return Collections.emptyList();
    }

    public static List<String> convertPlayerCollectionToList(Collection<? extends Player> players) {
        List<String> playerNames = new ArrayList<>();
        for (Player player : players) {
            String playerName = player.getName();  // 假设Player类有一个getName()方法返回玩家名称
            playerNames.add(playerName);
        }
        return playerNames;
    }

}
