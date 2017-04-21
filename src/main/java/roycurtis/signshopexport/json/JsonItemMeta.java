package roycurtis.signshopexport.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.*;

import java.io.IOException;
import java.util.Map;

/** Type adapter to convert ItemMeta subtypes to JSON */
class JsonItemMeta<T> extends TypeAdapter<T>
{
    @Override
    public void write(JsonWriter out, T value) throws IOException
    {
        // Handle nulls (sometimes possible, if item has no meta)
        if (value == null)
        {
            out.nullValue();
            return;
        }

        out.beginObject();

        handleGeneral(out, (ItemMeta) value);
        handleEnchants(out, (ItemMeta) value);

        if (value instanceof BannerMeta)
            handleBanner(out, (BannerMeta) value);

        if (value instanceof FireworkMeta)
            handleFirework(out, (FireworkMeta) value);

        if (value instanceof FireworkEffectMeta)
            handleFireworkEffect(out, (FireworkEffectMeta) value);

        if (value instanceof LeatherArmorMeta)
            handleLeatherArmor(out, (LeatherArmorMeta) value);

        if (value instanceof MapMeta)
            handleMaps(out, (MapMeta) value);

        if (value instanceof PotionMeta)
            handlePotions(out, (PotionMeta) value);

        if (value instanceof SkullMeta)
            handleSkull(out, (SkullMeta) value);

        if (value instanceof SpawnEggMeta)
            handleSpawnEgg(out, (SpawnEggMeta) value);

        out.endObject();
    }

    private void handleGeneral(JsonWriter out, ItemMeta meta) throws IOException
    {
        if ( meta.hasDisplayName() )
            out.name("displayName").value( meta.getDisplayName() );

        if ( meta.hasLore() )
            out.name("lore").beginArray()
                .value( meta.getLore().get(0) )
                .value( meta.getLore().get(1) )
                .value( meta.getLore().get(2) )
                .value( meta.getLore().get(3) )
                .endArray();

        // Only add "unbreakable" tag if true, since it's non-survival
        if ( meta.isUnbreakable() )
            out.name("unbreakable").value(true);
    }

    /**
     * Enchanted items have enchants in two separate locations:
     *
     * * ItemMeta.getEnchants() - If it's an enchanted item (e.g. sword)
     * * EnchantmentStorageMeta.getStoredEnchants() - If it's an enchanted book
     *
     * For cleaner data output, both are output to the same "enchantments" JSON object
     */
    private void handleEnchants(JsonWriter out, ItemMeta meta) throws IOException
    {
        Map<Enchantment, Integer> enchants = null;

        if ( meta.hasEnchants() )
            enchants = meta.getEnchants();
        else if (meta instanceof EnchantmentStorageMeta)
        {
            EnchantmentStorageMeta enchantMeta = (EnchantmentStorageMeta) meta;

            if ( enchantMeta.hasStoredEnchants() )
                enchants = enchantMeta.getStoredEnchants();
        }

        if (enchants == null)
            return;

        out.name("enchantments").beginObject();

        enchants.forEach( (e, i) -> {
            try
            {
                if (e == null || i == null) return;
                out.name( e.getName() ).value(i);
            }
            catch (IOException ignored) { }
        } );

        out.endObject();
    }

    private void handleBanner(JsonWriter out, BannerMeta meta) throws IOException
    {

    }

    private void handleFirework(JsonWriter out, FireworkMeta meta) throws IOException
    {

    }

    private void handleFireworkEffect(JsonWriter out, FireworkEffectMeta meta) throws IOException
    {

    }

    private void handleLeatherArmor(JsonWriter out, LeatherArmorMeta meta) throws IOException
    {

    }

    private void handleMaps(JsonWriter out, MapMeta meta) throws IOException
    {
        if ( meta.hasLocationName() )
            out.name("location").value( meta.getLocationName() );

        if ( meta.hasColor() )
            out.name("color").value( meta.getColor().toString() );
    }

    private void handlePotions(JsonWriter out, PotionMeta meta) throws IOException
    {

    }

    private void handleSkull(JsonWriter out, SkullMeta meta) throws IOException
    {

    }

    private void handleSpawnEgg(JsonWriter out, SpawnEggMeta meta) throws IOException
    {

    }

    /** Not necessary; we're never going to read these */
    @Override
    public T read(JsonReader in) throws IOException
    {
        return null;
    }
}
