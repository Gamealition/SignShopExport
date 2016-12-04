SignShopExport is a Bukkit plugin for 1.10.2+ and either [SignShop 2.11.0][1] or [QuickShop 0.9.22]
[2]. Its sole function is to export a JSON database of shops, for easy public consumption.

# Links

* [Downloads](https://github.com/Gamealition/SignShopExport/releases)
* [BukkitDev](http://dev.bukkit.org/bukkit-plugins/signshopexport/)

# Usage

Simply place into your server's `plugins` directory. As long as you also have SignShop 2.11.0+
or QuickShop 0.9.22 (but not both!), SignShopExport will immediately begin exporting data to the
configured JSON path. When it is done, it will wait until the next interval to export again.

You can set the `exportPath` to somewhere web accessible (e.g. `/var/www`) or you can just create
a symbolic link to the JSON file itself (e.g. `ln -s /var/mc/data.json /var/www/signshop.json`).

## Config

There's only one config file, found at `plugins/SignShopExport/config.yml` with these options:

* `exportPath` - Path of data file to export, relative to server directory. Can be absolute.
* `exportMinPath` - Path of minified data file to export, relative to server directory. The
 minified output is useful for reduced bandwidth usage. Can be absolute.
* `exportInterval` - Seconds between each export, with a minimum of 5 seconds.

## Commands

To reload SignShopExport, do `/signshopexport` in-game or on the console. This will:

* Immediately and safely stop any ongoing exports
* Reload the configuration file from disk
* Immediately trigger an export

Using this command requires console, op or the permission `signshopexport.reload`.

# Building, debugging and debug logging

For instructions and screenshots on how to. . .

* Compile this plugin from scratch
* Build a JAR of this plugin
* Debug this plugin on a server
* Enable debug logging levels such as `FINE` and `FINER`

. . .[please follow the linked guide on this Google document.](https://docs.google.com/document/d/1TTDXG7IZ9M0D2-rzbILAWg1CKjynHK8fNGxbf3W4wBk/view)

[1]: http://dev.bukkit.org/bukkit-plugins/signshop/
[2]: https://dev.bukkit.org/bukkit-plugins/quickshop-notlikeme/
