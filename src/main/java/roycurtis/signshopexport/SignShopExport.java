package roycurtis.signshopexport;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/** Core class of the SignShopExport plugin. Handles manager creation and reload. */
public class SignShopExport extends JavaPlugin
{
    static SignShopExport PLUGIN;
    static Logger         LOGGER;
    static Server         SERVER;
    static Config         CONFIG;
    static DataManager    DATAMANAGER;

    @Override
    public void onLoad()
    {
        PLUGIN = this;
        LOGGER = getLogger();
        SERVER = this.getServer();
    }

    @Override
    public void onEnable()
    {
        CONFIG      = new Config();
        DATAMANAGER = new DataManager();
        DATAMANAGER.run();
        LOGGER.info("To reload this plugin, simply reload SignShop");
    }

    @Override
    public void onDisable()
    {
        CONFIG      = null;
        DATAMANAGER = null;
        SERVER.getScheduler().cancelTasks(this);
        LOGGER.fine("All managers released and tasks cancelled");
    }
}
