package roycurtis.signshopexport.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.util.Arrays;

/** Exclusions class for blacklisting objects and fields in Gson */
public class Exclusions implements ExclusionStrategy
{
    private static final String[] SKIPPED_FIELDS = new String[]
    {
        // Ignore dynamic CraftBukkit handle field
        "handle",
        // Ignore book page contents
        "pages",
        // Ignore unsupported tags
        "unhandledTags",
        // Ignore redundant data object
        "data",
        // Ignore hide flags
        "hideFlag",
        // Ignore repair costs
        "repairCost",
        // Ignore skull profiles
        "profile",
        // Ignore shield patterns
        "blockEntityTag",
        // Ignore 1.11 unbreakable tag
        "unbreakable"
    };

    @Override
    public boolean shouldSkipField(FieldAttributes f)
    {
        return Arrays.stream(SKIPPED_FIELDS)
            .anyMatch( s -> s.equalsIgnoreCase( f.getName() ) );
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz)
    {
        String name = clazz.getSimpleName();

        // Skips vanilla ItemStacks, preferring Bukkit ones instead
        if ( name.equalsIgnoreCase("ItemStack") )
        if ( clazz.getTypeName().startsWith("net.minecraft.server") )
            return true;

        // Skip strings in books, etc
        return name.equalsIgnoreCase("ChatComponentText");
    }
}