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
import org.wargamer2010.signshop.events.SSDestroyedEvent;
import roycurtis.signshopexport.json.Exclusions;
import roycurtis.signshopexport.json.TypeAdapters;

import java.io.File;
import java.io.FileReader;
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

    DataManager(SignShopExport plugin)
    {
        outputFile = new File(plugin.getDataFolder(), CONFIG.outputPath);
        gson       = new GsonBuilder()
            .addSerializationExclusionStrategy(new Exclusions())
            .registerTypeAdapterFactory(new TypeAdapters())
            .setPrettyPrinting()
            .create();

        outputFile.delete();

        if ( !outputFile.exists() )
        {
            LOGGER.info("Json file not found; generating for first time");
            init();
        }
        else if ( !outputFile.isFile() )
            throw new RuntimeException("outputPath config points to a directory/invalid file");
        else
            load();

        SERVER.getPluginManager().registerEvents(new SignShopListener(), plugin);
        LOGGER.fine("DataManager is listening for sign creation events");
    }

    /**
     * Generates data file of entire SignShop database for the first time
     */
    private void init()
    {
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

        save();
        LOGGER.info( "Json file created at " + outputFile.getAbsolutePath() );
    }

    private void load()
    {
        try ( FileReader reader = new FileReader(outputFile) )
        {
            dataset = gson.fromJson(reader, JsonArray.class);
            LOGGER.fine("Json loaded with " + dataset.size() + " shops");
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not load json file", e);
        }
        save();
    }

    private void save()
    {
        try( FileWriter writer = new FileWriter(outputFile) )
        {
            gson.toJson(dataset, writer);
            LOGGER.fine("Json saved with " + dataset.size() + " shops");
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not save json file", e);
        }
    }

    class SignShopListener implements Listener
    {
        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void onSignCreate(SSCreatedEvent event)
        {
            Record      signRec  = Record.fromEvent(event);
            JsonElement signJson = gson.toJsonTree(signRec);

            dataset.add(signJson);
            LOGGER.fine("Recorded sign at " + event.getSign().getLocation());
            save();
        }
    }
}
