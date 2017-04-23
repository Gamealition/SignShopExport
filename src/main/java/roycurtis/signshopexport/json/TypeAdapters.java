package roycurtis.signshopexport.json;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Adapter factory for explaining to Gson how to serialize some Minecraft objects.
 *
 * This uses a TypeAdapterFactory because sometimes we have to deal with CraftBukkit or native
 * Minecraft classes. We can't refer to these using generics, because we only use Bukkit API here.
 */
public class TypeAdapters implements TypeAdapterFactory
{
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type)
    {
        // Needed to handle various subtypes of ItemMeta
        if ( ItemMeta.class.isAssignableFrom( type.getRawType() ) )
            return new JsonItemMeta<>();

        return null;
    }

}
