package roycurtis.signshopexport;

import org.bukkit.configuration.Configuration;

import static roycurtis.signshopexport.SignShopExport.LOGGER;
import static roycurtis.signshopexport.SignShopExport.PLUGIN;

/**
 * Container class for plugin's configuration values
 */
class Config
{
    Configuration config;

    /** Path of data file to export, relative to plugin directory */
    String exportPath = "data.json";
    /** Seconds between each export */
    int exportInterval = 1800;

    Config()
    {
        PLUGIN.saveDefaultConfig();
        PLUGIN.reloadConfig();

        config = PLUGIN.getConfig();

        exportPath     = config.getString("exportPath",     exportPath);
        exportInterval = config.getInt("exportInterval", exportInterval);

        if (exportInterval < 5)
        {
            LOGGER.warning("exportInterval in config is too low; setting to default of 1800");
            exportInterval = 1800;
        }
    }
}