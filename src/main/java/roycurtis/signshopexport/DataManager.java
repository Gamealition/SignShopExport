package roycurtis.signshopexport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.wargamer2010.signshop.Seller;
import org.wargamer2010.signshop.configuration.Storage;
import roycurtis.signshopexport.json.Exclusions;
import roycurtis.signshopexport.json.Record;
import roycurtis.signshopexport.json.TypeAdapters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import static roycurtis.signshopexport.SignShopExport.*;

/**
 * Manager class for collecting, tracking and saving SignShop data
 */
class DataManager implements Runnable
{
    private enum Operation
    {
        Init,
        Serializing,
        Saving
    }

    private Operation currentOp = Operation.Init;
    private File      outputFile;
    private Gson      gson;

    private JsonArray dataSet;
    private Seller[]  signs;
    private int       current;
    private int       total;

    DataManager()
    {
        outputFile = new File(PLUGIN.getDataFolder(), CONFIG.exportPath);
        gson       = new GsonBuilder()
            .addSerializationExclusionStrategy( new Exclusions() )
            .registerTypeAdapterFactory( new TypeAdapters() )
            .setPrettyPrinting()
            .create();

        if ( outputFile.exists() && !outputFile.isFile() )
            throw new RuntimeException("outputPath config points to a directory/invalid file");

        run();
    }

    /** Generates data file of entire SignShop database across server ticks */
    public void run()
    {
        LOGGER.finest("Running DataManager task for operation: " + currentOp);

        switch (currentOp)
        {
            case Init:        doInit();      break;
            case Serializing: doSerialize(); break;
            case Saving:      doSave();      break;
        }
    }

    /** Prepares a copy of SignShop's sellers list for serializing */
    private void doInit()
    {
        current = 0;
        total   = Storage.get().shopCount();
        dataSet = new JsonArray();
        signs   = Storage.get().getSellers().toArray(new Seller[total]);

        LOGGER.fine("Beginning JSON export of " + total + " entries (1 per tick)");
        currentOp = Operation.Serializing;
        SERVER.getScheduler().runTask(PLUGIN, this);
    }

    /** Spends one server tick to serialize one sign */
    private void doSerialize()
    {
        Seller      sign     = signs[current];
        Record      signRec  = Record.fromSeller(sign);
        JsonElement signJson = gson.toJsonTree(signRec);

        dataSet.add(signJson);
        current++;

        if (current >= total)
        {
            LOGGER.fine("Finished serializing all signs");
            currentOp = Operation.Saving;
        }
        else if (current % 10 == 0)
            LOGGER.finer( current + "/" + total + " signs serialized" );

        SERVER.getScheduler().runTask(PLUGIN, this);
    }

    /** Export all the processed shop data and schedule next export */
    private void doSave()
    {
        try( FileWriter writer = new FileWriter(outputFile) )
        {
            gson.toJson(dataSet, writer);
            LOGGER.fine("Json saved with " + dataSet.size() + " shops");
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not save json file", e);
        }
        finally
        {
            dataSet = null;
            signs   = null;
        }

        currentOp = Operation.Init;
        LOGGER.info( "Json file exported to " + outputFile.getAbsolutePath() );
        LOGGER.fine("Scheduling next export in " + CONFIG.exportInterval * 20 + " ticks");
        SERVER.getScheduler().runTaskLater(PLUGIN, this, CONFIG.exportInterval * 20);
    }
}
