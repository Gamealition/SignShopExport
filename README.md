SignShopExport is a Bukkit plugin for 1.10.2+ and SignShop 2.11.0. Its sole function is to generate
a public database of shops, using a friendlier and more comprehensive format than `sellers.yml`.

# Links

* [Downloads](https://github.com/Gamealition/SignShopExport/releases)
* [BukkitDev](http://dev.bukkit.org/bukkit-plugins/signshopexport/)

# Building

SignShopExport uses Maven for dependency management and building. These instructions are simply for
building a jar file of SignShopExport. This is useful for use with CI servers (e.g. Jenkins) or for
checking if the code builds in your development environment.

## Command line (Win/Linux)

*Assuming Maven is [installed to or available in PATH](https://maven.apache.org/install.html)*

1. [Clone this repository using your git client (e.g. 
`git clone https://github.com/Gamealition/SignShopExport.git`)](http://i.imgur.com/VB7dE6d.png)
* Go into repository directory
* [Execute `mvn clean package`](http://i.imgur.com/UOzULcl.png)
* [Built jar file will be located in the new `target` directory](http://i.imgur.com/bDGVDwW.png)

## IntelliJ

1. [Clone this repository using your git client](http://i.imgur.com/VB7dE6d.png)
* In IntelliJ, go to `File > Open`
* [Navigate to the repository and open the `pom.xml` file](http://i.imgur.com/zcVkyAm.png)
* [Look for and open the "Maven Projects" tab, expand "SignShopExport" and then "Lifecycle"](http://i.imgur.com/TB3Ab4T.png)
* [Double-click "Clean" and wait for the process to finish. This will ensure there are no left-over
files from previous Maven builds that may interfere with the final build.](http://i.imgur.com/Lx5yPdc.png)
* Double-click "Package" and wait for the process to finish
* [Built jar file will be located in the new `target` directory](http://i.imgur.com/bDGVDwW.png)

# Debugging

These instructions are for running and debugging SignShopExport from within your development
environment. These will help you debug SignShopExport and reload code changes as it runs. [Each of
these steps assumes you have a Bukkit/Spigot/PaperSpigot server locally installed.](http://i.imgur.com/q0B28cR.png)

## IntelliJ

1. [Clone this repository using your git client](http://i.imgur.com/VB7dE6d.png)
* In IntelliJ, go to `File > Open`
* [Navigate to the repository and open the `pom.xml` file](http://i.imgur.com/zcVkyAm.png)
* Go to `File > Project Structure... > Artifacts`
* [Click `Add > JAR > Empty`, then configure as such:](http://i.imgur.com/kXsbr3C.png)
    * Set Name to "SignShopExport"
    * Set Output directory to the "plugins" folder of your local server
    * Check "Build on make"
* Right-click "'SignShopExport' compile output" and then click "Put into Output Root", then click OK
* Go to `Run > Edit Configurations...`
* [Click `Add New Configuration > JAR Application`, then configure as such:](http://i.imgur.com/smuYOFs.png)
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
