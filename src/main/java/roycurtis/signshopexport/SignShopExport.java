package roycurtis.signshopexport;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.wargamer2010.signshop.SignShop;

import java.util.logging.Logger;

/** Core class of the SignShopExport plugin. Handles listener creation */
public class SignShopExport extends JavaPlugin implements Listener
{
    static Logger LOGGER;
    static Config CONFIG;

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

        CONFIG = new Config(this);

        getServer().getPluginManager().registerEvents(this, this);
        LOGGER.fine("Plugin fully enabled; listening for SignShop events");
    }

    @Override
    public void onDisable()
    {
        CONFIG = null;
        HandlerList.unregisterAll((Listener) this);
        LOGGER.fine("Plugin fully disabled; listener unregistered");
    }
}
