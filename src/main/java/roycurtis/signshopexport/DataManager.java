package roycurtis.signshopexport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import roycurtis.signshopexport.json.Exclusions;
import roycurtis.signshopexport.json.Record;
import roycurtis.signshopexport.json.TypeAdapters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

import static roycurtis.signshopexport.SignShopExport.*;

/**
 * Manager class for collecting, serializing and exporting shop data.
 *
 * This manager runs it self in a loop using Bukkit's scheduler. This is so it can spread the task
 * of serializing so many signs across server ticks, rather than cause lag spikes every so often.
 *
 * If the plugin is reloaded or otherwise needs to stop, this manager is safely stopped without any
 * state corruption or cleanup needed.
 */
class DataManager implements Runnable
{
    private enum Operation
    {
        Init,
        Serializing,
        Saving
    }

    private Operation  currentOp = Operation.Init;
    private DataSource dataSource;
    private File       outputFile;
    private File       outputMinFile;
    private Gson       gson;

    private JsonArray dataSet;
    private int       current;
    private int       total;

    DataManager(DataSource source)
    {
        dataSource    = source;
        outputFile    = new File(CONFIG.exportPath);
        outputMinFile = new File(CONFIG.exportMinPath);
        gson          = new GsonBuilder()
            .addSerializationExclusionStrategy( new Exclusions() )
            .registerTypeAdapterFactory( new TypeAdapters() )
            .create();

        if ( outputFile.exists() && !outputFile.isFile() )
            throw new RuntimeException("outputPath config points to a directory/invalid file");

        if ( outputMinFile.exists() && !outputMinFile.isFile() )
            throw new RuntimeException("outputMinPath config points to a directory/invalid file");
    }

    /** Generates data file of entire SignShop database across server ticks */
    public void run()
    {
        switch (currentOp)
        {
            case Init:        doInit();      break;
            case Serializing: doSerialize(); break;
            case Saving:      doSave();      break;
        }
    }

    /** Prepares the source's shops database for serializing */
    private void doInit()
    {
        current = 0;
        total   = dataSource.prepare();
        dataSet = new JsonArray();

        if (total <= 0)
        {
            LOGGER.fine( "There are no shops to export. Doing nothing." );
            LOGGER.fine("Scheduling next check in " + CONFIG.exportInterval * 20 + " ticks");
            SERVER.getScheduler().runTaskLater(PLUGIN, this, CONFIG.exportInterval * 20);
            return;
        }

        LOGGER.fine("Beginning JSON export of " + total + " entries (1 per tick)");
        currentOp = Operation.Serializing;
        SERVER.getScheduler().runTaskLater(PLUGIN, this, 1);
    }

    /** Serializes one sign every tick */
    private void doSerialize()
    {
        Record      signRec  = null;
        JsonElement signJson = null;

        try
        {
            signRec  = dataSource.createRecordForIndex(current);
            signJson = gson.toJsonTree(signRec);

            dataSet.add(signJson);
        }
        catch (Exception ex)
        {
            LOGGER.info("Skipping sign " + current + " as it failed to serialize. " +
                "This is likely because it was changed mid-process");

            LOGGER.fine("Details for sign " + current + ":");
            LOGGER.fine(signRec == null
                ? "Could not generate record from data source"
                : signRec.toString() );

            LOGGER.log(Level.FINE, "Exception for sign " + current, ex);
        }

        current++;

        if (current >= total)
        {
            LOGGER.fine("Finished serializing all signs");
            currentOp = Operation.Saving;
        }
        else if (current % 10 == 0)
            LOGGER.finer( current + "/" + total + " signs serialized" );

        SERVER.getScheduler().runTaskLater(PLUGIN, this, 1);
    }

    /** Export all the processed shop data, free resources and schedule next export */
    private void doSave()
    {
        doSaveFile(outputFile, false);
        doSaveFile(outputMinFile, true);

        dataSource.free();
        dataSet = null;
        current = 0;
        total   = 0;

        currentOp = Operation.Init;
        LOGGER.fine("Scheduling next export in " + CONFIG.exportInterval * 20 + " ticks");
        SERVER.getScheduler().runTaskLater(PLUGIN, this, CONFIG.exportInterval * 20);
    }

    private void doSaveFile(File file, boolean minified)
    {
        try (
            FileWriter fWriter = new FileWriter(file);
            JsonWriter jWriter = new JsonWriter(fWriter)
        )
        {
            if (!minified)
                jWriter.setIndent("  ");

            gson.toJson(dataSet, jWriter);
            LOGGER.fine( "Json file exported to " + file.getAbsolutePath() );
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not save json file", e);
        }
    }
}
