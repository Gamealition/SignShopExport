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
* `exportInterval` - Seconds between each export, with a minimum of 5 seconds.

## Commands

To reload SignShopExport, do `/signshopexport` in-game or on the console. This will:

* Immediately and safely stop any ongoing exports
* Reload the configuration file from disk
* Immediately trigger an export

Using this command requires console, op or the permission `signshopexport.reload`.

# Building

SignShopExport uses Maven for dependency management and building. These instructions are simply for
building a jar file of SignShopExport. This is useful for use with CI servers (e.g. Jenkins) or for
checking if the code builds in your development environment.

## Command line (Win/Linux)

*Assuming Maven is [installed to or available in PATH][3]*

1. [Clone this repository using your git client (e.g. 
`git clone https://github.com/Gamealition/SignShopExport.git`)][4]
* Go into repository directory
* [Execute `mvn clean package`][5]
* [Built jar file will be located in the new `target` directory][6]

## IntelliJ

1. [Clone this repository using your git client][7]
* In IntelliJ, go to `File > Open`
* [Navigate to the repository and open the `pom.xml` file][8]
* [Look for and open the "Maven Projects" tab, expand "SignShopExport" and then "Lifecycle"][9]
* [Double-click "Clean" and wait for the process to finish. This will ensure there are no left-over
files from previous Maven builds that may interfere with the final build.][10]
* Double-click "Package" and wait for the process to finish
* [Built jar file will be located in the new `target` directory][11]

# Debugging

These instructions are for running and debugging SignShopExport from within your development
environment. These will help you debug SignShopExport and reload code changes as it runs. [Each of
these steps assumes you have a Bukkit/Spigot/PaperSpigot server locally installed.][12]

## IntelliJ

1. [Clone this repository using your git client][13]
* In IntelliJ, go to `File > Open`
* [Navigate to the repository and open the `pom.xml` file][14]
* Go to `File > Project Structure... > Artifacts`
* [Click `Add > JAR > Empty`, then configure as such:][15]
    * Set Name to "SignShopExport"
    * Set Output directory to the "plugins" folder of your local server
    * Check "Build on make"
* Right-click "'SignShopExport' compile output" and then click "Put into Output Root", then click OK
* Go to `Run > Edit Configurations...`
* [Click `Add New Configuration > JAR Application`, then configure as such:][16]
    * Set Name to "Server" (or "Spigot" or "PaperSpigot", etc)
    * Set Path to JAR to the full path of your local server's executable JAR
        * e.g. `C:\Users\SSEDev\AppData\Local\Programs\Spigot\spigot-1.10.2.jar`
    * Set VM options to "-Xmx2G" (allocates 2GB RAM)
    * Set Working directory to the full path of your local server
        * e.g. `C:\Users\SSEDev\AppData\Local\Programs\Spigot\`
    * Checkmark "Single instance only" on the top right corner
* Under "Before launch", click `Add New Configuration > Build Artifacts`
* Check "SignShopExport" and then click OK twice

After setting up IntelliJ for debugging, all you need to do is press SHIFT+F9 to begin debugging.
This will automatically build a jar, put it in your local server's plugins folder and then start
your server automatically.

## Debug logging

SignShopExport makes use of `FINE` logging levels for debugging. To enable these messages, append
this line to the server's JVM arguments:

> `-Dlog4j.configurationFile=log4j.xml`

Then in the root directory of the server, create the file `log4j.xml` with these contents:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration monitorInterval="5" packages="com.mojang.util">
  <Appenders>
    <Queue name="TerminalConsole">
      <PatternLayout pattern="[%d{HH:mm:ss} %level]: %msg%n"/>
    </Queue>
  </Appenders>
  <Loggers>
    <Root level="INFO">
      <AppenderRef ref="TerminalConsole"/>
    </Root>
    <Logger additivity="false" level="ALL" name="roycurtis.signshopexport.SignShopExport">
      <AppenderRef ref="TerminalConsole"/>
    </Logger>
  </Loggers>
</Configuration>
```

[1]: http://dev.bukkit.org/bukkit-plugins/signshop/
[2]: https://dev.bukkit.org/bukkit-plugins/quickshop-notlikeme/
[3]: https://maven.apache.org/install.html
[4]: http://i.imgur.com/VB7dE6d.png
[5]: http://i.imgur.com/UOzULcl.png
[6]: http://i.imgur.com/bDGVDwW.png
[7]: http://i.imgur.com/VB7dE6d.png
[8]: http://i.imgur.com/zcVkyAm.png
[9]: http://i.imgur.com/TB3Ab4T.png
[10]: http://i.imgur.com/Lx5yPdc.png
[11]: http://i.imgur.com/bDGVDwW.png
[12]: http://i.imgur.com/q0B28cR.png
[13]: http://i.imgur.com/VB7dE6d.png
[14]: http://i.imgur.com/zcVkyAm.png
[15]: http://i.imgur.com/kXsbr3C.png
[16]: http://i.imgur.com/smuYOFs.png