package roycurtis.signshopexport;

import org.bukkit.Server;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.wargamer2010.signshop.SignShop;

import java.util.logging.Logger;

/** Core class of the SignShopExport plugin. Handles listener creation */
public class SignShopExport extends JavaPlugin
{
    static Logger      LOGGER;
    static Config      CONFIG;
    static Server      SERVER;
    static DataManager DATAMANAGER;

    SignShop signShop;

    @Override
    public void onLoad()
    {
        LOGGER = getLogger();
    }

    @Override
    public void onEnable()
    {
        signShop = (SignShop) getServer().getPluginManager().getPlugin("SignShop");

        SERVER      = this.getServer();
        CONFIG      = new Config(this);
        DATAMANAGER = new DataManager(this);

        LOGGER.fine("Plugin fully enabled; listening for SignShop events");
    }

    @Override
    public void onDisable()
    {
        CONFIG      = null;
        DATAMANAGER = null;

        HandlerList.unregisterAll(this);
        LOGGER.fine("Plugin fully disabled; listeners unregistered");
    }
}
