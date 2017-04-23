package roycurtis.signshopexport.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/** Exclusions class for blacklisting objects and fields in Gson */
public class Exclusions implements ExclusionStrategy
{
    @Override
    public boolean shouldSkipField(FieldAttributes f)
    {
        // Ignore redundant data object
        return f.getName().equalsIgnoreCase("data");
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz)
    {
        return false;
    }
}