package com.crystalneko.ctrpg.command;

import com.crystalneko.ctlib.sql.sqlite;
import com.crystalneko.ctrpg.CtRPG;
import com.crystalneko.ctlib.chat.chatPrefix;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class job implements CommandExecutor, TabCompleter {
    private CtRPG plugin;
    private String[] columName = {"name","job","xp","loveXP"};
    public job(CtRPG plugin) {
        this.plugin = plugin;
        addSqliteColum();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        //加入职业
        if (args[0].equalsIgnoreCase("join")) {
            String input = args[1].toLowerCase();
            if (input.equals("citizen") || input.equals("emperor") || input.equals("guard") || input.equals("farmer") || input.equals("tarder") || input.equals("leader") || input.equals("king") || input.equals("胡桃的狗")) {
                createPlayer(input, player);
            } else {
                player.sendMessage("§c输入的职业有误！");
            }
        } else if (args[0].equalsIgnoreCase("leave")) {
            player.sendMessage("开发中");
        } else if (args[0].equalsIgnoreCase("help")) {
            player.sendMessage("§b/job帮助\n§a/job help §b获取帮助\n§a/job join <职业> §b加入职业");
        } else {
            player.sendMessage("§c无效的子命令,输入§a/job help§c获取帮助");
        }
        return true;
    }
    private void createPlayer(String job,Player player){
        if(!sqlite.checkValueExists("job","name",player.getName())) {
            //初始装备
            initialItems(player,job);
            //添加前缀
            chatPrefix.addPrivatePrefix(player,"§a" + job);
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
            player.sendMessage("§a恭喜你成功加入职业!");
            if(job.equals("胡桃的狗")){ //我是胡桃的狗!!!
            Bukkit.getServer().broadcastMessage(player.getName() + "大声地说：§e我是胡桃的狗!!!");
            }

        } else {
            player.sendMessage("§c你已经加入过职业了!");
        }

    }
    private void addSqliteColum(){
        int i =0;
        while (i <= 3) {
            sqlite.addColumn("job", columName[i]);
            i ++;
        }
    }
    //初始装备
    private void initialItems(Player player,String job){
        if(job.equals("citizen")){
            // 获取要给予的物品类型
            Material itemType = Material.BREAD;
            // 创建物品堆栈
            ItemStack itemStack = new ItemStack(itemType, 5);
            // 给予玩家物品
            player.getInventory().addItem(itemStack);
        } else if(job.equals("emperor")){
            Material itemType = Material.DEBUG_STICK;
            ItemStack itemStack = new ItemStack(itemType, 1);
            player.getInventory().addItem(itemStack);
        } else if(job.equals("guard")){
            Material itemType = Material.BREAD;
            ItemStack itemStack = new ItemStack(itemType, 5);
            player.getInventory().addItem(itemStack);
            Material itemType1 = Material.GOLD_INGOT;
            ItemStack itemStack1 = new ItemStack(itemType1, 27);
            player.getInventory().addItem(itemStack1);
            Material itemType2 = Material.COMPASS;
            ItemStack itemStack2 = new ItemStack(itemType2, 1);
            player.getInventory().addItem(itemStack2);
        } else if(job.equals("farmer")){
            Material itemType = Material.IRON_HOE;
            ItemStack itemStack = new ItemStack(itemType, 1);
            player.getInventory().addItem(itemStack);
            Material itemType1 = Material.WHEAT_SEEDS;
            ItemStack itemStack1 = new ItemStack(itemType1, 32);
            player.getInventory().addItem(itemStack1);
            Material itemType2 = Material.BREAD;
            ItemStack itemStack2 = new ItemStack(itemType2, 5);
            player.getInventory().addItem(itemStack2);
        } else if (job.equals("leader")) {
            Material itemType = Material.WRITTEN_BOOK;
            ItemStack itemStack = new ItemStack(itemType, 1);
            player.getInventory().addItem(itemStack);
            Material itemType2 = Material.BREAD;
            ItemStack itemStack2 = new ItemStack(itemType2, 5);
            player.getInventory().addItem(itemStack2);
        } else if (job.equals("tarder")) {
            Material itemType2 = Material.BREAD;
            ItemStack itemStack2 = new ItemStack(itemType2, 5);
            player.getInventory().addItem(itemStack2);
            Material itemType = Material.EMERALD;
            ItemStack itemStack = new ItemStack(itemType, 10);
            player.getInventory().addItem(itemStack);
        } else if (job.equals("king")) {
            Material itemType = Material.STICK;
            ItemStack itemStack = new ItemStack(itemType, 1);
            player.getInventory().addItem(itemStack);
        }
    }
    //tab补全
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            completions.add("help");
            completions.add("join");
            return completions;
        }
        if(args.length == 2 && args[1].equals("join")){
            List<String> completions = new ArrayList<>();
            completions.add("citizen");
            completions.add("guard");
            completions.add("farmer");
            completions.add("leader");
            completions.add("tarder");
            return completions;
        }
        return Collections.emptyList();
    }
}
