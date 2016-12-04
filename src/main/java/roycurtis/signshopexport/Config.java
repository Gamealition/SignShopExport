package roycurtis.signshopexport;

import org.bukkit.configuration.Configuration;

import static roycurtis.signshopexport.SignShopExport.LOGGER;
import static roycurtis.signshopexport.SignShopExport.PLUGIN;

/** Container and manager class for plugin's configuration values */
class Config
{
    private Configuration config;

    /** Path of data file to export, relative to server directory or absolute */
    String exportPath     = "plugins/SignShopExport/data.json";
    /** Path of minified data file to export, relative to server directory or absolute */
    String exportMinPath  = "plugins/SignShopExport/data.min.json";
    /** Seconds between each export */
    int    exportInterval = 1800;

    Config()
    {
        PLUGIN.saveDefaultConfig();
        PLUGIN.reloadConfig();

        config = PLUGIN.getConfig();

        exportPath     = config.getString("exportPath", exportPath);
        exportMinPath  = config.getString("exportMinPath", exportMinPath);
        exportInterval = config.getInt("exportInterval", exportInterval);

        if (exportInterval < 5)
        {
            LOGGER.warning("exportInterval in config is too low; setting to default of 1800");
            exportInterval = 1800;
        }
    }
}