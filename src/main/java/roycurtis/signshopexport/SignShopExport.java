package roycurtis.signshopexport;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
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
        Plugin  signShop     = SERVER.getPluginManager().getPlugin("SignShop");
        Plugin  quickShop    = SERVER.getPluginManager().getPlugin("QuickShop");
        boolean hasSignShop  = signShop  != null && signShop.isEnabled();
        boolean hasQuickShop = quickShop != null && quickShop.isEnabled();

        if (!hasSignShop && !hasQuickShop)
        {
            LOGGER.warning("Neither SignShop nor QuickShop are loaded or enabled.");
            LOGGER.warning("SignShopExport will do nothing.");
            return;
        }
        else if (hasSignShop && hasQuickShop)
        {
            LOGGER.warning("Both SignShop and QuickShop are loaded and enabled.");
            LOGGER.warning("SignShopExport will do nothing, as it cannot handle both.");
            return;
        }

        CONFIG      = new Config();
        DATAMANAGER = new DataManager( hasSignShop ? new SSDataSource() : new QSDataSource() );

        DATAMANAGER.run();
        LOGGER.info("To reload this plugin, do `/signshopexport`");
    }

    @Override
    public void onDisable()
    {
        CONFIG      = null;
        DATAMANAGER = null;
        SERVER.getScheduler().cancelTasks(this);
        LOGGER.fine("All managers released and tasks cancelled");
    }

    @Override
    public boolean onCommand(CommandSender who, Command what, String label, String[] args)
    {
        this.setEnabled(false);
        this.setEnabled(true);
        who.sendMessage("*** SignShopExport's config reloaded; export triggered");
        return true;
    }
}
