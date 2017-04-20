package roycurtis.signshopexport.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.inventory.meta.MapMeta;

import java.io.IOException;

/** Type adapter to convert CraftBukkit maps to JSON */
class JsonMetaMap<T> extends TypeAdapter<T>
{
    @Override
    public void write(JsonWriter out, T value) throws IOException
    {
        if (value == null)
        {
            out.nullValue();
            return;
        }

        MapMeta map = (MapMeta) value;
        out.beginObject();

        // Metadata
        // TODO: make this common with JsonCraftMetaEnchantedBook
        if ( map.hasDisplayName() )
            out.name("displayName").value(map.getDisplayName());

        if ( map.hasLore() )
            out.name("lore").beginArray()
                .value(map.getLore().get(0))
                .value(map.getLore().get(1))
                .value(map.getLore().get(2))
                .value(map.getLore().get(3))
                .endArray();

        if ( map.hasLocationName() )
            out.name("location").value( map.getLocationName() );

        if ( map.hasColor() )
            out.name("color").value( map.getColor().toString() );

        out.endObject();
    }

    /** Not necessary; we're never going to read these */
    @Override
    public T read(JsonReader in) throws IOException
    {
        return null;
    }
}
