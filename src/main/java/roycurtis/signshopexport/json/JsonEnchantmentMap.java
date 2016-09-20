package roycurtis.signshopexport.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.enchantments.Enchantment;

import java.io.IOException;
import java.util.TreeMap;

/** Type adapter to convert CraftBukkit enchantments to JSON */
class JsonEnchantmentMap<T> extends TypeAdapter<T>
{
    @Override
    public void write(JsonWriter out, T value) throws IOException
    {
        if (value == null)
        {
            out.nullValue();
            return;
        }

        TreeMap<Enchantment, Integer> map = (TreeMap<Enchantment, Integer>) value;
        out.beginObject();

        map.forEach((e, i) -> {
            try
            {
                if (e == null || i == null) return;
                out.name(e.getName()).value(i);
            }
            catch (IOException ignored)
            {
            }
        });

        out.endObject();
    }

    @Override
    /** Not necessary; we're never going to read these */
    public T read(JsonReader in) throws IOException
    {
        return null;
    }
}
