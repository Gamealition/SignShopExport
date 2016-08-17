package roycurtis.signshopexport.json;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.io.IOException;
import java.util.TreeMap;

public class TypeAdapters implements TypeAdapterFactory
{
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type)
    {
        String name = type.getRawType().getSimpleName();

        // org.bukkit.craftbukkit.v1_10_R1.inventory.CraftMetaItem.EnchantmentMap
        // Needed because Gson does not serialize this very nicely
        if ( name.equalsIgnoreCase("EnchantmentMap") )
            return new JsonEnchantmentMap<>();

        // org.bukkit.craftbukkit.v1_10_R1.inventory.CraftMetaEnchantedBook
        // Needed because Gson chokes on duplicate `enchantments` field
        if ( name.equalsIgnoreCase("CraftMetaEnchantedBook") )
            return new JsonCraftMetaEnchantedBook<>();

        return null;
    }

    class JsonEnchantmentMap <T> extends TypeAdapter<T>
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
                catch (IOException ignored) { }
            });

            out.endObject();
        }

        @Override
        public T read(JsonReader in) throws IOException
        {
            return null;
        }
    }

    class JsonCraftMetaEnchantedBook <T> extends TypeAdapter<T>
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
            if (meta.hasDisplayName())
                out.name("displayName").value(meta.getDisplayName());

            if (meta.hasLore())
                out.name("lore").beginArray()
                    .value(meta.getLore().get(0))
                    .value(meta.getLore().get(1))
                    .value(meta.getLore().get(2))
                    .value(meta.getLore().get(3))
                    .endArray();

            // Enchantments
            out.name("enchantments").beginObject();
            meta.getStoredEnchants().forEach((e, i) -> {
                try
                {
                    if (e == null || i == null) return;
                    out.name(e.getName()).value(i);
                }
                catch (IOException ignored) { }
            });
            out.endObject();

            out.endObject();
        }

        @Override
        public T read(JsonReader in) throws IOException
        {
            return null;
        }
    }
}
