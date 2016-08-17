package roycurtis.signshopexport;

import org.bukkit.configuration.Configuration;

import java.util.logging.Logger;

/**
 * Container class for plugin's configuration values
 */
class Config
{
    Configuration config;

    /** Path of data file to export, relative to plugin directory */
    String outputFile = "data.json";

    Config(SignShopExport plugin)
    {
        Logger LOGGER = SignShopExport.LOGGER;

        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        config = plugin.getConfig();

        outputFile = config.getString("outputFile", outputFile);
    }
}