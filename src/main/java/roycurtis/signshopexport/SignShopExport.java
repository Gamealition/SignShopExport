package roycurtis.signshopexport;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.wargamer2010.signshop.SignShop;

import java.util.logging.Logger;

/** Core class of the SignShopExport plugin. Handles listener creation */
public class SignShopExport extends JavaPlugin
{
    static SignShopExport PLUGIN;
    static Logger         LOGGER;
    static Config         CONFIG;
    static Server         SERVER;
    static DataManager    DATAMANAGER;

    SignShop signShop;

    @Override
    public void onLoad()
    {
        PLUGIN = this;
        LOGGER = getLogger();
    }

    @Override
    public void onEnable()
    {
        signShop = (SignShop) getServer().getPluginManager().getPlugin("SignShop");

        SERVER      = this.getServer();
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
