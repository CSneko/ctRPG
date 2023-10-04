package com.crystalneko.ctrpg.event;


import com.crystalneko.ctlib.chat.chatPrefix;
import com.crystalneko.ctlib.sql.sqlite;
import com.crystalneko.ctrpg.CtRPG;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.Bukkit.getServer;

public class playerJoin implements Listener {
    private CtRPG plugin;

    public playerJoin(CtRPG plugin) {
        this.plugin = plugin;
        getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        //读取玩家职业
        String job = sqlite.getColumnValue("job","job","name",player.getName());
        if(job != null) {
            //写入前缀
            chatPrefix.addPrivatePrefix(player, "§a" + job);
        }
        //以下代码写得太烂
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 999999, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 4, 99));
        Location location = player.getLocation();
        if(!sqlite.checkValueExists("job","name",player.getName())){
            location.setX(20);
            location.setY(114);
            location.setZ(-1094);
            player.sendMessage("§a请输入§b/job join <职业> §a加入职业");
        }
        player.teleport(location);
        showCenterMessage(player,  "§a欢迎来到中世纪服务器");


    }

    private void showCenterMessage(Player player, String message) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle("", message, 30, 200, 50);
            }
        }.runTaskLater(plugin, 20);
    }
}