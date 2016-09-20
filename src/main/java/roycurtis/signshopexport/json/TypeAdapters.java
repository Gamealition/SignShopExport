package roycurtis.signshopexport.json;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

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

}
