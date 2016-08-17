package roycurtis.signshopexport;

import org.bukkit.configuration.Configuration;

/**
 * Container class for plugin's configuration values
 */
class Config
{
    Configuration config;

    /** Path of data file to export, relative to plugin directory */
    String outputPath = "data.json";

    Config(SignShopExport plugin)
    {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        config = plugin.getConfig();

        outputPath = config.getString("outputPath", outputPath);
    }
}