package me.shawlaf.banmanager;

import dev.wolveringer.bungeeutil.AsyncCatcher;
import me.shawlaf.banmanager.managers.ErrorManager;
import me.shawlaf.banmanager.managers.config.ConfigurationManager;
import me.shawlaf.banmanager.util.chat.C;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

/**
 * Created by Florian on 29.12.2016.
 */
public class Banmanager extends Plugin {
    
    private boolean successfulStartup = false;
    
    private ConfigurationManager configurationManager;
    private ErrorManager errorManager;
    
    @Override
    public void onLoad() {
        if (! ensureBungeeUtil()) {
            getLogger().severe(C.RED + "-------------------------------------------------------------------------------------------------------------------------");
            getLogger().severe(C.RED + "	[Shawlaf's Banmanager] BUNGEEUTIL DEPENDENCY NOT FOUND!");
            getLogger().severe(C.RED + "   PLEASE DOWNLOAD BUNGEEUTIL AT https://www.spigotmc.org/resources/8699");
            getLogger().severe(C.RED + "-------------------------------------------------------------------------------------------------------------------------");
            
            getProxy().getScheduler().schedule(this, this::disable, 1L, TimeUnit.SECONDS);
            return;
        }
    
        AsyncCatcher.disable(this);
        
        this.configurationManager = new ConfigurationManager(this);
        this.errorManager = new ErrorManager(this);
        
        successfulStartup = true;
    }
    
    private boolean ensureBungeeUtil() {
        return getProxy().getPluginManager().getPlugin("BungeeUtil") != null;
    }
    
    private void disable() {
        getProxy().getLogger().info("Disabling plugin " + getDescription().getName() + " version " + getDescription().getVersion() + " by " + getDescription().getAuthor());
        
        try {
            onDisable();
            for (Handler handler : getLogger().getHandlers())
                handler.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        
        getProxy().getScheduler().cancel(Banmanager.this);
        getExecutorService().shutdownNow();
        getProxy().getLogger().info("Disabled plugin " + getDescription().getName() + " version " + getDescription().getVersion() + " by " + getDescription().getAuthor());
    }
}
