package roycurtis.signshopexport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.wargamer2010.signshop.Seller;
import org.wargamer2010.signshop.configuration.Storage;
import org.wargamer2010.signshop.events.SSCreatedEvent;
import roycurtis.signshopexport.json.Exclusions;
import roycurtis.signshopexport.json.TypeAdapters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import static roycurtis.signshopexport.SignShopExport.*;

/**
 * Manager class for collecting, tracking and saving SignShop data
 */
class DataManager
{
    private File outputFile;
    private Gson gson;

    private JsonArray dataset = new JsonArray();

    private SignCreateListener listener  = new SignCreateListener();

    DataManager(SignShopExport plugin)
    {
        outputFile = new File(plugin.getDataFolder(), CONFIG.outputPath);

        // TODO: debug
        LOGGER.fine("Deleting output file for testing");
        outputFile.delete();

        if ( !outputFile.exists() )
            firstTime();
        else if ( !outputFile.isFile() )
            throw new RuntimeException("outputPath config points to a directory/invalid file");
        else
            load();

        SERVER.getPluginManager().registerEvents(listener, plugin);
        LOGGER.fine("DataManager is listening for sign creation events");
    }

    /**
     * Generates data file of entire SignShop database for the first time
     */
    private void firstTime()
    {
        LOGGER.info("Output file not found; generating for first time");

        gson = new GsonBuilder()
            .addSerializationExclusionStrategy( new Exclusions() )
            .registerTypeAdapterFactory( new TypeAdapters() )
            .setPrettyPrinting()
            .create();

        Collection<Seller> signs = Storage.get().getSellers();

        int done = 0;
        for (Seller sign : signs)
        {
            Record      signRec  = Record.fromSeller(sign);
            JsonElement signJson = gson.toJsonTree(signRec);

            dataset.add(signJson);
            done++;

            if (done % 10 == 0)
                LOGGER.info( done + "/" + signs.size() + " signs processed" );
        }

        try( FileWriter writer = new FileWriter(outputFile) )
        {
            gson.toJson(dataset, writer);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not save data json file", e);
        }

        LOGGER.info( "Done; saved to " + outputFile.getAbsolutePath() );
    }

    private void load()
    {

    }

    class SignCreateListener implements Listener
    {
        @EventHandler(priority = EventPriority.MONITOR)
        public void onSignCreate(SSCreatedEvent event)
        {

        }
    }
}
