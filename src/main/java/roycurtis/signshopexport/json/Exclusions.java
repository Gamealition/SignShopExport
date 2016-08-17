package roycurtis.signshopexport.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class Exclusions implements ExclusionStrategy
{
    @Override
    public boolean shouldSkipField(FieldAttributes f)
    {
        String name = f.getName();

        // Ignore book page contents
        return name.equalsIgnoreCase("pages")
            // Ignore unsupported tags
            || name.equalsIgnoreCase("unhandledTags")
            // Ignore redundant data object
            || name.equalsIgnoreCase("data")
            // Ignore hide flags
            || name.equalsIgnoreCase("hideFlag")
            // Ignore shield patterns
            || name.equalsIgnoreCase("blockEntityTag");
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz)
    {
        String name = clazz.getSimpleName();
        return name.equalsIgnoreCase("ChatComponentText");
    }
}