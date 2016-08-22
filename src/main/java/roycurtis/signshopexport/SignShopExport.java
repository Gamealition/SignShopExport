package roycurtis.signshopexport;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import roycurtis.signshopexport.quickshop.QSDataSource;
import roycurtis.signshopexport.signshop.SSDataSource;

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
        boolean signShop  = getServer().getPluginManager().getPlugin("SignShop")  != null;
        boolean quickShop = getServer().getPluginManager().getPlugin("QuickShop") != null;

        if (!signShop && !quickShop)
        {
            LOGGER.warning("Neither SignShop nor QuickShop are loaded.");
            LOGGER.warning("SignShopExport will do nothing.");
            return;
        }
        else if (signShop && quickShop)
        {
            LOGGER.warning("Both SignShop and QuickShop are loaded.");
            LOGGER.warning("SignShopExport will do nothing, as it cannot handle both.");
            return;
        }

        CONFIG      = new Config();
        DATAMANAGER = new DataManager( signShop ? new SSDataSource() : new QSDataSource() );

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
