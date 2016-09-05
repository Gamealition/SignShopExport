package roycurtis.signshopexport.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/** Exclusions class for blacklisting objects and fields in Gson */
public class Exclusions implements ExclusionStrategy
{
    @Override
    public boolean shouldSkipField(FieldAttributes f)
    {
        String name = f.getName();

        return
            // Ignore dynamic CraftBukkit handle field
            name.equalsIgnoreCase("handle")
            // Ignore book page contents
            || name.equalsIgnoreCase("pages")
            // Ignore unsupported tags
            || name.equalsIgnoreCase("unhandledTags")
            // Ignore redundant data object
            || name.equalsIgnoreCase("data")
            // Ignore hide flags
            || name.equalsIgnoreCase("hideFlag")
            // Ignore repair costs
            || name.equalsIgnoreCase("repairCost")
            // Ignore skull profiles
            || name.equalsIgnoreCase("profile")
            // Ignore shield patterns
            || name.equalsIgnoreCase("blockEntityTag");
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz)
    {
        String name = clazz.getSimpleName();

        if ( name.equalsIgnoreCase("ItemStack") )
        if ( clazz.getTypeName().startsWith("net.minecraft.server") )
            return true;

        return name.equalsIgnoreCase("ChatComponentText");
    }
}