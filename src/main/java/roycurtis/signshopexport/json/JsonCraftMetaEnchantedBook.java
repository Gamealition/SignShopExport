package roycurtis.signshopexport.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.io.IOException;

/** Type adapter to convert CraftBukkit enchanted book data to JSON */
class JsonCraftMetaEnchantedBook<T> extends TypeAdapter<T>
{
    @Override
    public void write(JsonWriter out, T value) throws IOException
    {
        if (value == null)
        {
            out.nullValue();
            return;
        }

        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) value;
        out.beginObject();

        // Metadata
        if ( meta.hasDisplayName() )
            out.name("displayName").value(meta.getDisplayName());

        if ( meta.hasLore() )
            out.name("lore").beginArray()
                .value(meta.getLore().get(0))
                .value(meta.getLore().get(1))
                .value(meta.getLore().get(2))
                .value(meta.getLore().get(3))
                .endArray();

        // Enchantments
        out.name("enchantments").beginObject();
        meta.getStoredEnchants().forEach( (e, i) -> {
            try
            {
                if (e == null || i == null) return;
                out.name( e.getName() ).value(i);
            }
            catch (IOException ignored) { }
        } );
        out.endObject();

        out.endObject();
    }

    /** Not necessary; we're never going to read these */
    @Override
    public T read(JsonReader in) throws IOException
    {
        return null;
    }
}
