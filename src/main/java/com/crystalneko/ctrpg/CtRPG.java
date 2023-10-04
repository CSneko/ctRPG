package com.crystalneko.ctrpg;

import com.crystalneko.ctrpg.command.job;
import com.crystalneko.ctrpg.command.jobAdmin;
import org.bukkit.plugin.java.JavaPlugin;

public final class CtRPG extends JavaPlugin {
    private job job;

    @Override
    public void onEnable() {
        //聊天前缀就不写了，因为反正要加toNeko,toNeko已经把聊天前缀都弄好了，这里负责加上前缀就行了
        // ---------------------------------------------------------初始化-------------------------------------------------------------
        this.job = new job(this);
        // ------------------------------------------------------注册命令执行器---------------------------------------------------------
        getCommand("job").setExecutor(job);
        getCommand("jobadmin").setExecutor(new jobAdmin());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
